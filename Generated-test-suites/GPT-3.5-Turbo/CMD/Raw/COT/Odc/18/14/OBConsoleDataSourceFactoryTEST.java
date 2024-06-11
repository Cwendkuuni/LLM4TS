
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig mockConnectionConfig;
    private ConnectionExtensionPoint mockConnectionExtensionPoint;
    private EventPublisher mockEventPublisher;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        mockConnectionConfig = mock(ConnectionConfig.class);
        mockConnectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        mockEventPublisher = mock(EventPublisher.class);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mockConnectionConfig, true, false);
        obConsoleDataSourceFactory.setEventPublisher(mockEventPublisher);
        obConsoleDataSourceFactory.setConnectionExtensionPoint(mockConnectionExtensionPoint);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        when(mockConnectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap())).thenReturn("jdbc:mysql://localhost/test");
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost/test", jdbcUrl);
    }

    @Test
    public void testGetDialectType() {
        when(mockConnectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    // Add more test cases as needed for edge cases and exception handling scenarios
}

