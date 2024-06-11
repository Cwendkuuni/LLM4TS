package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection connection;

    @Before
    public void setUp() {
        extension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        //Provide mock setup for JdbcOperationsUtil methods
    }

    @Test
    public void testKillQuery() {
        //Positive test
        extension.killQuery(connection, "1234");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullId() {
        //Exception test
        extension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        //Positive test
        extension.switchSchema(connection, "schema");
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullName() throws SQLException {
        //Exception test
        extension.switchSchema(connection, null);
    }

    @Test
    public void testGetCurrentSchema() {
        //Positive test
        String schema = extension.getCurrentSchema(connection);
        assertNotEquals("", schema);
    }

    @Test
    public void testGetConnectionId() {
        //Positive test
        String connectionId = extension.getConnectionId(connection);
        assertNotEquals(0, connectionId);
    }
}