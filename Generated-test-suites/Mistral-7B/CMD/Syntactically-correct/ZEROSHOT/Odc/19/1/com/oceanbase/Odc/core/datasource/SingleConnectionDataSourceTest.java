package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for SingleConnectionDataSource.
 *
 * @author your_name
 */
public class SingleConnectionDataSourceTest {

    private static final String DATABASE_URL = "jdbc:mock:database";
    private static final String USERNAME = "test_user";
    private static final String PASSWORD = "test_password";

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisherMock;
    private List<SingleConnectionDataSource.ConnectionInitializer> initializerList;

    @Before
    public void setUp() throws Exception {
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        dataSource = new SingleConnectionDataSource(true) {
            @Override
            protected Connection innerCreateConnection() throws SQLException {
                connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                return connection;
            }

            @Override
            protected void initConnection(Connection connection) throws SQLException {
                super.initConnection(connection);
                initializerList = new LinkedList<>();
            }

            @Override
            public void addInitializer(SingleConnectionDataSource.ConnectionInitializer connectionInitializer) {
                initializerList.add(connectionInitializer);
            }
        };
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        when(eventPublisherMock.publishEvent(any(ConnectionResetEvent.class))).thenReturn(true);

        Connection connectionFromDataSource = dataSource.getConnection();
        assertNotNull(connectionFromDataSource);
        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));

        Connection connectionFromDataSourceAgain = dataSource.getConnection();
        assertSame(connectionFromDataSource, connectionFromDataSourceAgain);

        reset(eventPublisherMock);

        Connection connectionFromDataSourceThird = dataSource.getConnection();
        assertSame(connectionFromDataSource, connectionFromDataSourceThird);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws Exception {
        when(eventPublisherMock.publishEvent(any(ConnectionResetEvent.class))).thenReturn(true);

        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");

        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws Exception {
        when(eventPublisherMock.publishEvent(any(ConnectionResetEvent.class))).thenReturn(true);

        Connection connectionFromDataSource = dataSource.getConnection();
        assertNotNull(connectionFromDataSource);

        dataSource.resetConnection();

        Connection connectionFromDataSourceAfterReset = dataSource.getConnection();
        assertNotNull(connectionFromDataSourceAfterReset);
        assertFalse(connectionFromDataSource.isOpen());
        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithTimeout() throws Exception {
        dataSource.setTimeOutMillis(0);

        dataSource.getConnection();
        dataSource.getConnection();

        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConflictException("", new Object[]{}, "Connection is occupied");
        }
    }

    @Test
    public void testAddInitializer() throws Exception {
        SingleConnectionDataSource.ConnectionInitializer initializerMock = mock(SingleConnectionDataSource.ConnectionInitializer.class);

        dataSource.addInitializer(initializerMock);

        verify(initializerMock, times(1)).init(connection);
    }
}