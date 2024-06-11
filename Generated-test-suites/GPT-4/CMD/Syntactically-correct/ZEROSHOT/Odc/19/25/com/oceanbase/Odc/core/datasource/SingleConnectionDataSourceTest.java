
package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class SingleConnectionDataSourceTest {

    SingleConnectionDataSource dataSource;
    Connection mockConnection;
    ConnectionInitializer mockInitializer;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockInitializer = Mockito.mock(ConnectionInitializer.class);
        dataSource = new SingleConnectionDataSource();
        dataSource.connection = mockConnection;

        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
    }

    @Test
    void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    void testGetConnectionAutoReconnect() throws SQLException {
        dataSource.setAutoReconnect(true);
        dataSource.connection = null;

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    void testGetConnectionAutoReconnectFailed() {
        dataSource.setAutoReconnect(false);
        dataSource.connection = null;

        assertThrows(SQLException.class, () -> {
            dataSource.getConnection();
        });
    }

    @Test
    void testGetConnectionWithUsernameAndPassword() throws SQLException {
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");

        assertThrows(SQLException.class, () -> {
            dataSource.getConnection("wrongUser", "wrongPassword");
        });

        Connection connection = dataSource.getConnection("testUser", "testPassword");

        assertNotNull(connection);
    }

    @Test
    void testAddInitializer() throws SQLException {
        dataSource.addInitializer(mockInitializer);
        verifyZeroInteractions(mockInitializer);
    }

    @Test
    void testClose() throws SQLException {
        dataSource.close();
        verify(mockConnection).close();
    }

    @Test
    void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(mockConnection);

        verify(mockConnection).setAutoCommit(true);
    }

    @Test
    void testInitConnection() throws SQLException {
        dataSource.addInitializer(mockInitializer);
        dataSource.initConnection(mockConnection);

        verify(mockInitializer).init(mockConnection);
    }

    @Test
    void testResetConnection() throws SQLException {
        dataSource.connection = mockConnection;
        EventPublisher mockPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(mockPublisher);

        dataSource.resetConnection();

        assertNull(dataSource.connection);
        assertNull(dataSource.lock);
        verify(mockPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

}
