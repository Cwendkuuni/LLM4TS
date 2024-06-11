import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("default_schema");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        // Act
        String jdbcUrl = dataSourceFactory.getJdbcUrl();

        // Assert
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("localhost"));
        assertTrue(jdbcUrl.contains("3306"));
        assertTrue(jdbcUrl.contains("default_schema"));
    }

    @Test
    public void testGetDataSource() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("default_schema");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        // Act
        DataSource dataSource = dataSourceFactory.getDataSource();

        // Assert
        assertNotNull(dataSource);
        assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
    }

    @Test
    public void testGetDialectType() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        // Act
        DialectType dialectType = dataSourceFactory.getDialectType();

        // Assert
        assertNotNull(dialectType);
        assertEquals(DialectType.MYSQL, dialectType);
    }

    // Add more test cases for other methods as needed

}

