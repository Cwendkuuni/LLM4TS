package com.oceanbase.odc.core.datasource;
import com.oceanbase.odc.core.datasource.*;
import com.oceanbase.odc.common.event.*;
import static org.junit.Assert.*;
import org.junit.*;

public class SingleConnectionDataSourceTest {

    // declare an instance of the class to be tested
    SingleConnectionDataSource scds;

    @Before
    public void setUp() {
        scds = new SingleConnectionDataSource();
    }

    @After
    public void tearDown() {
        scds = null;
    }

    @Test
    public void testSetAndGetAutoCommit() {
        Boolean autoCommit = true;
        scds.setAutoCommit(autoCommit);
        assertEquals(autoCommit, scds.getAutoCommit());
    }

    @Test
    public void testGetAutoReconnect() {
        assertFalse(scds.getAutoReconnect());
    }

    @Test
    public void testSetAndGetTimeOutMillis() {
        long timeout = 10000;
        scds.setTimeOutMillis(timeout);
        assertEquals(timeout, scds.getTimeOutMillis());
    }

    @Test
    public void testSetAndGetEventPublisher() {
        EventPublisher ep = new EventPublisher();
        scds.setEventPublisher(ep);
        assertEquals(ep, scds.getEventPublisher());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullConnection() throws SQLException {
        scds.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUserPass() throws SQLException {
        scds.getConnection("invalidUser","invalidPass");
    }  

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        scds.resetConnection();
    }

    @Test
    public void testClose() {
        scds.close();
        assertNull(scds.getConnection());
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNull() {
        scds.addInitializer(null);
    }
}