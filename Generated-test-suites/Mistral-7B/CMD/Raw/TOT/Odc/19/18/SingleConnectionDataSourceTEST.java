package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SingleConnectionDataSource}.
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        eventPublisher = mock(EventPublisher.class);
        connection = Mockito.mock(Connection.class);
        dataSource.setEventPublisher(eventPublisher);
        when(connection.isValid(10 * 1000)).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
    }

    @Test
    public void testGetConnection() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Statement statement = connection.createStatement();
                statement.execute("CREATE TABLE test_table (id INT PRIMARY KEY)");;
            }
        });
        Connection connection = dataSource.getConnection();
        assertTrue(connection.isValid(10 * 1000));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionThrowsException() throws SQLException {
        when(connection.isValid(10 * 1000)).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                throw new SQLException("Invalid database connection.");
            }
        });
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Statement statement = connection.createStatement();
                statement.execute("CREATE TABLE test_table (id INT PRIMARY KEY)");;
            }
        });
        dataSource.getConnection();
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertTrue(connection.isValid(10 * 1000));
        assertEquals(connection, dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.initializerList.contains(initializer));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionConcurrency() throws SQLException {
        Connection initialConnection = dataSource.getConnection();
        dataSource.getConnection();
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Statement statement = connection.createStatement();
                statement.execute("CREATE TABLE test_table (id INT PRIMARY KEY)");;
            }
        });
        Connection connection = dataSource.getConnection();
        dataSource.close();
        Connection newConnection = dataSource.getConnection();
        assertTrue(newConnection.isValid(10 * 1000));
        assertEquals(newConnection, connection);
    }
}