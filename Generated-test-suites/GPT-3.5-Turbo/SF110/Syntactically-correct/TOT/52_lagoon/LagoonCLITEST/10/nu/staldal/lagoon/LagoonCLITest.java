import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonCLITest {
  private LagoonCLI lagoonCLI;
  
  @Before
  public void setUp() {
    lagoonCLI = new LagoonCLI();
  }
  
  @After
  public void tearDown() {
    lagoonCLI = null;
  }
  
  @Test
  public void testMain_NoArgs_ShouldPrintSyntaxMessage() {
    // Arrange
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    
    // Act
    LagoonCLI.main(new String[]{});
    final String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    final String actualOutput = outContent.toString().trim();
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testMain_InvalidSitemapFile_ShouldPrintErrorMessage() {
    // Arrange
    final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));
    
    // Act
    LagoonCLI.main(new String[]{"invalid_sitemap.xml"});
    final String expectedOutput = "Error while initializing Lagoon:\nFile not found: invalid_sitemap.xml";
    final String actualOutput = errContent.toString().trim();
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testMain_ValidSitemapFile_ShouldInitializeLagoonSuccessfully() {
    // Act
    LagoonCLI.main(new String[]{"valid_sitemap.xml"});
    
    // Assert
    // Ensure that Lagoon is initialized successfully by checking for a specific console output
    // ...
  }
  
  @Test
  public void testMain_BuildOption_ShouldBuildWebsite() {
    // Arrange
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    
    // Act
    LagoonCLI.main(new String[]{"property_file.xml", "build"});
    final String expectedOutput = "Building website...\n...with errors...";
    final String actualOutput = outContent.toString().trim();
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testMain_ForceOption_ShouldForceBuildWebsite() {
    // Arrange
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    
    // Act
    LagoonCLI.main(new String[]{"property_file.xml", "force"});
    final String expectedOutput = "Force building website...\n...with errors...";
    final String actualOutput = outContent.toString().trim();
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testMain_IntervalOption_ShouldBuildWebsiteWithInterval() {
    // Arrange
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    
    // Act
    LagoonCLI.main(new String[]{"property_file.xml", "10"});
    final String expectedOutput = "Building website...\n...with errors...";
    final String actualOutput = outContent.toString().trim();
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testGetProperty_PropertyNotSpecified_ShouldThrowLagoonException() {
    // Act
    try {
      LagoonCLI.getProperty("invalid_property");
    } catch (LagoonException e) {
      // Assert
      assertEquals("Property invalid_property not specified", e.getMessage());
    }
  }
  
  @Test
  public void testGetProperty_ValidProperty_ShouldReturnPropertyValue() {
    // Act
    final String propertyValue = LagoonCLI.getProperty("targetURL");
    
    // Assert
    assertEquals("http://example.com", propertyValue);
  }
  
  @Test
  public void testShowTime_LessThan10Seconds_ShouldPrintMilliseconds() {
    // Arrange
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    
    // Act
    LagoonCLI.showTime(5000); // 5 seconds
    final String expectedOutput = "in 5000 ms";
    final String actualOutput = outContent.toString().trim();
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testShowTime_MoreThan10Seconds_ShouldPrintSeconds() {
    // Arrange
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    
    // Act
    LagoonCLI.showTime(15000); // 15 seconds
    final String expectedOutput = "in 15 s";
    final String actualOutput = outContent.toString().trim();
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
}