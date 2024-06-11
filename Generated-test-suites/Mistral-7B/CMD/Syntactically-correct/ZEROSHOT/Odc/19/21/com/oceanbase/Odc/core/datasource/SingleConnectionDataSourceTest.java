package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

/**
 * Test class for SingleConnectionDataSource.
 *
 * @author yh263208
 * @date 2021-11-08 16:20
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    @InjectMocks
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private Connection connection;
    @Mock
    private ConnectionInitializer connectionInitializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);
        when(connection.isValid(anyLong())).thenReturn(true);

        singleConnectionDataSource.addInitializer(connectionInitializer);

        Connection result = singleConnectionDataSource.getConnection();
        assertEquals(connection, result);

        verify(connectionInitializer).init(connection);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);
        when(connection.isValid(anyLong())).thenReturn(false);

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullUsernameOrPassword() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();

        try {
            singleConnectionDataSource.getConnection("", "");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
            throw e;
        }
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();

        try {
            singleConnectionDataSource.getConnection("testUsername", "testPassword");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        singleConnectionDataSource.resetConnection();
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() throws SQLException {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        verify(connectionInitializer).init(singleConnectionDataSource.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException {
        when(singleConnectionDataSource.tryLock()).thenReturn(false);

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullAutoCommit() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();

        try {
            singleConnectionDataSource.getConnection();
        } catch (SQLException e) {
            assertNull(singleConnectionDataSource.getAutoCommit());
            throw e;
        }
    }

    @Test
    public void testGetConnectionWithAutoCommit() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource(true);

        singleConnectionDataSource.getConnection();
        assertTrue(singleConnectionDataSource.getAutoCommit());
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.close();
        verify(connection).close();
    }
}