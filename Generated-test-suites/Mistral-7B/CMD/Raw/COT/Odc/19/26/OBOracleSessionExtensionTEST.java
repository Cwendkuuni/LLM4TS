package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;

/**
 * Test class for OBOracleSessionExtension
 */
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection connectionMock;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() {
        extension = new OBOracleSessionExtension();
        connectionMock = Mockito.mock(Connection.class);
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        Mockito.when(connectionMock.getConnection()).thenReturn(connectionMock);
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connectionMock.getSchemaName()).thenReturn("testSchema");
        Mockito.when(connectionMock.getAutoCommit()).thenReturn(true);
        Mockito.when(connectionMock.isClosed()).thenReturn(false);
        Mockito.when(connectionMock.isClosed()).thenAnswer(i -> false);

        JdbcOperationsUtil.setJdbcOperations(extension, jdbcOperationsMock);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testConnectionId";
        extension.killQuery(connectionMock, connectionId);
        Mockito.verify(jdbcOperationsMock, Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        extension.killQuery(null, "testConnectionId");
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertThrows(SQLException.class, () -> extension.killQuery(null, "testConnectionId"));
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        extension.killQuery(connectionMock, null);
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertThrows(NullPointerException.class, () -> extension.killQuery(connectionMock, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchemaName = "testNewSchema";
        extension.switchSchema(connectionMock, newSchemaName);
        Mockito.verify(jdbcOperationsMock, Mockito.times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(newSchemaName));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        String newSchemaName = "testNewSchema";
        extension.switchSchema(null, newSchemaName);
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertThrows(SQLException.class, () -> extension.switchSchema(null, newSchemaName));
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        extension.switchSchema(connectionMock, null);
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertThrows(NullPointerException.class, () -> extension.switchSchema(connectionMock, null));
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchemaName = "testSchema";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(expectedSchemaName);
        String actualSchemaName = extension.getCurrentSchema(connectionMock);
        Mockito.verify(jdbcOperationsMock, Mockito.times(1)).queryForObject(Mockito.anyString(), Mockito.eq(String.class));
        assertEquals(expectedSchemaName, actualSchemaName);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException());
        String actualSchemaName = extension.getCurrentSchema(null);
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertThrows(SQLException.class, () -> extension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "testConnectionId";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(expectedConnectionId);
        String actualConnectionId = extension.getConnectionId(connectionMock);
        Mockito.verify(jdbcOperationsMock, Mockito.times(1)).queryForObject(Mockito.anyString(), Mockito.eq(String.class));
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithClosedConnection() {
        String expectedConnectionId = "testConnectionId";
        Mockito.when(connectionMock.isClosed()).thenReturn(true);
        String actualConnectionId = extension.getConnectionId(connectionMock);
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        String expectedConnectionId = "testServerThreadId";
        Mockito.when(connectionMock instanceof OceanBaseConnection).thenReturn(true);
        Mockito.when(connectionMock.getClass()).thenReturn(OceanBaseConnection.class);
        Mockito.when(connectionMock.getServerThreadId()).thenReturn(expectedConnectionId);
        String actualConnectionId = extension.getConnectionId(connectionMock);
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithNonOceanBaseConnection() {
        String actualConnectionId = extension.getConnectionId(connectionMock);
        Mockito.verifyNoInteractions(jdbcOperationsMock);
        assertEquals(null, actualConnectionId);
    }
}