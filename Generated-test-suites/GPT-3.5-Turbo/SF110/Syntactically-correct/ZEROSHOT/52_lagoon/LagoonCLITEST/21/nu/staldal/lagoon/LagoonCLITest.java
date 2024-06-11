import org.junit.*;
import java.io.*;
import nu.staldal.xtree.*;

public class LagoonCLITest {
  
  @Test
  public void testMain() {
    // Test Case: When property_file is not passed as argument
    String[] args1 = {};
    LagoonCLI.main(args1);
    
    // Test Case: When property_file and interval are passed as arguments
    String[] args2 = {"property_file", "interval"};
    LagoonCLI.main(args2);
    
    // Test Case: When property_file and build option are passed as arguments
    String[] args3 = {"property_file", "build"};
    LagoonCLI.main(args3);
    
    // Test Case: When property_file and force option are passed as arguments
    String[] args4 = {"property_file", "force"};
    LagoonCLI.main(args4);
    
    // Test Case: When sitemap_file is passed as argument and it ends with ".xml"
    String[] args5 = {"sitemap_file.xml"};
    LagoonCLI.main(args5);
    
    // Test Case: When sitemap_file is passed as argument and it ends with ".sitemap"
    String[] args6 = {"sitemap_file.sitemap"};
    LagoonCLI.main(args6);
    
    // Test Case: When property_file is passed as argument and the properties file is valid
    String[] args7 = {"property_file"};
    LagoonCLI.main(args7);
  }
  
  @Test
  public void testGetProperty() throws LagoonException {
    // Test Case: When the specified property is not found
    LagoonCLI.properties = new Properties();
    try {
      LagoonCLI.getProperty("targetURL");
      Assert.fail("Expected LagoonException to be thrown");
    } catch (LagoonException e) {
      Assert.assertEquals("Property targetURL not specified", e.getMessage());
    }
    
    // Test Case: When the specified property is found
    LagoonCLI.properties = new Properties();
    LagoonCLI.properties.setProperty("targetURL", "http://example.com");
    try {
      String targetURL = LagoonCLI.getProperty("targetURL");
      Assert.assertEquals("http://example.com", targetURL);
    } catch (LagoonException e) {
      Assert.fail("Unexpected LagoonException thrown");
    }
  }
  
  @Test
  public void testShowTime() {
    // Test Case: When the time is less than 10 seconds
    ByteArrayOutputStream outContent1 = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent1));
    LagoonCLI.showTime(5000);
    Assert.assertEquals("in 5000 ms\n", outContent1.toString());
    
    // Test Case: When the time is more than 10 seconds
    ByteArrayOutputStream outContent2 = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent2));
    LagoonCLI.showTime(15000);
    Assert.assertEquals("in 15 s\n", outContent2.toString());
  }
}