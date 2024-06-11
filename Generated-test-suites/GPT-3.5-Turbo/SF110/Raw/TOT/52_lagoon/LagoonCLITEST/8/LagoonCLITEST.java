import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class LagoonCLITest {

    private LagoonCLI lagoon;

    @Before
    public void setUp() {
        lagoon = new LagoonCLI();
    }

    // Test cases for main method

    @Test
    public void testMain_InvalidArguments() {
        // Test that main method prints the syntax message when no arguments are provided
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        OutputStream os = System.out;
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        
        LagoonCLI.main(new String[]{});
        
        System.setOut(System.out);
        assertEquals(expectedOutput, os.toString());
    }

    @Test
    public void testMain_BuildOption() {
        // Test that main method sets the build flag when "build" option is provided
        String[] args = {"property_file", "build"};
        
        LagoonCLI.main(args);
        
        assertTrue(lagoon.build);
        assertFalse(lagoon.force);
    }

    @Test
    public void testMain_ForceOption() {
        // Test that main method sets the force flag when "force" option is provided
        String[] args = {"property_file", "force"};
        
        LagoonCLI.main(args);
        
        assertTrue(lagoon.force);
        assertFalse(lagoon.build);
    }

    @Test
    public void testMain_IntervalOption() {
        // Test that main method sets the interval correctly when a valid interval is provided
        String[] args = {"property_file", "10"};
        
        LagoonCLI.main(args);
        
        assertEquals(10000, lagoon.interval);
        assertFalse(lagoon.build);
        assertFalse(lagoon.force);
    }

    // Test cases for getProperty method

    @Test(expected = LagoonException.class)
    public void testGetProperty_MissingProperty() throws LagoonException {
        // Test that getProperty method throws LagoonException when a required property is missing
        lagoon.properties = new Properties();
        lagoon.properties.setProperty("targetURL", "http://example.com");
        
        lagoon.getProperty("sitemapFile");
    }

    @Test
    public void testGetProperty_ValidProperty() {
        // Test that getProperty method returns the correct value when a valid property is provided
        lagoon.properties = new Properties();
        lagoon.properties.setProperty("targetURL", "http://example.com");
        lagoon.properties.setProperty("sitemapFile", "sitemap.xml");
        
        String propertyValue = lagoon.getProperty("sitemapFile");
        
        assertEquals("sitemap.xml", propertyValue);
    }

    // Test cases for showTime method

    @Test
    public void testShowTime_LessThan10Seconds() {
        // Test that showTime method prints the time in milliseconds for values less than 10 seconds
        String expectedOutput = "in 5000 ms\n";
        OutputStream os = System.out;
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        
        lagoon.showTime(5000);
        
        System.setOut(System.out);
        assertEquals(expectedOutput, os.toString());
    }

    @Test
    public void testShowTime_MoreThan10Seconds() {
        // Test that showTime method prints the time in seconds for values more than 10 seconds
        String expectedOutput = "in 15 s\n";
        OutputStream os = System.out;
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        
        lagoon.showTime(15000);
        
        System.setOut(System.out);
        assertEquals(expectedOutput, os.toString());
    }

}