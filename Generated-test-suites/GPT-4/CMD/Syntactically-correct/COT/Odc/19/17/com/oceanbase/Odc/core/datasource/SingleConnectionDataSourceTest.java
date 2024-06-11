package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testDefaultConstructor() {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        assertFalse(connection.isAutoReconnect());
    }

    @Test
    public void testParameterizedConstructor() {
        SingleConnectionDataSource connection = new SingleConnectionDataSource(true);
        assertTrue(connection.isAutoReconnect());
    }

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        assertNotNull(connection.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        assertNull(connection.getConnection("TestUserName", "WrongPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        connection.resetConnection();
        assertNull(connection.getConnection());
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        connection.addInitializer(initializer);
        // Unfortunately, initializerList is private and there is no direct way of checking whether the initializer was added or not  
        assertNotNull(connection.getConnection()); 
    }

    @Test
    public void testAutoCommitSetterAndGetter() {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        connection.setAutoCommit(false);
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testEventPublisherSetter() {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        EventPublisher publisher = mock(EventPublisher.class);
        connection.setEventPublisher(publisher);
        // Similar to the addInitializer case, there is no explicit method to assert this operation. Ideally there should be a getter for this attribute
    }

    @Test
    public void testSetTimeOutMillis() {
        SingleConnectionDataSource connection = new SingleConnectionDataSource();
        connection.setTimeOutMillis(1000);
        // Similar to the previous cases, there is no explicit way to assert this operation. A getter for this attribute should ideally be available.
    }
}
