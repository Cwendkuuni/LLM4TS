
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    ConnectionConfig connectionConfig;
    
    @Mock
    EventPublisher eventPublisher;

    @InjectMocks
    OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @BeforeEach
    public void init() {
        Mockito.when(connectionConfig.getHost()).thenReturn("localhost");
        Mockito.when(connectionConfig.getPort()).thenReturn(8080);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        //init others mock value 
    }

    @Test
    public void shouldReturnJdbcUrl() {
        assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void shouldReturnUsername() {
        assertNotNull(OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void shouldReturnPassword() {
        assertNotNull(OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void shouldReturnJdbcParams() {
        assertNotNull(OBConsoleDataSourceFactory.getJdbcParams(connectionConfig));
    }

    @Test
    public void shouldGetDatasource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void shouldReturnDialectType() {
        assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void shouldDeepCopy() {
        assertNotNull(obConsoleDataSourceFactory.deepCopy());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInvalidPort() {
        obConsoleDataSourceFactory.resetPort(val -> -10);
    }

}
