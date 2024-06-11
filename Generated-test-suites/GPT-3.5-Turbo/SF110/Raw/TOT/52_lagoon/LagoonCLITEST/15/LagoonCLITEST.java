import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class LagoonCLITest {

    // Test case for main() method when no arguments are provided
    @Test
    public void testMainNoArguments() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[0]);

        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString().trim());
    }

    // Test case for main() method when property_file argument is provided without <interval>|build|force argument
    @Test
    public void testMainPropertyFileArgument() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"file.properties"});

        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString().trim());
    }

    // Test case for main() method when sitemap_file argument is provided without <interval>|build|force argument
    @Test
    public void testMainSitemapFileArgument() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap.xml"});

        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString().trim());
    }

    // Test case for main() method when sitemap_file argument is provided with "build" argument
    @Test
    public void testMainSitemapFileBuildArgument() {
        // Mock the LagoonProcessor to test the build() method
        LagoonProcessor mockProcessor = Mockito.mock(LagoonProcessor.class);
        Mockito.when(mockProcessor.build(false)).thenReturn(true);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap.xml", "build"});

        assertTrue(outContent.toString().contains("Building website..."));
        assertTrue(outContent.toString().contains("...with errors..."));
        // Additional assertions to verify the behavior of build() method of LagoonProcessor
    }

    // Test case for main() method when sitemap_file argument is provided with "force" argument
    @Test
    public void testMainSitemapFileForceArgument() {
        // Mock the LagoonProcessor to test the build() method
        LagoonProcessor mockProcessor = Mockito.mock(LagoonProcessor.class);
        Mockito.when(mockProcessor.build(true)).thenReturn(true);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap.xml", "force"});

        assertTrue(outContent.toString().contains("Force building website..."));
        assertTrue(outContent.toString().contains("...with errors..."));
        // Additional assertions to verify the behavior of build() method of LagoonProcessor
    }

    // Test case for main() method when sitemap_file argument is provided with numeric interval argument
    @Test
    public void testMainSitemapFileNumericIntervalArgument() {
        // Mock the LagoonProcessor to test the build() method
        LagoonProcessor mockProcessor = Mockito.mock(LagoonProcessor.class);
        Mockito.when(mockProcessor.build(false)).thenReturn(true);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap.xml", "5"});

        assertTrue(outContent.toString().contains("Building website..."));
        assertTrue(outContent.toString().contains("...with errors..."));
        // Additional assertions to verify the behavior of main() method for interval functionality
    }

    // Test case for main() method when sitemap_file argument is provided with non-numeric interval argument
    @Test
    public void testMainSitemapFileNonNumericIntervalArgument() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap.xml", "abc"});

        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString().trim());
    }

    // Test case for main() method when sitemap_file argument is provided with invalid interval (less than 1L)
    @Test
    public void testMainSitemapFileInvalidIntervalArgument() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap.xml", "0"});

        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString().trim());
    }

    // Test case for getProperty() method when a valid property name is provided
    @Test
    public void testGetPropertyValidPropertyName() throws LagoonException {
        // Set up the properties
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.setProperties(properties);

        String value = LagoonCLI.getProperty("targetURL");

        assertEquals("http://example.com", value);
    }

    // Test case for getProperty() method when an invalid property name is provided
    @Test(expected = LagoonException.class)
    public void testGetPropertyInvalidPropertyName() throws LagoonException {
        // Set up the properties
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.setProperties(properties);

        LagoonCLI.getProperty("invalidProperty");
    }

    // Test case for showTime() method when the provided time in milliseconds is less than 10000L
    @Test
    public void testShowTimeLessThanTenSeconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(5000);

        assertEquals("in 5000 ms", outContent.toString().trim());
    }

    // Test case for showTime() method when the provided time in milliseconds is more than 10000L
    @Test
    public void testShowTimeMoreThanTenSeconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(15000);

        assertEquals("in 15 s", outContent.toString().trim());
    }

}