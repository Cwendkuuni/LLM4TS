import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private ConnectionExtensionPoint connectionExtensionPoint;
    
    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionPluginUtil.getConnectionExtension(any())).thenReturn(connectionExtensionPoint);
        
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }
    
    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:mysql://localhost:3306/defaultSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=EXCEPTION&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expectedUrl, dataSourceFactory.getJdbcUrl());
    }
    
    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        // Add more assertions based on the expected behavior of the getDataSource method
    }
    
    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, dataSourceFactory.getDialectType());
    }
    
    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copy = dataSourceFactory.deepCopy();
        assertNotNull(copy);
        assertTrue(copy instanceof OBConsoleDataSourceFactory);
        // Add more assertions based on the expected behavior of the deepCopy method
    }
    
    // Add more test methods to cover all other methods in the OBConsoleDataSourceFactory class
    
}