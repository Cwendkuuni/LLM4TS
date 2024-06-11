package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link SingleConnectionDataSource}
 */
public class SingleConnectionDataSourceTest {

    @InjectMocks
    private SingleConnectionDataSource underTest;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private Connection mockConnection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest.setAutoReconnect(true);
        underTest.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        doAnswer(invocation -> underTest.connection = mockConnection).when(underTest).innerCreateConnection();
        underTest.addInitializer(connectionInitializer);
        Connection actualConnection = underTest.getConnection();
        verify(connectionInitializer).init(mockConnection);
        assert (actualConnection == mockConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        when(mockConnection.isValid(anyInt())).thenReturn(false);

        underTest.addInitializer(connectionInitializer);
        underTest.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);

        underTest.resetConnection();
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        underTest.addInitializer(connectionInitializer);
        List<ConnectionInitializer> actualInitializerList = underTest.getInitializerList();
        assertEquals(1, actualInitializerList.size());
        verify(connectionInitializer).init(any(Connection.class));
    }

    @Test
    public void testAutoCommit() throws SQLException {
        underTest.setAutoCommit(true);
        Connection connection = underTest.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)");
        statement.execute("INSERT INTO test_table VALUES (1)");
        statement.executeUpdate("UPDATE test_table SET id = 2 WHERE id = 1");
        statement.close();
        connection.commit();

        underTest.setAutoCommit(false);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        statement.executeUpdate("UPDATE test_table SET id = 3 WHERE id = 2");
        statement.close();
        connection.rollback();
    }

    @Test(expected = ConflictException.class)
    public void testConnectionOccupied() throws SQLException {
        underTest.setAutoCommit(true);
        Connection connection1 = underTest.getConnection();
        Connection connection2 = underTest.getConnection();

        connection1.close();

        connection2.createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)");
    }

    @Test
    public void testClose() throws SQLException {
        underTest.getConnection();
        underTest.close();
        underTest.getConnection();
    }

    private List<ConnectionInitializer> getInitializerList(SingleConnectionDataSource underTest) {
        return underTest.getInitializerList();
    }
}