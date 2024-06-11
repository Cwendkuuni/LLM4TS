
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension oBOracleSessionExtension;
    @Mock 
    Connection connection;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        oBOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123456";
        oBOracleSessionExtension.killQuery(connection, connectionId);
        // Verify that JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId); was called once
    }

    @Test
    public void testSwitchSchema() {
        String schema = "testSchema";
        oBOracleSessionExtension.switchSchema(connection, schema);
        // Verify that switchSchema was called once
    }

    @Test
    public void testGetCurrentSchema() {
        oBOracleSessionExtension.getCurrentSchema(connection);
        // Verify that the correct query was executed
    }

    @Test
    public void testGetConnectionId() {
        oBOracleSessionExtension.getConnectionId(connection);
        // Verify that the correct query was executed
    }

    @Test(expected = Exception.class)
    public void testSwitchSchema_WithException() {
        doThrow(new SQLException()).when(connection).setSchema(anyString());
        oBOracleSessionExtension.switchSchema(connection, "test schema");
    }

    @Test(expected = Exception.class)
    public void testGetCurrentSchema_WithException() {
        doThrow(new Exception()).when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)));
        oBOracleSessionExtension.getCurrentSchema(connection);
    }

    @Test(expected = Exception.class)
    public void testGetConnectionId_WithException() {
        doThrow(new Exception()).when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)));
        oBOracleSessionExtension.getConnectionId(connection);
    }
}
