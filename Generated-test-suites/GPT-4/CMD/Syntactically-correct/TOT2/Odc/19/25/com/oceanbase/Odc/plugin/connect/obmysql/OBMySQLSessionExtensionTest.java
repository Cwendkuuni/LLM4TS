package com.oceanbase.odc.plugin.connect.obmysql;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mock;

public class OBMySQLSessionExtensionTest {
    @Mock 
    Connection conn;
    
    OBMySQLSessionExtension testObj = new OBMySQLSessionExtension();

    @Test
    public void killQueryBasicTest() {
        testObj.killQuery(conn, "123");
    }

    @Test(expected = SQLException.class)
    public void switchSchemaTestExpectException() throws SQLException {
        testObj.switchSchema(conn, "");
    }

    @Test
    public void getCurrentSchemaBasicTest() {
        String schema = testObj.getCurrentSchema(conn);
        assertNotNull(schema);
    }

    @Test
    public void getConnectionIdBasicTest() {
        String connectionId = testObj.getConnectionId(conn);
        assertNotNull(connectionId);
    }

    //ToDo: More tests could be added to handle edge case scenarios

}