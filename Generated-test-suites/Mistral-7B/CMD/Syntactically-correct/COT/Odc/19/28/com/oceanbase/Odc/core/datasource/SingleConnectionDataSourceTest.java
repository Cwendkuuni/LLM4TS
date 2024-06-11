package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;

/**
 * Test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;
    private List<ConnectionInitializer> initializerList;
    private ReentrantLock lock;

    @Before
    public void setup() {
        this.dataSource = new SingleConnectionDataSource();
        this.connection = mock(Connection.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.connectionInitializer = mock(ConnectionInitializer.class);
        this.initializerList = Arrays.asList(this.connectionInitializer);
        this.lock = new ReentrantLock();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connection);
        Connection result = this.dataSource.getConnection();
        assertSame(this.connection, result);
    }

    @Test
    public void testGetConnection_InvalidCredentials() throws SQLException {
        when(this.dataSource.getUsername()).thenReturn("testUser");
        when(this.dataSource.getPassword()).thenReturn("testPassword");
        assertThrows(SQLException.class, () -> this.dataSource.getConnection("invalidUser", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connection);
        this.dataSource.resetConnection();
        verify(this.eventPublisher).publishEvent(argThat(isA(ConnectionResetEvent.class)));
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        this.dataSource.addInitializer(this.connectionInitializer);
        assertTrue(this.dataSource.getInitializerList().contains(this.connectionInitializer));
    }

    @Test
    public void testClose() {
        try {
            this.dataSource.close();
        } finally {
            assertNull(this.dataSource.getConnection());
        }
    }

    @Test
    public void testCreateConnection() throws SQLException {
        when(this.dataSource.newConnectionFromDriver(this.dataSource.getUsername(), this.dataSource.getPassword()))
                .thenReturn(this.connection);
        this.dataSource.initConnection(this.connection);
        verify(this.connectionInitializer).init(this.connection);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(this.lock.tryLock(this.dataSource.getTimeOutMillis(), TimeUnit.MILLISECONDS)).thenReturn(true);
        when(this.connection.close()).thenThrow(new SQLException());
        this.dataSource.getConnectionProxy(this.connection);
        verify(this.lock).unlock();
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connection);
        this.dataSource.onConnectionCreated(this.connection);
        verify(this.dataSource).log(anyString());
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        ArgumentCaptor<ConnectionResetEvent> captor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        this.dataSource.onConnectionReset(this.connection);
        verify(this.eventPublisher).publishEvent(captor.capture());
        ConnectionResetEvent event = captor.getValue();
        assertSame(this.connection, event.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(this.dataSource.newConnectionFromDriver(this.dataSource.getUsername(), this.dataSource.getPassword()))
                .thenReturn(this.connection);
        Connection result = this.dataSource.innerCreateConnection();
        assertSame(this.connection, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnection_ConnectionNotNULL() throws SQLException {
        this.dataSource.connection = this.connection;
        this.dataSource.innerCreateConnection();
    }

}