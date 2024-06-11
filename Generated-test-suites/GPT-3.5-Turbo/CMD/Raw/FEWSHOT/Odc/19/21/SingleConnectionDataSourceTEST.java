package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() {
        try {
            assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetConnectionWithCredentials() {
        try {
            assertNotNull(dataSource.getConnection("username", "password"));
        } catch (SQLException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testResetConnection() {
        try {
            dataSource.resetConnection();
            assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() {
        try {
            dataSource.prepareConnection(mock(Connection.class));
        } catch (SQLException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testInnerCreateConnection() {
        try {
            assertNotNull(dataSource.innerCreateConnection());
        } catch (SQLException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(mock(Connection.class));
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.onConnectionReset(mock(Connection.class));
    }
}