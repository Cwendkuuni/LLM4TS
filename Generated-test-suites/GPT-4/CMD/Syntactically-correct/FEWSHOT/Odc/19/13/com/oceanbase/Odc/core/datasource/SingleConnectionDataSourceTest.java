
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;
    
    @Mock
    private Connection connection;
    
    @Captor
    private ArgumentCaptor<ConnectionResetEvent> eventCaptor;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
    }

    @After
    public void teardown() {
        dataSource.close();
    }

    @Test
    public void testCreateDataSource() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testSetAndGetTimeout() {
        long timeout = 5000;
        dataSource.setTimeOutMillis(timeout);
        assertEquals(timeout, dataSource.getTimeOutMillis());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        assertThrows(SQLException.class, () -> 
            dataSource.getConnection("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testGetConnectionValidCredentials() throws SQLException {
        //Assuming set username and password are 'testUser' and 'testPass' respectively.
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPass");
        final Connection connection = dataSource.getConnection("testUser", "testPass");
        assertNotNull(connection);
    }

    @Test
    public void testFireEventOnReset() throws SQLException {
        //Mock an EventPublisher
        EventPublisher publisher = spy(EventPublisher.class);
        dataSource.setEventPublisher(publisher);
        dataSource.resetConnection();

        //Verify that an event was triggered on reset
        verify(publisher).publishEvent(eventCaptor.capture());
        assertNotNull(eventCaptor.getValue());
    }
}
