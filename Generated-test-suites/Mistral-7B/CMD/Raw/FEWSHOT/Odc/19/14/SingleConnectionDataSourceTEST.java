package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionWrapper;
import com.oceanbase.odc.core.datasource.ConflictException;
import com.oceanbase.odc.core.datasource.EventPublisherImpl;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;

/**
 * Test cases for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-15 09:53
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    /**
     * Test for {@link SingleConnectionDataSource#SingleConnectionDataSource()}
     */
    @Test
    public void testSingleConnectionDataSource() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        assertEquals(false, dataSource.isAutoReconnect());
        assertNull(dataSource.getEventPublisher());
        assertNull(dataSource.getConnection());
        assertFalse(dataSource.tryLock());
    }

    /**
     * Test for {@link SingleConnectionDataSource#SingleConnectionDataSource(boolean)}
     */
    @Test
    public void testSingleConnectionDataSourceWithAutoReconnect() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        assertNotNull(dataSource);
        assertTrue(dataSource.isAutoReconnect());
        assertNull(dataSource.getEventPublisher());
        assertNull(dataSource.getConnection());
        assertFalse(dataSource.tryLock());
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnection()}
     * @throws SQLException
     */
    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        when(connection.isValid(anyLong())).thenReturn(false);
        when(connection.isClosed()).thenReturn(true);
        doThrow(new SQLException("Exception")).when(dataSource).innerCreateConnection();
        assertThrows(SQLException.class, () -> dataSource.getConnection());

        dataSource.resetConnection();

        Connection connectionFromDataSource = dataSource.getConnection();
        assertEquals(connection, connectionFromDataSource);
        verify(connection).isValid(anyLong());
        verify(connection).isClosed();
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnection(String, String)}
     * @throws SQLException
     */
    @Test
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        when(dataSource.getUsername()).thenReturn("testUser");
        when(dataSource.getPassword()).thenReturn("testPassword");

        Connection connection = mock(Connection.class);
        when(connection.isValid(anyLong())).thenReturn(true);

        assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUser", "wrongPassword"));
    }

    /**
     * Test for {@link SingleConnectionDataSource#resetConnection()}
     * @throws SQLException
     */
    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        when(connection.isValid(anyLong())).thenReturn(false);

        dataSource.setConnection(connection);
        dataSource.setLock(mock(Lock.class));

        when(dataSource.innerCreateConnection()).thenReturn(connection);

        dataSource.resetConnection();

        Connection connectionFromDataSource = dataSource.getConnection();
        assertEquals(connection, connectionFromDataSource);
        verify(dataSource.getLock()).unlock();
        verify(dataSource).close();
        verify(dataSource).onConnectionReset(connection);
    }

    /**
     * Test for {@link SingleConnectionDataSource#addInitializer(ConnectionInitializer)}
     */
    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializer);

        assertEquals(1, dataSource.getInitializerList().size());
        verify(initializer).init(eq(null));
    }

    /**
     * Test for {@link SingleConnectionDataSource#close()}
     */
    @Test
    public void testClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);

        dataSource.close();

        verify(connection).close();
    }

    /**
     * Test for {@link SingleConnectionDataSource#prepareConnection(Connection)}
     */
    @Test
    public void testPrepareConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        Boolean autoCommit = true;

        dataSource.setAutoCommit(autoCommit);
        dataSource.prepareConnection(connection);

        verify(connection).setAutoCommit(eq(autoCommit));
    }

    /**
     * Test for {@link SingleConnectionDataSource#tryLock()}
     */
    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Lock lock = mock(Lock.class);
        when(lock.tryLock(anyLong(), anyInt())).thenReturn(true);

        dataSource.setLock(lock);

        assertTrue(dataSource.tryLock());
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnectionProxy(Connection)}
     */
    @Test
    public void testGetConnectionProxy() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        Lock lock = mock(Lock.class);

        when(dataSource.tryLock()).thenReturn(true);
        when(dataSource.innerCreateConnection()).thenReturn(connection);

        Connection connectionProxy = dataSource.getConnectionProxy(connection);

        assertSame(connectionProxy, connection);
        verify(connectionProxy, eq(ConnectionWrapper.class)).getConnection();
        verify(connectionProxy, eq(CloseIgnoreInvocationHandler.class)).getLock();
    }

    /**
     * Test for {@link SingleConnectionDataSource#initConnection(Connection)}
     * @throws SQLException
     */
    @Test
    public void testInitConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        initializerList.add(initializer);

        dataSource.setInitializerList(initializerList);

        doCallRealMethod().when(dataSource).initConnection(connection);

        dataSource.initConnection(connection);

        verify(initializer).init(connection);
    }

    /**
     * Test for {@link SingleConnectionDataSource#onConnectionCreated(Connection)}
     */
    @Test
    public void testOnConnectionCreated() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);

        dataSource.onConnectionCreated(connection);

        verify(dataSource).log(anyString(), any(Throwable.class));
    }

    /**
     * Test for {@link SingleConnectionDataSource#onConnectionReset(Connection)}
     */
    @Test
    public void testOnConnectionReset() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        EventPublisher eventPublisher = new EventPublisherImpl();
        Connection connection = mock(Connection.class);

        dataSource.setEventPublisher(eventPublisher);

        doThrow(new SQLException("Exception"))
                .when(dataSource)
                .innerCreateConnection();

        doNothing().when(eventPublisher).publishEvent(any(ConnectionResetEvent.class));

        dataSource.resetConnection();

        verify(dataSource).publishEvent(any(ConnectionResetEvent.class));
    }

    /**
     * Test for {@link SingleConnectionDataSource#innerCreateConnection()}
     * @throws SQLException
     */
    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);

        when(dataSource.newConnectionFromDriver(anyString(), anyString()))
                .thenReturn(connection);

        Connection connectionFromDataSource = dataSource.innerCreateConnection();

        assertEquals(connection, connectionFromDataSource);
    }
}