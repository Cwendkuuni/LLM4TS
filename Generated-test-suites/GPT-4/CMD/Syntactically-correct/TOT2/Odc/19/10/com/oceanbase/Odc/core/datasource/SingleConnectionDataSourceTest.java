package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource instance;

    @Before
    public void setUp() {
        this.instance = new SingleConnectionDataSource();
    }

    @Test
    public void testAutoCommit() {
        instance.setAutoCommit(true);
        assertEquals(instance.getAutoCommit(), (Boolean) true);
    }

    @Test
    public void testAutoReconnect() {
        Boolean autoReconnect = this.instance.getAutoReconnect();
        assertEquals(autoReconnect, (Boolean) false);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection() throws SQLException {
        instance.getConnection();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection mockConn = mock(Connection.class);
        this.instance.getConnection(anyString(), anyString()); // Should throw exception as no connection exists.
    }

    @Test
    public void testResetConnection() throws SQLException {
        this.instance.resetConnection();
    }

    @Test
    public void testClose() {
        this.instance.close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        this.instance.addInitializer(mockInitializer);
    }

    @Test
    public void testSetAutoCommit() {
        this.instance.setAutoCommit(true);
        assertEquals(this.instance.getAutoCommit(), (Boolean) true);
    }

    @Test
    public void testSetTimeOutMillis() {
        this.instance.setTimeOutMillis(5000);
        assertEquals(this.instance.getTimeOutMillis(), (long) 5000);
    }

    @Test
    public void testEventPublisher() {
        EventPublisher mockEventPublisher = mock(EventPublisher.class);
        this.instance.setEventPublisher(mockEventPublisher);
    }
}