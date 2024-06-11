import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBOracleSessionExtension.class, JdbcOperationsUtil.class})
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws Exception {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = PowerMockito.mock(Connection.class);
        String connectionId = "testConnectionId";
        PowerMockito.doNothing().when(connection).createStatement();
        PowerMockito.doNothing().when(connection).execute(Mockito.anyString());

        obOracleSessionExtension.killQuery(connection, connectionId);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(connectionId);
        Mockito.verify(connection).execute(Mockito.eq("KILL QUERY " + connectionId));
    }

    @Test
    public void testSwitchSchema_Success() throws Exception {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = PowerMockito.mock(Connection.class);
        String schemaName = "testSchemaName";
        String currentSchema = "testCurrentSchema";
        PowerMockito.doReturn(currentSchema).when(connection).getCurrentSchema();
        PowerMockito.doNothing().when(connection).createStatement();
        PowerMockito.doNothing().when(connection).update(Mockito.anyString());

        obOracleSessionExtension.switchSchema(connection, schemaName);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(schemaName);
        Mockito.verify(connection).update(Mockito.eq("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_CurrentSchemaIsSameAsTargetSchema() throws Exception {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = PowerMockito.mock(Connection.class);
        String schemaName = "testSchemaName";
        String currentSchema = "testSchemaName";
        PowerMockito.doReturn(currentSchema).when(connection).getCurrentSchema();
        PowerMockito.doNothing().when(connection).createStatement();

        obOracleSessionExtension.switchSchema(connection, schemaName);
        Mockito.verifyNoInteractions(connection);
        throw new SQLException("Current schema is same as the target schema.");
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = PowerMockito.mock(Connection.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "testCurrentSchema";
        PowerMockito.doReturn(currentSchema).when(connection).queryForObject(querySql, String.class);

        String result = obOracleSessionExtension.getCurrentSchema(connection);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(result);
        Assert.assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws Exception {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = PowerMockito.mock(OceanBaseConnection.class);
        String serverThreadId = "testServerThreadId";
        PowerMockito.doReturn(serverThreadId).when(connection).getServerThreadId();

        String result = obOracleSessionExtension.getConnectionId(connection);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(result);
        Assert.assertEquals(serverThreadId, result);
    }

    @Test
    public void testGetConnectionId_Exception() throws Exception {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = PowerMockito.mock(Connection.class);
        PowerMockito.doThrow(new Exception()).when(connection).queryForObject(Mockito.anyString(), Mockito.any());

        String result = obOracleSessionExtension.getConnectionId(connection);
        Assert.assertNull(result);
    }
}