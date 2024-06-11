package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializeException;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionProxy;
import com.oceanbase.odc.core.datasource.ConflictException;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-16 14:31
 * @since ODC_release_3.2.2
 */
@Slf4j
public class SingleConnectionDataSourceTest {
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final long TIMEOUT_MILLIS = 10 * 1000;
    private static final EventPublisher eventPublisherMock = mock(EventPublisher.class);

    @Setter
    @Getter
    private SingleConnectionDataSource testObject;

    @Setter
    @Getter
    private Connection connectionMock;

    @Setter
    @Getter
    private CloseIgnoreInvocationHandler connectionProxyMock;

    @Captor
    private ArgumentCaptor<ConnectionResetEvent> connectionResetEventCaptor;

    @Before
    public void setup() {
        testObject = new SingleConnectionDataSource();
        testObject.setEventPublisher(eventPublisherMock);
        connectionMock = mock(Connection.class);
        connectionProxyMock = new CloseIgnoreInvocationHandler(connectionMock, new ReentrantLock());
    }

    @After
    public void teardown() {
        testObject = null;
        connectionMock = null;
        connectionProxyMock = null;
    }

    @Test
    public void testGetConnection() throws SQLException {
        testObject.setAutoReconnect(true);
        when(connectionMock.isValid(TIMEOUT_MILLIS)).thenReturn(false);

        testObject.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException, ConnectionInitializeException {
                // initialization logic
            }
        });

        Connection result = testObject.getConnection();
        assertFalse(Objects.isNull(result));
        assertSame(connectionMock, result);
        verify(connectionMock).isValid(TIMEOUT_MILLIS);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        testObject.setAutoReconnect(false);
        testObject.setUsername(USERNAME);
        testObject.setPassword(PASSWORD);

        testObject.getConnection();
        fail("Expected SQLException to be thrown.");
    }

    @Test
    public void testResetConnection() throws SQLException {
        testObject.setAutoReconnect(true);
        testObject.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException, ConnectionInitializeException {
                // initialization logic
            }
        });

        testObject.resetConnection();
        testObject.getConnection();

        verify(connectionMock).close();
        verify(eventPublisherMock).publishEvent(connectionResetEventCaptor.capture());
        ConnectionResetEvent event = connectionResetEventCaptor.getValue();
        assertNull(event.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testResetConnectionNotAvailable() throws SQLException {
        testObject.setAutoReconnect(false);
        testObject.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException, ConnectionInitializeException {
                // initialization logic
            }
        });

        testObject.resetConnection();
        testObject.getConnection();

        testObject.resetConnection();
        testObject.getConnection();

        fail("Expected ConflictException to be thrown.");
    }

    @Test
    public void testAddInitializer() {
        testObject.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException, ConnectionInitializeException {
                // initialization logic
            }
        });

        List<ConnectionInitializer> list = testObject.getInitializerList();
        assertTrue(list.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullInitializer() {
        testObject.addInitializer(null);
        fail("Expected NullPointerException to be thrown.");
    }

    @Test
    public void testClose() throws SQLException {
        testObject.getConnection();
        testObject.close();
        testObject.getConnection();

        verify(connectionMock).close();
    }
}