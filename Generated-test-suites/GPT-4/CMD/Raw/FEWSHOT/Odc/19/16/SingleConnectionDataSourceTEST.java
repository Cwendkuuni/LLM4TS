
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.odc.common.event.EventPublisher;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {

    @Test
    public void testConnectionHandling() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource(true);
        ds.setUsername("test");
        ds.setPassword("test");
        
        Connection conn1 = ds.getConnection();
        assertNotNull(conn1);

        Connection conn2 = ds.getConnection();
        assertNotSame(conn1, conn2);
        
        ds.resetConnection();
        Connection conn3 = ds.getConnection();
        assertNotSame(conn1, conn3);
        assertNotSame(conn2, conn3);
    }

    @Test(expected=SQLException.class)
    public void testInvalidUsernamePassword() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.setUsername("test");
        ds.setPassword("test");
        ds.getConnection("invalidUser", "invalidPass");
    }

    @Test
    public void testAutoCommitHandling() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        assertNull(ds.getAutoCommit());

        ds.setAutoCommit(true);
        assertTrue(ds.getAutoCommit());

        ds.setAutoCommit(false);
        assertFalse(ds.getAutoCommit());
    }

    @Test
    public void testEventPublisher() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.setUsername("test");
        ds.setPassword("test");
        
        Connection initialConnection = ds.getConnection();
        assertNotNull(initialConnection);

        EventPublisher mockEventPublisher = Mockito.mock(EventPublisher.class);
        ds.setEventPublisher(mockEventPublisher);

        ds.resetConnection(); 
        Mockito.verify(mockEventPublisher).publishEvent(Mockito.any());
    }

    @Test
    public void testInitializerFunctionality() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ConnectionInitializer mockInitializer = Mockito.mock(ConnectionInitializer.class);
        
        ds.addInitializer(mockInitializer);
        ds.setUsername("username");
        ds.setPassword("password");
        Connection conn = ds.getConnection();

        Mockito.verify(mockInitializer).init(Mockito.any());
    }
}
