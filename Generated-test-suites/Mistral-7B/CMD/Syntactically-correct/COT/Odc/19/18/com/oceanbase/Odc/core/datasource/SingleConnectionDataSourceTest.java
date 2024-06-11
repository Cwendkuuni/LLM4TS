package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-31 11:18
 * @since ODC_release_3.2.2
 */
@Slf4j
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private ConnectionInitializer connectionInitializerMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() throws Exception {
        this.dataSource = new SingleConnectionDataSource();
        this.connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
        this.dataSource.setEventPublisher(this.eventPublisherMock);
        this.dataSource.addInitializer(this.connectionInitializerMock);
        this.connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testGetConnection_validCredentials() throws Exception {
        Mockito.when(this.connectionInitializerMock.init(this.connection)).thenReturn(null);
        Connection returnedConnection = this.dataSource.getConnection();
        assertNotNull(returnedConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_invalidCredentials() throws Exception {
        Mockito.when(this.connectionInitializerMock.init(this.connection)).thenThrow(new SQLException("Invalid credentials"));
        this.dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws Exception {
        Mockito.when(this.connectionInitializerMock.init(this.connection)).thenReturn(null);
        this.dataSource.resetConnection();
        Mockito.verify(this.eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnection_connectionIsOccupied() throws Exception {
        Mockito.when(this.connectionInitializerMock.init(this.connection)).thenReturn(null);
        this.dataSource.lock.lock();
        throw new IllegalStateException("Test case should not reach this point");
    }

    @Test
    public void testAddInitializer() throws Exception {
        this.dataSource.addInitializer(this.connectionInitializerMock);
        List<ConnectionInitializer> initializerList = this.dataSource.initializerList;
        assertEquals(1, initializerList.size());
        assertTrue(initializerList.contains(this.connectionInitializerMock));
    }

    @Test
    public void testClose() throws Exception {
        this.dataSource.close();
        Mockito.verify(this.connection).close();
    }
}