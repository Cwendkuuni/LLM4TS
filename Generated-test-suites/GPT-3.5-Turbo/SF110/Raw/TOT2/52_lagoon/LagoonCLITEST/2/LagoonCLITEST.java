import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {

    // Test case for main method with property file and build option
    @Test
    public void testMainPropertyFileBuildOption() {
        String[] args = {"property_file", "build"};
        LagoonCLI.main(args);
        // Add assertions if needed
    }

    // Test case for main method with sitemap file and force option
    @Test
    public void testMainSitemapFileForceOption() {
        String[] args = {"sitemap_file", "force"};
        LagoonCLI.main(args);
        // Add assertions if needed
    }

    // Test case for getProperty method with existing property
    @Test
    public void testGetPropertyExistingProperty() {
        String expected = "value";
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("name", expected);
        try {
            String actual = LagoonCLI.getProperty("name");
            assertEquals(expected, actual);
        } catch (LagoonException e) {
            fail("Exception thrown");
        }
    }

    // Test case for getProperty method with non-existing property
    @Test
    public void testGetPropertyNonExistingProperty() {
        LagoonCLI.properties = new Properties();
        try {
            String actual = LagoonCLI.getProperty("non_existing");
            fail("No exception thrown");
        } catch (LagoonException e) {
            // Exception expected
        }
    }

    // Test case for showTime method with milliseconds less than 10 seconds
    @Test
    public void testShowTimeMillisecondsLessThan10Seconds() {
        long time = 5000;
        String expected = "in 5000 ms";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(time);
        assertEquals(expected, outContent.toString().trim());
    }

    // Test case for showTime method with milliseconds more than 10 seconds
    @Test
    public void testShowTimeMillisecondsMoreThan10Seconds() {
        long time = 15000;
        String expected = "in 15 s";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(time);
        assertEquals(expected, outContent.toString().trim());
    }

}