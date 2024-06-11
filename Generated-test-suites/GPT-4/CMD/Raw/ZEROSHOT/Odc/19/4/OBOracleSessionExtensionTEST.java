
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection mockConnection;
    private OceanBaseConnection mockOceanBaseConnection;
    private JdbcOperationsUtil.JdbcOperations mockJdbcOperations;

    @Before
    public void setUp() {
        extension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
        mockOceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        mockJdbcOperations = Mockito.mock(JdbcOperationsUtil.JdbcOperations.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(Mockito.any())).thenReturn(mockJdbcOperations);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        extension.killQuery(mockConnection, connectionId);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(mockJdbcOperations, Mockito.times(1)).execute(argumentCaptor.capture());
        Assert.assertEquals("KILL QUERY " + connectionId, argumentCaptor.getValue());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        Mockito.when(mockJdbcOperations.queryForObject(Mockito.any(), Mockito.eq(String.class))).thenReturn(null);
        extension.switchSchema(mockConnection, schemaName);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(mockJdbcOperations, Mockito.times(1)).update(argumentCaptor.capture());
        Assert.assertTrue(argumentCaptor.getValue().contains("ALTER SESSION SET CURRENT_SCHEMA="));
        Assert.assertTrue(argumentCaptor.getValue().contains(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaName = "test";
        Mockito.when(mockJdbcOperations.queryForObject(Mockito.any(), Mockito.eq(String.class))).thenReturn(schemaName);
        String currentSchema = extension.getCurrentSchema(mockConnection);
        Assert.assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String sessionId = "123";
        Mockito.when(mockJdbcOperations.queryForObject(Mockito.any(), Mockito.eq(String.class))).thenReturn(sessionId);
        String connectionId = extension.getConnectionId(mockConnection);
        Assert.assertEquals(sessionId, connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        String sessionId = "123";
        Mockito.when(mockJdbcOperations.queryForObject(Mockito.any(), Mockito.eq(String.class))).thenThrow(new RuntimeException());
        Mockito.when(mockOceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(sessionId));
        String connectionId = extension.getConnectionId(mockOceanBaseConnection);
        Assert.assertEquals(sessionId, connectionId);
    }
}
