
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource();
        this.dataSource.setUsername("test");
        this.dataSource.setPassword("test");
    }

    @After
    public void tearDown() {
        if(this.dataSource != null){
            this.dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException{
        assertNotNull(this.dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        this.dataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testGetConnectionWithValidCredentials() throws SQLException {
        assertNotNull(this.dataSource.getConnection(this.dataSource.getUsername(), this.dataSource.getPassword()));
    }

    @Test
    public void testResetConnection() throws SQLException {
        assertNotNull(this.dataSource.getConnection());
        this.dataSource.resetConnection();
        assertTrue(this.dataSource.getConnection().isValid(this.dataSource.getLoginTimeout()));
    }

    @Test
    public void testAddInitializer(){
        int initSize = this.dataSource.getInitializerList().size();
        this.dataSource.addInitializer(connection -> connection.setAutoCommit(false));
        assertEquals(initSize + 1, this.dataSource.getInitializerList().size());
    }

    @Test
    public void testAutoCommit() throws SQLException {
        Boolean autoCommit = this.dataSource.getAutoCommit();
        this.dataSource.setAutoCommit(!autoCommit);

        Boolean newAutoCommit = this.dataSource.getConnection().getAutoCommit();
        assertNotEquals(autoCommit, newAutoCommit);
    }

}
