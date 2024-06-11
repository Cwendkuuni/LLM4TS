package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
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
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private Connection connectionMock;

    @Mock
    private ConnectionInitializer connectionInitializerMock;

    @InjectMocks
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connectionMock.isValid(anyLong())).thenReturn(true);
    }

    @After
    public void tearDown() throws Exception {
        DriverManager.getConnection("jdbc:mock:db"); // Release the driver connection
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connectionMock);
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        verify(singleConnectionDataSource).prepareConnection(connectionMock);
        verifyNoMoreInteractions(singleConnectionDataSource);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNullConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(null);
        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(false);
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connectionMock);
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithEventPublisher() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connectionMock);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        singleConnectionDataSource.getConnection();
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithLock() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        initializerList.add(connectionInitializer);
        singleConnectionDataSource.setInitializerList(initializerList);
        singleConnectionDataSource.setTimeOutMillis(1);

        when(connectionMock.isValid(anyLong())).thenReturn(true);
        when(connectionMock.getAutoCommit()).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.getMetaData()).thenReturn(mock(java.sql.DatabaseMetaData.class));
        when(connectionMock.getConnection()).thenReturn(connectionMock);
        doNothing().when(connectionMock).setAutoCommit(anyBoolean());

        when(connectionInitializer.init(connectionMock)).thenThrow(new SQLException("Initialization failed"));

        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.setAutoReconnect(true);

        try {
            singleConnectionDataSource.getConnection();
            fail();
        } catch (ConflictException e) {
            // Expected
        }

        verify(connectionMock).setAutoCommit(true);
        verifyNoMoreInteractions(connectionMock);
    }

    @Test
    public void testClose() throws Exception {
        singleConnectionDataSource.close();
        verify(connectionMock).close();
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        verify(singleConnectionDataSource).getInitializerList().add(connectionInitializer);
    }
}