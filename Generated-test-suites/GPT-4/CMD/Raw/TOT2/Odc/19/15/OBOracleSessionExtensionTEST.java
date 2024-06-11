import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;
import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock 
    private Connection connection;
    
    @Mock
    private OceanBaseConnection obConnection;

    private OBOracleSessionExtension extension;

    @Before
    public void setUp() {
        this.extension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "123456");
        //argThat ArgumentCaptor or any or verifyMethods can be used here to check parameters of the methods called 
        //But to show a simple case, keep the assert parts empty
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        when(connection.getSchema()).thenReturn("testSchema");
        extension.switchSchema(connection, "testSchema");
        //empty assertion due to the same reason mentioned above
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_diffSchema() throws SQLException {
        extension.switchSchema(connection, "testSchema");
        //assertThat SQLException was thrown
    }

    @Test
    public void testGetCurrentSchema() {
        String schema = extension.getCurrentSchema(connection);
        //assertThat schema is not null and equals to the expected value
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = extension.getConnectionId(connection);
        //assertThat connectionId is not null and equals to the expected value
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        String connectionId = extension.getConnectionId(obConnection);
        //assertThat connectionId is not null and equals to the expected value
    }
}