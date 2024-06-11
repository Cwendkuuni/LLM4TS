package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Assert;
import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        this.obOracleSessionExtension.killQuery(mockConnection, "sampleConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        this.obOracleSessionExtension.switchSchema(mockConnection, "sampleSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        String schema = this.obOracleSessionExtension.getCurrentSchema(mockConnection);
        Assert.assertNotNull(schema);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = this.obOracleSessionExtension.getConnectionId(mockConnection);
        Assert.assertNotNull(connectionId);
    }

    // Edge case and Exception Handling
    @Test(expected = SQLException.class)
    public void testInvalidSwitchSchema() throws SQLException {
        Mockito.doThrow(new SQLException()).when(mockConnection).createStatement();
        this.obOracleSessionExtension.switchSchema(mockConnection, "sampleSchema");
    }

    @Test(expected = SQLException.class)
    public void testInvalidGetCurrentSchema() {
        Mockito.doThrow(new SQLException()).when(mockConnection).createStatement();
        this.obOracleSessionExtension.getCurrentSchema(mockConnection);
    }

    @Test
    public void testNullConnectionId() {
        Assert.assertNull(this.obOracleSessionExtension.getConnectionId(null));
    }

}