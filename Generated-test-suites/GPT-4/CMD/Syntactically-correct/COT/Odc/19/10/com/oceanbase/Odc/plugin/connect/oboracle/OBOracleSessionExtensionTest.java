package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension obo = new OBOracleSessionExtension();
    Connection connection = mock(Connection.class);

    @Test
    public void testKillQuery() {
        obo.killQuery(connection, "123");
        // Here we should be checking if the query has been killed successfully
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obo.switchSchema(connection, "newSchema");
        // Here we should be checking if the schema has been switched successfully
    }

    @Test
    public void testGetCurrentSchema() {
        String schema = obo.getCurrentSchema(connection);
        assertEquals("schema", schema);
    }

    @Test
    public void testGetConnectionId() {
        String id = obo.getConnectionId(connection);
        assertEquals("123", id);
    }

    // Now we test identified edge cases and exceptions
    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNull(){
        obo.killQuery(connection, null);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidName() throws SQLException {
        obo.switchSchema(connection, ""); // empty schema name
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchemaWithInvalidConnection() {
        obo.getCurrentSchema(null); // connection is null
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        Connection obConn = mock(OceanBaseConnection.class);
        String id = obo.getConnectionId(obConn);
        assertEquals("connId", id); // test functionality when an exception is thrown.
    }
}
