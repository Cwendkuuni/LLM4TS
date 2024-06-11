import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
  
  private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

  @Before
  public void setUp() {
    ConnectionConfig connectionConfig = new ConnectionConfig();
    connectionConfig.setHost("localhost");
    connectionConfig.setPort(3306);
    connectionConfig.setUsername("user");
    connectionConfig.setPassword("password");
    connectionConfig.setDefaultSchema("test_schema");
    
    obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
  }
  
  @Test
  public void testGetJdbcUrl() {
    String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
    assertEquals("jdbc:mysql://localhost:3306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false", jdbcUrl);
  }
  
  @Test
  public void testGetDataSource() {
    DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
    assertNotNull(dataSource);
  }

}