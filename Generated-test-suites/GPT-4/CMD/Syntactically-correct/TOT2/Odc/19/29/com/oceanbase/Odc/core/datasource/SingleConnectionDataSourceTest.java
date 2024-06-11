package com.oceanbase.odc.core.datasource;

import org.junit.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    @Test(expected = SQLException.class)
    public void whenGetConnectionInvalid_thenThrowException() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void whenGetUserNameAndPasswordNotCorrect_ThenThrowSQLException() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection("randomUserName", "randomPassword");
    }

    @Test(expected = SQLException.class)
    public void whenResetConnectionAndErrorWhileClosing_ThenThrowSQLException() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
    }

    @Test(expected = NullPointerException.class)
    public void whenAddInitializerIsNull_ThenThrowNullPointerException() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(null);
    }

    @Test
    public void whenCloseCalledTwice_ThenNoExceptions() { // assuming the connection was properly initialized
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.close();
        dataSource.close(); // this should not throw any exception 
    }
    
}
