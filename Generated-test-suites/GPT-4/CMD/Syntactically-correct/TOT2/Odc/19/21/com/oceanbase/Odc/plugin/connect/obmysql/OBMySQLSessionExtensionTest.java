package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection conn;
    private String connectionId;
    private String schemaName;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        conn = mock(Connection.class);
        connectionId = "1234";
        schemaName = "testSchema";
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(conn, connectionId);
        //Verify if the query KILL QUERY + connectionId was executed, suitable Mocking Framework can be used to test this
    }

    @Test(expected = NullPointerException.class)
    public void killQuery_NullConnection_Test() {
        obMySQLSessionExtension.killQuery(null, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(conn, schemaName);
        //Verify if the correct SQL commands were executed, suitable Mocking Framework can be used to test this
    }

    @Test(expected = NullPointerException.class)
    public void switchSchema_NullConnection_Test() throws SQLException {
        obMySQLSessionExtension.switchSchema(null, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        obMySQLSessionExtension.getCurrentSchema(conn);
        //Verify if the correct SQL query was executed to fetch schema, suitable Mocking Framework can be used to test this
    }

    @Test(expected = NullPointerException.class)
    public void getCurrentSchema_NullConnection_Test() {
        obMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() {
        obMySQLSessionExtension.getConnectionId(conn);
        //Verify if the correct SQL query was executed to fetch connection id, suitable Mocking Framework can be used to test this
    }

    @Test(expected = NullPointerException.class)
    public void getConnectionId_NullConnection_Test() {
        obMySQLSessionExtension.getConnectionId(null);
    }
}