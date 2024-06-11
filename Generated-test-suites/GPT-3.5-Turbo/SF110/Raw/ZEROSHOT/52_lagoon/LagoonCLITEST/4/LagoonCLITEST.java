import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {
    
    // Test main method with empty arguments
    @Test
    public void testMainEmptyArgs() {
        String[] args = {};
        LagoonCLI.main(args);
        // assert statements
    }
    
    // Test main method with invalid arguments
    @Test
    public void testMainInvalidArgs() {
        String[] args = {"arg1"};
        LagoonCLI.main(args);
        // assert statements
    }

    // Test main method with valid property file argument
    @Test
    public void testMainPropertyFileArg() {
        String[] args = {"property_file"};
        LagoonCLI.main(args);
        // assert statements
    }

    // Test main method with valid sitemap file argument
    @Test
    public void testMainSitemapFileArg() {
        String[] args = {"sitemap_file"};
        LagoonCLI.main(args);
        // assert statements
    }

    // Test build option in main method
    @Test
    public void testMainBuildOption() {
        String[] args = {"property_file", "build"};
        LagoonCLI.main(args);
        // assert statements
    }
    
    // Test force option in main method
    @Test
    public void testMainForceOption() {
        String[] args = {"property_file", "force"};
        LagoonCLI.main(args);
        // assert statements
    }
    
    // Test interval option in main method
    @Test
    public void testMainIntervalOption() {
        String[] args = {"property_file", "10"};
        LagoonCLI.main(args);
        // assert statements
    }

    // Test getProperty method with valid property name
    @Test
    public void testGetPropertyValidName() throws LagoonException {
        String name = "targetURL";
        String expectedValue = "http://example.com";
        String actualValue = LagoonCLI.getProperty(name);
        assertEquals(expectedValue, actualValue);
    }
    
    // Test getProperty method with invalid property name
    @Test(expected = LagoonException.class)
    public void testGetPropertyInvalidName() throws LagoonException {
        String name = "invalidProperty";
        LagoonCLI.getProperty(name);
    }

    // Test showTime method with time less than 10 seconds
    @Test
    public void testShowTimeLessThan10Seconds() {
        long ms = 5000L;
        String expectedOutput = "in 5 s";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(ms);
        String actualOutput = outContent.toString().trim();
        assertEquals(expectedOutput, actualOutput);
        System.setOut(System.out);
    }
    
    // Test showTime method with time more than 10 seconds
    @Test
    public void testShowTimeMoreThan10Seconds() {
        long ms = 15000L;
        String expectedOutput = "in 15 s";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(ms);
        String actualOutput = outContent.toString().trim();
        assertEquals(expectedOutput, actualOutput);
        System.setOut(System.out);
    }
}