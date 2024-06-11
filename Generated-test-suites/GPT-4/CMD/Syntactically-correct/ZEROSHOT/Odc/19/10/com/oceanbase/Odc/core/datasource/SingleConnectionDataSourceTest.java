package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource(true);
    }

    @After 
    public void tearDown() {
        singleConnectionDataSource = null;
    }

    @Test
    public void testGetConnectionIsNull() throws SQLException {
        Connection conn = singleConnectionDataSource.getConnection();
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUserNamePassword() throws SQLException {
        singleConnectionDataSource.getConnection("invalid_username", "invalid_password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);

        try {
        Field field = singleConnectionDataSource.getClass().getDeclaredField("connection");
        field.setAccessible(true);
        field.set(singleConnectionDataSource, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        singleConnectionDataSource.resetConnection();

        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
        assertTrue(singleConnectionDataSource.getAutoCommit());
    }

    @Test
    public void testAutoCommit() {
        singleConnectionDataSource.setAutoCommit(true);
        assertTrue(singleConnectionDataSource.getAutoCommit());
    }

    @Test
    public void testEventPublisher() {
        EventPublisher publisherMock = mock(EventPublisher.class);
        singleConnectionDataSource.setEventPublisher(publisherMock);
        assertSame(publisherMock, singleConnectionDataSource.eventPublisher);
    }

    @Test
    public void testAddInitializer() {
        int initialListSize = singleConnectionDataSource.initializerList.size();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        singleConnectionDataSource.addInitializer(initializer);
        assertTrue(singleConnectionDataSource.initializerList.size() > initialListSize);
    }

    @Test
    public void testTimeOutMillis() {
        long timeout = 20000;
        singleConnectionDataSource.setTimeOutMillis(timeout);
        assertEquals(timeout, singleConnectionDataSource.timeOutMillis);
    }

    @Test
    public void testGetConnectionFailedWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenThrow(new SQLException());
        
        try {
            Field field = singleConnectionDataSource.getClass().getDeclaredField("connection");
            field.setAccessible(true);
            field.set(singleConnectionDataSource, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
       
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection());
    }
}
