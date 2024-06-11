import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension extension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        extension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "1");
        // Add your assertions here 
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        extension.switchSchema(connection, "schema");
        // Add your assertions here 
    }
  
    @Test
    public void testGetCurrentSchema() {
        String schema = extension.getCurrentSchema(connection);
        // Add your assertions here 
    }

    @Test
    public void testGetConnectionId() {
        String id = extension.getConnectionId(connection);
        // Add your assertions here 
    }

    // Add more tests here for edge cases and exceptions etc.

}