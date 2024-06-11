package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

/**
 * Tests for {@link SingleConnectionDataSource}.
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Captor
    private ArgumentCaptor<ConnectionResetEvent> connectionResetEventCaptor;

    private Connection connection;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        connection = DriverManager.getConnection("jdbc:mock:mydb");
        dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws Exception {
        assertNull(dataSource.getConnection());

        dataSource.addInitializer(mock(ConnectionInitializer.class));

        Connection actual = dataSource.getConnection();
        assertSame(connection, actual);
        verify(eventPublisher).publishEvent(connectionResetEventCaptor.capture());
        ConnectionResetEvent event = connectionResetEventCaptor.getValue();
        assertSame(connection, event.getConnection());
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() throws Exception {
        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithNullEventPublisher() throws Exception {
        dataSource.setEventPublisher(null);
        dataSource.addInitializer(mock(ConnectionInitializer.class));

        dataSource.getConnection();

        assertFalse(dataSource.isInitialized());
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.getConnection();
        dataSource.resetConnection();

        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializerWithNullConnectionInitializer() throws Exception {
        dataSource.addInitializer(null);

        assertFalse(dataSource.isInitialized());
    }

    @Test
    public void testClose() throws Exception {
        dataSource.addInitializer(mock(ConnectionInitializer.class));
        dataSource.getConnection();

        dataSource.close();

        assertNull(dataSource.getConnection());
    }

    @Test
    public void testCloseTwice() throws Exception {
        dataSource.addInitializer(mock(ConnectionInitializer.class));
        dataSource.getConnection();

        dataSource.close();
        dataSource.close();

        assertNull(dataSource.getConnection());
    }

    @Test
    public void testTryLock() throws Exception {
        dataSource.setTimeOutMillis(10);
        dataSource.lock.tryLock(5, TimeUnit.SECONDS);
        assertFalse(dataSource.lock.isHeldByCurrentThread());

        dataSource.lock.tryLock(60, TimeUnit.SECONDS);
        assertTrue(dataSource.lock.isHeldByCurrentThread());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        dataSource.getConnection();

        assertSame(connection, dataSource.getConnectionProxy(connection));
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}