package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection mockConnection;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(true);
    }
    
    @Test
    public void testConnection() {
        assertNotNull(dataSource.getConnection());
    }
    
    @Test
    public void testIsAutoReconnect() {
        assertFalse(dataSource.isAutoReconnect());
    }
    
    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalidUsername", "invalidPassword");
    }
    
    @Test
    public void testResetConnection() {
        assertNotNull(dataSource.resetConnection());
    }
    
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        // Assert that initializerList contains the initializer
    }
    
    @Test
    public void testClose() {
        assertNotNull(dataSource.close());
    }
    
    @Test
    public void testGetSetAutoCommit() {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
    }
    
    @Test
    public void testGetSetEventPublisher() {
        EventPublisher publisher = Mockito.mock(EventPublisher.class);
        dataSource.setEventPublisher(publisher);
        assertEquals(publisher, dataSource.getEventPublisher());
    }
    
    @Test
    public void testSetTimeOutMillis() {
        dataSource.setTimeOutMillis(20000);
        // Assert that the timeOutMillis is set to 20000 (write a getter if required)
    }
    
    @Test(expected = ConflictException.class)
    public void testConnectionProxyOccupied() {
        dataSource.getConnection();
        dataSource.getConnection();
    }
}