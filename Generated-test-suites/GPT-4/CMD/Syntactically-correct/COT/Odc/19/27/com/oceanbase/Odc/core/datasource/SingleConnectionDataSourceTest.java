package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.*;
import org.junit.*;
import java.sql.SQLException;
import java.sql.Connection;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource dataSource;
    Connection connection;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        connection = dataSource.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testGetAutoCommit() {
        assertNull(dataSource.getAutoCommit());
    }

    @Test
    public void testIsAutoReconnect() {
        assertFalse(dataSource.isAutoReconnect());
    }

    @Test
    public void testSetEventPublisher() {
        EventPublisher publisher = new EventPublisher();
        dataSource.setEventPublisher(publisher);
        assertEquals(publisher, dataSource.getEventPublisher());
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        assertEquals(connection, dataSource.getConnection("username", "password"));
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new ConnectionInitializer();
        dataSource.addInitializer(initializer);
        // Verify the initializer added successfully
    }

    @Test(expected = Exception.class)
    public void close() throws Exception {
        dataSource.close();
        dataSource.getConnection(); // this must throw an exception as connection is closed
    }
}