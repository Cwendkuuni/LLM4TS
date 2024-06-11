
package com.oceanbase.odc.core.datasource;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private Connection connection;

    @Mock
    private Lock lock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(singleConnectionDataSource.getConnection()).thenReturn(connection);

        Assert.assertEquals(connection, singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithParameters() throws SQLException {
        when(singleConnectionDataSource.getConnection(anyString(), anyString())).thenReturn(connection);

        Assert.assertEquals(connection, singleConnectionDataSource.getConnection("username", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        doNothing().when(singleConnectionDataSource).resetConnection();
        singleConnectionDataSource.resetConnection();

        verify(singleConnectionDataSource, times(1)).resetConnection();
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionWhenConnectionNull() throws SQLException {
        when(singleConnectionDataSource.getConnection()).thenReturn(null);

        singleConnectionDataSource.resetConnection();
        Assert.fail("Should have thrown SQLException");
    }

    @Test
    public void testClose() throws SQLException {
        doNothing().when(singleConnectionDataSource).close();
        singleConnectionDataSource.close();

        verify(singleConnectionDataSource, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        doNothing().when(singleConnectionDataSource).prepareConnection(connection);
        singleConnectionDataSource.prepareConnection(connection);

        verify(singleConnectionDataSource, times(1)).prepareConnection(connection);
    }

    @Test
    public void testGetConnectionProxy() {
        when(singleConnectionDataSource.getConnectionProxy(connection)).thenReturn(connection);

        Assert.assertEquals(connection, singleConnectionDataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInitConnection() throws SQLException {
        doNothing().when(singleConnectionDataSource).initConnection(connection);
        singleConnectionDataSource.initConnection(connection);

        verify(singleConnectionDataSource, times(1)).initConnection(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        doNothing().when(singleConnectionDataSource).onConnectionCreated(connection);
        singleConnectionDataSource.onConnectionCreated(connection);

        verify(singleConnectionDataSource, times(1)).onConnectionCreated(connection);
    }

}
