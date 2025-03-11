package ro.mpp2024;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("finding task with manufacturerN {} ",manufacturerN);
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Masini where manufacturer=?")) {
            preStmt.setString(1, manufacturerN);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                }
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry("finding task with min {} and max {} ",min,max);
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Masini where year between ? and ?")) {
            preStmt.setInt(1, min);
            preStmt.setInt(2, max);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                }
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task {} ",elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Masini (manufacturer, model, year) values (?,?,?)")){
            preStmt.setString(1,elem.getManufacturer());
            preStmt.setString(2,elem.getModel());
            preStmt.setInt(3,elem.getYear());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances",result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("updating task with id {} ",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Masini set manufacturer=?, model=?, year=? where id=?")){
            preStmt.setString(1,elem.getManufacturer());
            preStmt.setString(2,elem.getModel());
            preStmt.setInt(3,elem.getYear());
            preStmt.setInt(4,integer);
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances",result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Masini")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        logger.traceExit(cars);
        return cars;
    }
}
