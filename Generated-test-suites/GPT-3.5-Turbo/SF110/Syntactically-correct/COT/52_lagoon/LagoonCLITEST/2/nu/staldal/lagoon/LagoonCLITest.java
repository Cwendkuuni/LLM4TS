
import org.junit.*;
import nu.staldal.lagoon.LagoonCLI;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test case to check the functionality of the main method
        String[] args = {"property_file", "interval"};
        LagoonCLI.main(args);
        // Additional test cases for build and force options can be added here
    }

    @Test
    public void testGetProperty() {
        // Test case to check the functionality of the getProperty method
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "src");
        try {
            String targetURL = LagoonCLI.getProperty("targetURL");
            Assert.assertEquals("http://example.com", targetURL);

            String sitemapFile = LagoonCLI.getProperty("sitemapFile");
            Assert.assertEquals("sitemap.xml", sitemapFile);

            String sourceDir = LagoonCLI.getProperty("sourceDir");
            Assert.assertEquals("src", sourceDir);
        } catch (Exception ex) {
            Assert.fail("Failed to get property: " + ex.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        // Test case to check the functionality of the showTime method
        long ms = 5000;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(ms);
        Assert.assertEquals("in 5 s\n", outContent.toString());
    }
}
