package com.oceanbase.odc.plugin.connect.oboracle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
    public void killQuery() {
        try {
            obOracleSessionExtension.killQuery(mockConnection, "test");
        } catch (Exception e) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    @Test 
    public void switchSchema() {
        try {
            obOracleSessionExtension.switchSchema(mockConnection, "test");
        } catch (SQLException e) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    @Test
    public void getCurrentSchemaTest() {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        Assert.assertNotNull(currentSchema);
    }

    @Test
    public void getConnectionIdTest() {
        String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        Assert.assertNotNull(connectionId);
    }
}