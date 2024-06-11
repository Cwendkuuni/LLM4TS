package com.oceanbase.odc.core.datasource;
import org.junit.*;
import java.sql.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    SingleConnectionDataSource scds;
    Connection mockConnection;

    @Before
    public void setUp() throws Exception {
        scds = new SingleConnectionDataSource();
        mockConnection = mock(Connection.class);
        scds.setConnection(mockConnection); // assume that a setter for connection exists
    }

    // testing getConnection() method when there is no existing connection
    @Test
    public void testGetConnectionNoExisting() throws SQLException {
        when(mockConnection.closed()).thenReturn(true); 
        Connection result = scds.getConnection();
        assertNotNull(result);
        verify(mockConnection).close();
    }

    // testing getConnection() method when there is existing connection
    @Test
    public void testGetConnectionExisting() throws SQLException {
        when(mockConnection.closed()).thenReturn(false); // connection is open
        when(mockConnection.isValid(anyInt())).thenReturn(true); // connection is valid
        Connection result = scds.getConnection();
        assertNotNull(result);
    }

    // testing getConnection() with username and password
    @Test(expected = SQLException.class) 
    public void testGetConnectionInvalidusername() throws SQLException {
        scds.getConnection("invalidusername", "invalidpassword");
    }

    // testing close() when there is connection to close
    @Test
    public void testCloseWithConnection() throws SQLException {
        scds.close();
        verify(mockConnection).close();
    }

    // ...the other test methods continue here...
}