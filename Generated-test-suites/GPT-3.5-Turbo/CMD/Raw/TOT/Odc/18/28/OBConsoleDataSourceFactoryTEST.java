import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getUsername()).thenReturn("user");
        when(connectionConfig.getPassword()).thenReturn("pass");
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/default?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copy = factory.deepCopy();
        assertNotEquals(factory, copy);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        factory.resetParameters((oldParams) -> params);
        assertEquals(params, factory.getConnectionConfig().getJdbcUrlParameters());
    }

}