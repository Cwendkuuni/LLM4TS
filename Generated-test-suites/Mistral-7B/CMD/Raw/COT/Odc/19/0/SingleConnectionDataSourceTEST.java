package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingletonConnectionDataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-22 20:18
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        eventPublisher = Mockito.mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();

        Connection newConnection = dataSource.getConnection();
        assertNotSame(connection, newConnection);

        dataSource.onConnectionReset(connection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test(expected = ConflictException.class)
    public void testCloseWithConnectionLocked() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();

        dataSource.close();
        dataSource.getConnection();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.prepareConnection(connection);
        assertTrue(dataSource.getAutoCommit() == connection.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        dataSource.setTimeOutMillis(200);
        SingleConnectionDataSource dataSource2 = new SingleConnectionDataSource();

        assertTrue(dataSource.tryLock());
        assertFalse(dataSource2.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource() {{
            setLock(new ReentrantLock());
        }};
        Lock lock = dataSource.getLock();

        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        assertEquals(connection, connectionProxy.getConnection());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.initConnection(connection);
        assertTrue(dataSource.getInitializerList().isEmpty());
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.onConnectionReset(connection);
        Mockito.verify(eventPublisher).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

    @Test(expected = IllegalStateException.class)
    public void testConnectionIsNotNull() throws SQLException {
        dataSource.setConnection(Mockito.mock(Connection.class));
        dataSource.getConnection();
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.setConnection(connection);
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testCloseWithConnectionNotClosable() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        doThrow(new SQLException("Connection is not closable")).when(connection).close();
        dataSource.setConnection(connection);
        dataSource.close();
    }

    @Test
    public void testInitConnectionWithInitializers() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        List<ConnectionInitializer> initializerList = new LinkedList<>();
        initializerList.add(Mockito.mock(ConnectionInitializer.class));
        dataSource.setInitializerList(initializerList);
        dataSource.initConnection(connection);

        for (ConnectionInitializer initializer : initializerList) {
            Mockito.verify(initializer).init(connection);
        }

        assertEquals(initializerList.size(), dataSource.getInitializerList().size());
    }
}