import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LagoonCLITest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testMain() {
        String[] args = {"property_file"};
        LagoonCLI.main(args);
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        Assert.assertEquals(expectedOutput, outContent.toString().trim());

        args = new String[]{"property_file", "build"};
        LagoonCLI.main(args);
        expectedOutput = "Error while initializing Lagoon:\nPassword is required but not specified";
        Assert.assertEquals(expectedOutput, errContent.toString().trim());

        args = new String[]{"property_file", "force"};
        LagoonCLI.main(args);
        expectedOutput = "Error while initializing Lagoon:\nIncorrect password";
        Assert.assertEquals(expectedOutput, errContent.toString().trim());

        args = new String[]{"property_file", "invalid"};
        LagoonCLI.main(args);
        expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        Assert.assertEquals(expectedOutput, outContent.toString().trim());

        args = new String[]{"property_file", "10"};
        LagoonCLI.main(args);
        expectedOutput = "Error while initializing Lagoon:\nFile not found: null";
        Assert.assertEquals(expectedOutput, errContent.toString().trim());

        args = new String[]{"valid_sitemap.xml", "10"};
        LagoonCLI.main(args);
        expectedOutput = "Lagoon initialized successfully\nReady";
        Assert.assertTrue(outContent.toString().trim().startsWith(expectedOutput));
    }

    @Test
    public void testGetProperty() {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "src/main/java");
        LagoonCLI.properties.setProperty("password", "password");

        String name = "targetURL";
        try {
            String value = LagoonCLI.getProperty(name);
            Assert.assertEquals("http://example.com", value);
        } catch (LagoonException e) {
            e.printStackTrace();
        }

        name = "invalidName";
        try {
            LagoonCLI.getProperty(name);
            Assert.fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            Assert.assertEquals("Property invalidName not specified", e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        long ms = 5000;
        LagoonCLI.showTime(ms);
        Assert.assertEquals("in 5 s", outContent.toString().trim());

        ms = 10000;
        LagoonCLI.showTime(ms);
        Assert.assertEquals("in 10 s", outContent.toString().trim());
    }
}