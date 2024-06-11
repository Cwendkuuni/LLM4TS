import org.junit.*;
import static org.junit.Assert.*;

public class LagoonCLITest {
    private String[] args;
    
    @Before
    public void setUp() {
        args = new String[2];
    }
    
    @Test
    public void testMainWithInvalidArgs() {
        LagoonCLI.main(args);
        // Assert that nothing is printed to console
        assertEquals("", outContent.toString());
    }
    
    @Test
    public void testMainWithBuildArg() {
        args[0] = "property_file";
        args[1] = "build";
        
        LagoonCLI.main(args);
        // Assert that "Building website..." is printed to console
        assertTrue(outContent.toString().contains("Building website..."));
    }
    
    @Test
    public void testMainWithForceArg() {
        args[0] = "property_file";
        args[1] = "force";
        
        LagoonCLI.main(args);
        // Assert that "Force building website..." is printed to console
        assertTrue(outContent.toString().contains("Force building website..."));
    }
    
    @Test
    public void testMainWithInterval() {
        args[0] = "property_file";
        args[1] = "10";
        
        LagoonCLI.main(args);
        // Assert that "Building website..." is printed to console
        assertTrue(outContent.toString().contains("Building website..."));
    }
    
    @Test
    public void testMainWithInvalidPropertyFile() {
        args[0] = "invalid_property_file";
        
        LagoonCLI.main(args);
        // Assert that "Error while initializing Lagoon: File not found" is printed to console
        assertTrue(outContent.toString().contains("Error while initializing Lagoon: File not found"));
    }
    
    @Test
    public void testMainWithInvalidSitemapFile() {
        args[0] = "invalid_sitemap_file";
        
        LagoonCLI.main(args);
        // Assert that "Error while initializing Lagoon: File not found" is printed to console
        assertTrue(outContent.toString().contains("Error while initializing Lagoon: File not found"));
    }
    
    @Test
    public void testMainWithInvalidPassword() {
        args[0] = "property_file_with_invalid_password";
        
        LagoonCLI.main(args);
        // Assert that "Error while initializing Lagoon: Incorrect password" is printed to console
        assertTrue(outContent.toString().contains("Error while initializing Lagoon: Incorrect password"));
    }
    
    @Test
    public void testMainWithIOException() {
        args[0] = "property_file_with_io_exception";
        
        LagoonCLI.main(args);
        // Assert that "Error while initializing Lagoon: I/O error" is printed to console
        assertTrue(outContent.toString().contains("Error while initializing Lagoon: I/O error"));
    }
    
    @Test
    public void testMainWithLagoonException() {
        args[0] = "property_file_with_lagoon_exception";
        
        LagoonCLI.main(args);
        // Assert that "Error while initializing Lagoon: <exception message>" is printed to console
        assertTrue(outContent.toString().contains("Error while initializing Lagoon: "));
    }
    
    @Test
    public void testMainWithInputFromConsole() {
        args[0] = "property_file";
        
        LagoonCLI.main(args);
        // Assert that "Ready" is printed to console
        assertTrue(outContent.toString().contains("Ready"));
    }
    
    @Test
    public void testGetPropertyWithValidValue() {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "src");
        
        try {
            String targetURL = LagoonCLI.getProperty("targetURL");
            String sitemapFile = LagoonCLI.getProperty("sitemapFile");
            String sourceDir = LagoonCLI.getProperty("sourceDir");
            
            assertEquals("http://example.com", targetURL);
            assertEquals("sitemap.xml", sitemapFile);
            assertEquals("src", sourceDir);
        } catch (LagoonException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetPropertyWithMissingValue() {
        LagoonCLI.properties = new Properties();
        
        try {
            LagoonCLI.getProperty("targetURL");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTimeWithLessThan10Seconds() {
        long time = 1234L;
        LagoonCLI.showTime(time);
        // Assert that "in 1234 ms" is printed to console
        assertTrue(outContent.toString().contains("in 1234 ms"));
    }
    
    @Test
    public void testShowTimeWithMoreThan10Seconds() {
        long time = 12345L;
        LagoonCLI.showTime(time);
        // Assert that "in 12 s" is printed to console
        assertTrue(outContent.toString().contains("in 12 s"));
    }
    
    @After
    public void tearDown() {
        args = null;
    }
}