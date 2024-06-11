
package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private EventPublisher mockEventPublisher;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @After
    public void tearDown() {
        singleConnectionDataSource.close();
    }

    @Test
    public void testGetConnectionWhenNull() throws SQLException {
        assertNull(singleConnectionDataSource.connection);
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertNotNull(singleConnectionDataSource.connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(mockInitializer);
        assertEquals(1, singleConnectionDataSource.initializerList.size());
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.setEventPublisher(mockEventPublisher);
        assertNotNull(singleConnectionDataSource.connection);
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.connection);
        verify(mockEventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testAutoCommitSetting() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.getAutoCommit());
        singleConnectionDataSource.setAutoCommit(false);
        assertTrue(connection.getAutoCommit());
        singleConnectionDataSource.resetConnection();
        connection = singleConnectionDataSource.getConnection();
        assertFalse(connection.getAutoCommit());
    }
}

