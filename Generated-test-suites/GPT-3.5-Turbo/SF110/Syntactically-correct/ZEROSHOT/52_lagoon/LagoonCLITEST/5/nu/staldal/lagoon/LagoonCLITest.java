import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {
  
  @Test
  public void testMain_invalidArguments() {
    String[] args = new String[0];
    LagoonCLI.main(args);
  }
  
  @Test
  public void testMain_buildArgument() {
    String[] args = {"property_file", "build"};
    LagoonCLI.main(args);
  }
  
  @Test
  public void testMain_forceArgument() {
    String[] args = {"property_file", "force"};
    LagoonCLI.main(args);
  }
  
  @Test
  public void testMain_intervalArgument() {
    String[] args = {"property_file", "500"};
    LagoonCLI.main(args);
  }
  
  @Test
  public void testMain_sitemapArgument() {
    String[] args = {"sitemap_file", "build"};
    LagoonCLI.main(args);
  }
  
  @Test
  public void testMain_invalidIntervalArgument() {
    String[] args = {"property_file", "abc"};
    LagoonCLI.main(args);
  }
  
  @Test
  public void testMain_correctArguments() {
    String[] args = {"property_file", "1000"};
    LagoonCLI.main(args);
  }
  
  @Test
  public void testGetProperty() {
    LagoonCLI.properties = new Properties();
    LagoonCLI.properties.setProperty("targetURL", "http://example.com");
    LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
    LagoonCLI.properties.setProperty("sourceDir", "src/main");
    String name = "targetURL";
    String expectedValue = "http://example.com";
    String actualValue = LagoonCLI.getProperty(name);
    assertEquals(expectedValue, actualValue);
  }
  
  @Test(expected = LagoonException.class)
  public void testGetProperty_missingProperty() {
    LagoonCLI.properties = new Properties();
    String name = "targetURL";
    LagoonCLI.getProperty(name);
  }
  
  @Test
  public void testShowTime_lessThanTenSeconds() {
    long ms = 5000L;
    String expectedValue = "in 5000 ms";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    LagoonCLI.showTime(ms);
    String actualValue = outContent.toString().trim();
    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testShowTime_moreThanTenSeconds() {
    long ms = 15000L;
    String expectedValue = "in 15 s";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    LagoonCLI.showTime(ms);
    String actualValue = outContent.toString().trim();
    assertEquals(expectedValue, actualValue);
  }
}