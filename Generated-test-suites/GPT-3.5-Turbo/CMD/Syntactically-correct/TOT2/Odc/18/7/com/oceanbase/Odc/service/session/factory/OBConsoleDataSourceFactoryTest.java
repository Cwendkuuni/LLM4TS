import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        // Setup ConnectionConfig for testing
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");

        Map<String, Object> jdbcUrlParameters = new HashMap<>();
        jdbcUrlParameters.put("characterEncoding", "UTF-8");
        jdbcUrlParameters.put("useUnicode", true);
        connectionConfig.setJdbcUrlParameters(jdbcUrlParameters);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=exception&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false&characterEncoding=UTF-8&useUnicode=true";
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_ORACLE;
        DialectType actualDialectType = dataSourceFactory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
        assertTrue(clonedFactory instanceof OBConsoleDataSourceFactory);
        assertEquals(dataSourceFactory.getJdbcUrl(), ((OBConsoleDataSourceFactory) clonedFactory).getJdbcUrl());
    }

}