import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(connection.createStatement()).thenReturn(PowerMockito.mock(java.sql.Statement.class));
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
        PowerMockito.verifyStatic(Mockito.times(1));
        Mockito.verify(connection.createStatement()).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testKillQuery_Exception() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
        PowerMockito.verifyStatic(Mockito.never());
        Mockito.verifyNoMoreInteractions(connection.createStatement());
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "testSchemaName");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals("testSchemaName", currentSchema);
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        String currentSchema = "testCurrentSchema";
        when(connection.createStatement()).thenReturn(PowerMockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeUpdate(any(String.class))).thenReturn(1);
        when(connection.createStatement().executeQuery(any(String.class))).thenReturn(PowerMockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(any(String.class))).thenReturn(PowerMockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(any(String.class))).thenReturn(PowerMockito.mock(java.sql.ResultSet.class));
        obOracleSessionExtension.switchSchema(connection, "testSchemaName");
        String newSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assert.assertNotEquals(currentSchema, newSchema);
        PowerMockito.verifyStatic(Mockito.times(1));
        Mockito.verify(connection.createStatement()).executeUpdate("ALTER SESSION SET CURRENT_SCHEMA=testSchemaName");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Exception() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.switchSchema(connection, "testSchemaName");
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String expectedSchema = "testSchema";
        when(connection.createStatement()).thenReturn(PowerMockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(querySql)).thenReturn(PowerMockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(querySql).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(querySql).getString("CURRENT_SCHEMA")).thenReturn(expectedSchema);
        String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection.createStatement()).thenReturn(PowerMockito.mock(java.sql.Statement.class));
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        String expectedConnectionId = "testConnectionId";
        if (connection instanceof OceanBaseConnection) {
            PowerMockito.verifyStatic(Mockito.times(1));
            Mockito.verifyStatic().when(Mockito.atLeastOnce()).ofInstance(OceanBaseConnection.class).getServerThreadId();
            Mockito.verifyStatic().when(Mockito.atLeastOnce()).ofInstance(OceanBaseConnection.class).getServerThreadId().equals(expectedConnectionId);
            PowerMockito.verifyStatic().when(Mockito.atLeastOnce()).ofInstance(OceanBaseConnection.class).getServerThreadId().thenReturn(expectedConnectionId);
            Assert.assertEquals(expectedConnectionId, connectionId);
        } else {
            String querySql = "select userenv('sessionid') from dual";
            PowerMockito.verifyStatic(Mockito.times(1));
            Mockito.verify(connection.createStatement()).queryForObject(querySql, eq(String.class));
            PowerMockito.verify(connection.createStatement()).queryForObject(querySql, eq("testConnectionId"));
            Assert.assertEquals("testConnectionId", connectionId);
        }
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        Assert.assertNull(connectionId);
    }
}