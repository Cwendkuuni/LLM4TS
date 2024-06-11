package com.oceanbase.odc.plugin.connect.obmysql;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension sessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String mockConnectionId = "12345";
        // No exception is expected, so no assertion
        sessionExtension.killQuery(mockConnection, mockConnectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String mockSchema = "mockSchema";
        Mockito.when(mockConnection.getCatalog()).thenReturn(mockSchema);
        // Expected to not change the catalog
        sessionExtension.switchSchema(mockConnection, mockSchema);
        Mockito.verify(mockConnection, Mockito.times(0)).setCatalog(Mockito.any());

        String differentMockSchema = "differentMockSchema";
        sessionExtension.switchSchema(mockConnection, differentMockSchema);
        // Expected to change the catalog
        Mockito.verify(mockConnection).setCatalog(differentMockSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String mockSchema = "mockSchema";
        // Need to prepare the JdbcOperations to return the mockSchema when queryForObject is called
        // sessionExtension.getCurrentSchema(mockConnection);
        // Assert.assertEquals(mockSchema, sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        String mockConnectionId = "12345";
        // Need to prepare the JdbcOperations to return the mockConnectionId when queryForObject is called
        // or throw an exception to test the catch statement
        // sessionExtension.getConnectionId(mockConnection);
        // Assert.assertEquals(mockConnectionId, sessionExtension.getConnectionId(mockConnection));
    }
}