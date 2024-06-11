package com.oceanbase.odc.core.datasource;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.sql.Connection;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

    @Test
    public void testGetConnectionReturnsNotNull() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithUsernameAndPasswordThrowsExceptionForInvalidInput() throws SQLException {
        dataSource.getConnection("invalid_username", "invalid_password");
    }

    @Test
    public void testResetConnectionDoesNotThrowException() throws SQLException {
        dataSource.resetConnection();
    }

    @Test
    public void testCloseDoesNotThrowException() {
        dataSource.close();
    }

    @Test
    public void testAddInitializer() {
        int initialSize = dataSource.initializerList.size();
        dataSource.addInitializer(connection -> {});
        assertEquals(initialSize + 1, dataSource.initializerList.size());
    }

    @Test
    public void testSetAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertEquals(true, connection.getAutoCommit());
    }

    @Test
    public void testTryLockTimeout() {
        dataSource.setTimeOutMillis(5);
        dataSource.tryLock();
        long start = System.currentTimeMillis();
        try {
            dataSource.tryLock();
        } catch (Exception e) {
            assertTrue(System.currentTimeMillis()-start >= 5);
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void testGetConnectionProxy() {
        assertNotNull(dataSource.getConnectionProxy(connection));
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionThrowsSQLExceptionWhenConnectionIsNotNull() throws SQLException {
        dataSource.innerCreateConnection();
        dataSource.innerCreateConnection();
    }      
}