package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private Connection connectionMock;

    @Mock
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testAutoCommitGetterSetter() {
        singleConnectionDataSource.setAutoCommit(true);
        assertTrue(singleConnectionDataSource.getAutoCommit());
    }

    @Test
    public void testAutoReconnectGetter() {
        assertFalse(singleConnectionDataSource.isAutoReconnect());
    }

    @Test
    public void testEventPublisherGetterSetter() {
        singleConnectionDataSource.setEventPublisher(eventPublisherMock);
        assertSame(eventPublisherMock, singleConnectionDataSource.getEventPublisher());
    }

    @Test
    public void testGetConnection() throws Exception {
        assertNotNull(singleConnectionDataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernamePassword() throws Exception {
        singleConnectionDataSource.getConnection("invalidUsername", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws Exception {
        singleConnectionDataSource.resetConnection();
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
    }

    @Test
    public void testTimeOutMillisGetterSetter() {
        singleConnectionDataSource.setTimeOutMillis(1234L);
        assertEquals(1234L, singleConnectionDataSource.getTimeOutMillis());
    }

    @After
    public void tearDown() {
        singleConnectionDataSource.close();
    }
}
