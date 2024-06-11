
package com.oceanbase.odc.core.datasource;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource source;
    private EventPublisher mockPublisher;

    //initialize the DataSource before every test
    @Before
    public void setup(){
       source = new SingleConnectionDataSource();
       mockPublisher = mock(EventPublisher.class);
       source.setEventPublisher(mockPublisher);
    }
    
    //Close the dataSource after every test
    @After
    public void teardown() {
        source.close();
    }

    @Test
    public void testGetAutoCommit(){
        source.setAutoCommit(true);
        assertTrue(source.getAutoCommit());
        source.setAutoCommit(false);
        assertFalse(source.getAutoCommit());
    }
  
    @Test
    public void testGetAutoReconnect(){
        assertFalse(source.getAutoReconnect());
        source = new SingleConnectionDataSource(true);
        assertTrue(source.getAutoReconnect());
    }
   
    @Test
    public void testGetConnection() throws Exception {
        Connection connection = source.getConnection();
        assertNotNull(connection);
        assertTrue(connection instanceof Connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInValidUser() throws Exception {
       source.getConnection("invalid_user", "invalid_pass");
    }

    @Test
    public void testResetConnection() throws Exception {
        Connection beforeReset = source.getConnection();
        source.resetConnection();
        Connection afterReset = source.getConnection();
        assertNotSame(beforeReset, afterReset);
    }

    @Test
    public void testClose() throws Exception {
        Connection connection = source.getConnection();
        source.close();
        try {
            connection.isClosed();
            fail("Should have thrown an exception");
        } catch (SQLException e) {
            assert (e.getMessage().contains("closed"));
        }
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        source.addInitializer(initializer);
        source.getConnection();
        verify(initializer).init(any(Connection.class));
    }
}
