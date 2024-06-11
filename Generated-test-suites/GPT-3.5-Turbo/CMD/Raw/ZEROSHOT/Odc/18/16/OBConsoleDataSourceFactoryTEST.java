import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private EventPublisher eventPublisher;
    
    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        eventPublisher = mock(EventPublisher.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }
    
    @Test
    public void testGetUsername() {
        String username = ConnectionSessionUtil.getUserOrSchemaString("user1", DialectType.OB_ORACLE);
        assertEquals("\"user1\"", username);
    }
    
    @Test
    public void testGetPassword() {
        when(connectionConfig.getPassword()).thenReturn("password");
        assertEquals("password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }
    
    @Test
    public void testGetJdbcParams() {
        SSLConfig sslConfig = mock(SSLConfig.class);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        when(connectionConfig.getSslConfig()).thenReturn(sslConfig);
        when(sslConfig.getEnabled()).thenReturn(true);
        when(connectionConfig.getSslFileEntry()).thenReturn(sslFileEntry);
        
        assertNotNull(OBConsoleDataSourceFactory.getJdbcParams(connectionConfig));
    }
    
    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
    }
    
    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }
    
    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
        assertEquals(OBConsoleDataSourceFactory.class, clonedFactory.getClass());
    }
    
    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        assertNotNull(dataSourceFactory.getUsername());
        dataSourceFactory.resetUsername(mapper);
        verify(mapper).map(any());
    }
    
    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        assertNotNull(dataSourceFactory.getPassword());
        dataSourceFactory.resetPassword(mapper);
        verify(mapper).map(any());
    }
    
    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        assertNotNull(dataSourceFactory.getJdbcUrl());
        dataSourceFactory.resetHost(mapper);
        verify(mapper).map(any());
    }
    
    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        assertNotNull(dataSourceFactory.getJdbcUrl());
        dataSourceFactory.resetPort(mapper);
        verify(mapper).map(any());
    }
    
    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        assertNotNull(dataSourceFactory.getJdbcUrl());
        dataSourceFactory.resetSchema(mapper);
        verify(mapper).map(any());
    }
    
    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        assertNotNull(dataSourceFactory.getJdbcUrl());
        dataSourceFactory.resetParameters(mapper);
        verify(mapper).map(any());
    }
}