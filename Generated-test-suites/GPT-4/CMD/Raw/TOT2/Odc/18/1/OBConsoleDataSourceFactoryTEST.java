import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {
    @Test
    public void testGetDataSource() {
        ConnectionConfig mockConfig = Mockito.mock(ConnectionConfig.class);
        ConnectionExtensionPoint mockExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        Mockito.when(mockConfig.getHost()).thenReturn("localhost");
        Mockito.when(mockConfig.getPort()).thenReturn(8080);
        Mockito.when(mockConfig.getDialectType()).thenReturn(DialectType.ORACLE);
        Mockito.when(mockExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.any())).thenReturn("JDBC URL");
        Mockito.when(mockExtensionPoint.getDriverClassName()).thenReturn("com.mysql.cj.jdbc.Driver");

        OBConsoleDataSourceFactory consoleDataSourceFactory = new OBConsoleDataSourceFactory(mockConfig, false, true);
        consoleDataSourceFactory.setConnectionExtensionPoint(mockExtensionPoint);
        DataSource dataSource = consoleDataSourceFactory.getDataSource();

        // Add verification and assertion here...
    }
}