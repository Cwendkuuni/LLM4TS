
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtensions;
    private Connection connection;

    @Before
    public void setUp() {
        obOracleSessionExtensions = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test(expected = NullPointerException.class)
    public void killQuery_NullConnection() {
         obOracleSessionExtensions.killQuery(null, "xyz");
    }

    @Test(expected = NullPointerException.class)
    public void killQuery_NullConnectionId() {
         obOracleSessionExtensions.killQuery(connection, null);
    }

    @Test(expected = NullPointerException.class)
    public void switchSchema_NullConnection() throws SQLException {
         obOracleSessionExtensions.switchSchema(null, "mySchema");
    }

    @Test(expected = SQLException.class)
    public void switchSchema_WrongConnection() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException());
        obOracleSessionExtensions.switchSchema(connection, "mySchema");
    }

    @Test(expected = NullPointerException.class)
    public void switchSchema_NullSchemaName() throws SQLException {
         obOracleSessionExtensions.switchSchema(connection, null);
    }

    @Test
    public void getCurrentSchema_NullConnection() {
         String actualSchema = obOracleSessionExtensions.getCurrentSchema(null);
         assertNull(actualSchema);
    }

    @Test
    public void getConnectionId_NullConnection() {
         String actualId = obOracleSessionExtensions.getConnectionId(null);
         assertNull(actualId);
    }
}
