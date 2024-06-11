import com.oceanbase.odc.core.shared.constant.DialectType;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertNotNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
    }

}