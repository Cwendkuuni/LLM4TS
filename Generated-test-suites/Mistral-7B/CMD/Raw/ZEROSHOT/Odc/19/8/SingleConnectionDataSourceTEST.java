package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setup() {
        this.dataSource = new SingleConnectionDataSource();
        this.connectionMock = mock(Connection.class);
        this.eventPublisherMock = mock(EventPublisher.class);
        this.dataSource.setEventPublisher(this.eventPublisherMock);
    }

    @After
    public void tearDown() throws SQLException {
        this.dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        this.dataSource.addInitializer(mock(ConnectionInitializer.class));
        when(this.connectionMock.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(true);
        when(this.connectionMock.isClosed()).thenReturn(false);

        Connection connection = this.dataSource.getConnection();
        assertEquals(this.connectionMock, connection);
        verify(this.eventPublisherMock).publishEvent(eq(new ConnectionResetEvent(this.connectionMock)));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        this.dataSource.addInitializer(mock(ConnectionInitializer.class));
        when(this.connectionMock.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(false);

        this.dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithClosedConnection() throws SQLException {
        this.dataSource.addInitializer(mock(ConnectionInitializer.class));
        when(this.connectionMock.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(true);
        when(this.connectionMock.isClosed()).thenReturn(true);

        this.dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        this.dataSource.addInitializer(mock(ConnectionInitializer.class));
        this.dataSource.getConnection();
        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
        verify(this.eventPublisherMock).publishEvent(new ConnectionResetEvent(null));
    }

    @Test
    public void testAddInitializer() {
        this.dataSource.addInitializer(mock(ConnectionInitializer.class));
        List<ConnectionInitializer> initializerList = this.dataSource.getInitializerList();
        assertEquals(1, initializerList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullInitializer() {
        this.dataSource.addInitializer(null);
    }
}