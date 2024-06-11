package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.DataSourceTestUtils;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.event.EventPublisherTestUtils;

/**
 * Test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @date 2023-03-21 16:00
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private EventPublisher eventPublisher;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        this.eventPublisher = mock(EventPublisher.class);
        this.dataSource = new SingleConnectionDataSource(true, this.eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = innerCreateConnection();
        assertSame(connection, dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        String invalidUsername = "invalid_username";
        String invalidPassword = "invalid_password";
        assertThrows(SQLException.class, () -> dataSource.getConnection(invalidUsername, invalidPassword));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = innerCreateConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
        verify(this.eventPublisher).publishEvent(ArgumentCaptor.capture(ConnectionResetEvent.class));
        ConnectionResetEvent event = ArgumentCaptor.getLastCaptor().getValue();
        assertSame(connection, event.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(connectionInitializer);
        assertTrue(dataSource.initializerList.contains(connectionInitializer));
    }

    @Test
    public void testClose() {
        Connection connection = innerCreateConnection();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Boolean autoCommit = true;
        Connection connection = innerCreateConnection();
        dataSource.prepareConnection(connection);
        assertTrue(connection.isAutoCommit());
        dataSource.setAutoCommit(false);
        dataSource.prepareConnection(connection);
        assertFalse(connection.isAutoCommit());
    }

    @Test(expected = ConflictException.class)
    public void testConnectionOccupied() throws SQLException, InterruptedException {
        Long lockTimeOutMillis = 1L;
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.lock = new ReentrantLock();
        dataSource.tryLock();
        throw new InterruptedException();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = innerCreateConnection();
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = innerCreateConnection();
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        ConnectionInitializer initializer = new ConnectionInitializerImpl();
        initializerList.add(initializer);
        dataSource.initializerList = initializerList;
        dataSource.initConnection(connection);
        verify(initializer).init(connection);
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = innerCreateConnection();
        dataSource.onConnectionReset(connection);
        verify(this.eventPublisher).publishEvent(ArgumentCaptor.capture(ConnectionResetEvent.class));
        ConnectionResetEvent event = ArgumentCaptor.getLastCaptor().getValue();
        assertSame(connection, event.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = innerCreateConnection();
        assertNull(dataSource.getConnection());
        dataSource.connection = connection;
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
        dataSource.onConnectionCreated(connection);
    }

    private Connection innerCreateConnection() throws SQLException {
        return DataSourceTestUtils.newConnectionFromDriver(DataSourceTestUtils.getUsername(), DataSourceTestUtils.getPassword());
    }

}