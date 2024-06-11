import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        String[] args = new String[2];
        args[0] = "property_file";
        args[1] = "interval";
        LagoonCLI.main(args);

        args[1] = "build";
        LagoonCLI.main(args);

        args[1] = "force";
        LagoonCLI.main(args);

        args[1] = "non_integer_interval";
        LagoonCLI.main(args);

        args = new String[1];
        args[0] = "property_file";
        LagoonCLI.main(args);

        args[0] = "sitemap_file";
        LagoonCLI.main(args);

        args[0] = "invalid_file";
        LagoonCLI.main(args);

        args = new String[0];
        LagoonCLI.main(args);
    }

    @Test
    public void testGetProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "value1");
        LagoonCLI.properties.setProperty("sitemapFile", "value2");
        LagoonCLI.properties.setProperty("sourceDir", "value3");
        LagoonCLI.properties.setProperty("password", "value4");

        assertEquals("value1", LagoonCLI.getProperty("targetURL"));
        assertEquals("value2", LagoonCLI.getProperty("sitemapFile"));
        assertEquals("value3", LagoonCLI.getProperty("sourceDir"));
        assertEquals("value4", LagoonCLI.getProperty("password"));

        try {
            LagoonCLI.getProperty("invalid_property");
            fail("LagoonException not thrown");
        } catch (LagoonException e) {
            assertEquals("Property invalid_property not specified", e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        LagoonCLI.showTime(5000);
        LagoonCLI.showTime(15000);
    }
}