package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private Lock lockMock;

    @Before
    public void setUp() throws Exception {
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        lockMock = new ReentrantLock();
        connectionMock = Mockito.mock(Connection.class);
        dataSource = new SingleConnectionDataSource(false) {
            @Override
            public Connection getConnection() throws SQLException {
                return connectionMock;
            }

            @Override
            public void close() throws SQLException {
                // do nothing
            }

            @Override
            public void addInitializer(ConnectionInitializer connectionInitializer) {
                // not used in this test
            }

            @Override
            public EventPublisher getEventPublisher() {
                return eventPublisherMock;
            }

            @Override
            public long getLoginTimeout() {
                return 0;
            }

            @Override
            public String getUsername() {
                return USERNAME;
            }

            @Override
            public String getPassword() {
                return PASSWORD;
            }

            @Override
            public List<ConnectionInitializer> getConnectionInitializers() {
                return null;
            }

            @Override
            public boolean isAutoCommit() {
                return false;
            }

            @Override
            public void setAutoCommit(boolean autoCommit) {
                // not used in this test
            }

            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return connectionMock;
            }

            @Override
            protected void initConnection(Connection connection) throws SQLException {
                // not used in this test
            }

            @Override
            protected void onConnectionCreated(Connection connection) {
                // not used in this test
            }

            @Override
            protected void onConnectionReset(Connection connection) {
                // not used in this test
            }

            @Override
            protected void prepareConnection(Connection connection) throws SQLException {
                // not used in this test
            }

            @Override
            protected void log(String msg) {
                // not used in this test
            }
        };

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((SingleConnectionDataSource) invocation.getTarget()).setAutoCommit(true);
                return null;
            }
        }).when(dataSource).setAutoCommit(true);

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new ConflictException(ErrorCodes.ConnectionOccupied, new Object[] {},
                        "Connection is occupied, waited " + dataSource.getTimeOutMillis() + " millis");
            }
        }).when(dataSource).tryLock();
    }

    @Test
    public void testGetConnection() throws Exception {
        Mockito.when(connectionMock.isValid()).thenReturn(true);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertSame(connectionMock, connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws Exception {
        Mockito.when(connectionMock.isValid()).thenReturn(false);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithAutoReconnectDisabled() throws Exception {
        dataSource.resetConnection();
        Mockito.when(connectionMock.isValid()).thenReturn(false);
        dataSource.getConnection();
    }

    @Test
    public void testSetAutoCommit() throws Exception {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.isAutoCommit());
    }

    @Test
    public void testAddInitializer() throws Exception {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws Exception {
        Mockito.when(connectionMock.isValid()).thenReturn(true);
        Mockito.when(connectionMock.getUserName()).thenReturn(null);
        Mockito.when(connectionMock.getPassword()).thenReturn(null);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullDataSource() throws Exception {
        SingleConnectionDataSource dataSource = null;
        dataSource.getConnection();
    }
}