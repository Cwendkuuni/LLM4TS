
package com.oceanbase.odc.core.datasource;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.mockito.*;
import java.sql.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.locks.Lock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private Lock mockLock;

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.lock = mockLock;
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(singleConnectionDataSource.getLoginTimeout())).thenReturn(true);

        Connection result = singleConnectionDataSource.getConnection();
        assertNotNull(result);
        verify(mockConnection, times(1)).isClosed();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WhenClosed() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WithInvalidUsernamePassword() throws SQLException {
        singleConnectionDataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        verify(mockConnection, times(1)).close();
        assertNull(singleConnectionDataSource.connection);
        assertNull(singleConnectionDataSource.lock);
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        when(mockConnection.getAutoCommit()).thenReturn(false);

        singleConnectionDataSource.prepareConnection(mockConnection);
        verify(mockConnection, times(1)).setAutoCommit(anyBoolean());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(initializer);
        assertTrue(singleConnectionDataSource.initializerList.contains(initializer));
    }

    @Test
    public void testOnConnectionCreated() {
        singleConnectionDataSource.onConnectionCreated(mockConnection);
        // Method does not have any side effects or return. Could only be verified through debug log which cannot be verified through unit test.
    }
    
    @Test
    public void testOnConnectionReset() {
        EventPublisher mockPublisher = mock(EventPublisher.class);
        singleConnectionDataSource.setEventPublisher(mockPublisher);
        singleConnectionDataSource.onConnectionReset(mockConnection);
        verify(mockPublisher, times(1)).publishEvent(any());
    }
}
