import org.junit.*;
import nu.staldal.lagoon.*;

import java.io.*;
import java.util.*;

public class LagoonCLITest {

    @Test
    public void testMain() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = {"test.property", "build"};
        LagoonCLI.main(args);
        
        String expectedOutput = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testGetProperty() throws LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "https://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "/path/to/source");
        properties.setProperty("password", "password");

        LagoonCLI.properties = properties;

        String result = LagoonCLI.getProperty("targetURL");
        Assert.assertEquals("https://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyThrowsException() throws LagoonException {
        Properties properties = new Properties();
        LagoonCLI.properties = properties;

        LagoonCLI.getProperty("targetURL");
    }

    @Test
    public void testShowTimeLessThanTenSeconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(5000L);

        Assert.assertEquals("in 5000 ms\n", outContent.toString());
    }

    @Test
    public void testShowTimeGreaterThanTenSeconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(15000L);

        Assert.assertEquals("in 15 s\n", outContent.toString());
    }

}