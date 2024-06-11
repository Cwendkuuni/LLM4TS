package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    SingleConnectionDataSource connectionDataSource;

    @Before
    public void setup() {
        connectionDataSource = new SingleConnectionDataSource();
    }


    //Testing getConnection() method
    @Test
    public void getConnectionTest() throws SQLException {
        assertNotNull(connectionDataSource.getConnection());
    }
    
    //Testing getConnection(String username, String password) method
    @Test
    public void getConnectionWithUPTest() throws SQLException {
        assertNotNull(connectionDataSource.getConnection("testUser", "testPass"));
    }
    
    //Testing method addInitializer()
	@Test
	public void addInitializerTest() {
		ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);

		// Add initializer, then try to get a connection. Initializer's init method should be called.
		singleConnectionDataSource.addInitializer(mockInitializer);
		assertNotNull(singleConnectionDataSource.getConnection());
		verify(mockInitializer).init(any(Connection.class));
	}

    //Testing resetConnection() when no connection exist
    @Test(expected = SQLException.class)
    public void resetConnectionWhenNoConnectionExist() throws SQLException {
        connectionDataSource.resetConnection();
    }

    //Testing method close() 
    @Test
    public void closeConnectionTest() throws SQLException {
        connectionDataSource.close();
        assertTrue(connectionDataSource.getConnection().isClosed());
    }

    //Constructor test
    @Test
    public void constructorTest(){
        connectionDataSource = new SingleConnectionDataSource(false);
        assertFalse(connectionDataSource.isAutoReconnect());
    }

    //Test for SQLException when getting connection with wrong username & password
    @Test(expected = SQLException.class)
    public void getConnectionSqlExceptionTest() throws SQLException {
        connectionDataSource.getConnection("abc", "xyz");
    }

    //Test SQLExpection when resetConnection is called but no connection to reset
    @Test(expected = SQLException.class)
    public void resetSqlExceptionTest() throws SQLException {
        connectionDataSource.resetConnection();
    }

    //Test IllegalStateException when calling innerCreateConnection but connection is not null
    @Test(expected = IllegalStateException.class)
    public void innerCreateConnectionIllegalStateExceptionTest() throws SQLException {
        connectionDataSource.getConnection(); // this will initialize the connection
        connectionDataSource.innerCreateConnection(); // this should throw an exception
    }
}
