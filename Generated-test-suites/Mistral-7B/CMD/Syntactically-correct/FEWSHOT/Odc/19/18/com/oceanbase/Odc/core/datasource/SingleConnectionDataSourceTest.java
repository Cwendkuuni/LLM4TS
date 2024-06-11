package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ODCDataSourceFactory;

/**
 * Tests for {@link SingleConnectionDataSource} class.
 *
 * @author yh263208
 * @date 2023-03-27 10:32
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connectionMock;

    @Mock
    private EventPublisher eventPublisherMock;

    private List<ConnectionInitializer> initializerList;

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        initializerList = new ArrayList<>();
        singleConnectionDataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return connectionMock;
            }
        };
        singleConnectionDataSource.setEventPublisher(eventPublisherMock);
    }

    @After
    public void tearDown() throws Exception {
        singleConnectionDataSource.close();
    }

    @Test
    public void testGetConnection() throws Exception {
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(anyInt())).thenReturn(true);

        singleConnectionDataSource.addInitializer(mock(ConnectionInitializer.class));

        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertEquals(connectionMock, connection);

        verify(connectionMock).createStatement();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        singleConnectionDataSource.setUsername(username);
        singleConnectionDataSource.setPassword(password);

        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(anyInt())).thenReturn(false);

        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }

        verify(connectionMock, never()).createStatement();
    }

    @Test
    public void testGetConnectionClosed() throws Exception {
        when(connectionMock.isClosed()).thenReturn(true);

        singleConnectionDataSource.addInitializer(mock(ConnectionInitializer.class));

        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals("Connection was closed or not valid", e.getMessage());
        }

        verify(connectionMock, never()).createStatement();
    }

    @Test
    public void testGetConnectionAutoReconnect() throws Exception {
        when(connectionMock.isClosed()).thenReturn(true);
        singleConnectionDataSource.setAutoReconnect(true);

        singleConnectionDataSource.addInitializer(mock(ConnectionInitializer.class));

        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        verify(connectionMock).createStatement();
    }

    @Test
    public void testResetConnection() throws Exception {
        singleConnectionDataSource.addInitializer(mock(ConnectionInitializer.class));

        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(anyInt())).thenReturn(false);
        singleConnectionDataSource.setConnection(connectionMock);

        singleConnectionDataSource.resetConnection();

        Connection resetConnection = singleConnectionDataSource.getConnection();
        assertNotNull(resetConnection);
        assertEquals(connectionMock, resetConnection);

        verify(eventPublisherMock).publishEvent(argThat(e -> e instanceof ConnectionResetEvent));
    }

    @Test
    public void testClose() throws Exception {
        singleConnectionDataSource.addInitializer(mock(ConnectionInitializer.class));
        singleConnectionDataSource.setConnection(connectionMock);

        singleConnectionDataSource.close();

        verify(connectionMock).close();
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(mock(ConnectionInitializer.class));

        List<ConnectionInitializer> initializers = singleConnectionDataSource.getInitializerList();
        assertEquals(1, initializers.size());
    }
}