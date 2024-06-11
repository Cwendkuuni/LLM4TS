
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.common.event.EventPublisher;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBTenantEndpoint endpoint = new OBTenantEndpoint("proxyHost","proxyPort");
        connectionConfig.setEndpoint(endpoint);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();

        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.startsWith("jdbc:"));
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();

        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();

        assertNotNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copiedFactory = factory.deepCopy();

        assertNotNull(copiedFactory);
        assertTrue(copiedFactory instanceof OBConsoleDataSourceFactory);
    }
}
