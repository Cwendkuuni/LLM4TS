import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import com.oceanbase.odc.plugin.connect.oboracle.*;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension obj = new OBOracleSessionExtension();

    @Test
    public void test_killQuery() {
    //Logic to test killQuery() method
    }

    @Test
    public void test_switchSchema() {
    //Logic to test switchSchema() method
    }

    @Test
    public void test_getCurrentSchema() {
    //Logic to test getCurrentSchema() method
    }

    @Test
    public void test_getConnectionId() {
    //Logic to test getConnectionId() method
    }

    @Test(expected = NullPointerException.class)
    public void test_killQuery_nullConnection() {
    //Logic to test killQuery() method when connection is null
    }

    @Test
    public void test_getConnectionId_NotInstanceOfOceanBaseConnection() {
    //Logic to test getConnectionId() method when connection object is not instance of OceanBaseConnection
    }

    @Test(expected = SQLException.class)
    public void test_switchSchema_disconnected() {
    //Logic to test switchSchema() method when connection is disconnected
    }
}