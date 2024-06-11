import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

  @Test
  public void testGetDataSource() {
    // Create a instance of OBConsoleDataSourceFactory
    OBConsoleDataSourceFactory  dataSourceFactory = new OBConsoleDataSourceFactory();
    // specify testConnectionConfig
    ConnectionConfig testConnectionConfig = new ConnectionConfig();
    // set the variables of testConnectionConfig, eg: testConnectionConfig.setHost("localhost");

    // Create a dataSource from testConnectionConfig
    DataSource createdDataSource = dataSourceFactory.getDataSource(testConnectionConfig);
      
    // Assertions
    assertNotNull(createdDataSource);
    assertEquals("Checks the dataSource username", "expectedUsername", createdDataSource.getUsername());
    assertEquals("Checks the dataSource password", "expectedPassword", createdDataSource.getPassword());
    assertEquals("Checks the dataSource host", "expectedHost", createdDataSource.getHost());
    assertEquals("Checks the dataSource port", "expectedPort", createdDataSource.getPort());
  }

  // Here you would generate the rest of the tests following the example of one provided

}