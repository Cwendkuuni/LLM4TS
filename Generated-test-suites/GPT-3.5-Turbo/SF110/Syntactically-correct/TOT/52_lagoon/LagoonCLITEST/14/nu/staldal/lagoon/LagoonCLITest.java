import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonCLITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void testMain_InvalidArgument() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testMain_BuildWebsite() {
        String[] args = new String[2];
        args[0] = "property_file.xml";
        args[1] = "build";
        LagoonCLI.main(args);
        assertTrue(outContent.toString().contains("Building website..."));
    }

    @Test
    public void testMain_ForceBuildWebsite() {
        String[] args = new String[2];
        args[0] = "property_file.xml";
        args[1] = "force";
        LagoonCLI.main(args);
        assertTrue(outContent.toString().contains("Force building website..."));
    }

    @Test
    public void testMain_BuildWebsiteWithInterval() {
        String[] args = new String[2];
        args[0] = "property_file.xml";
        args[1] = "10";
        LagoonCLI.main(args);
        assertTrue(outContent.toString().contains("Building website..."));
    }

    @Test
    public void testMain_ReadFromConsole_Quit() {
        String[] args = new String[1];
        args[0] = "property_file.xml";
        LagoonCLI.main(args);
        assertTrue(outContent.toString().contains("Ready"));
        assertTrue(errContent.toString().contains(""));
    }

    @Test
    public void testMain_ReadFromConsole_BuildWebsite() {
        String[] args = new String[1];
        args[0] = "property_file.xml";
        LagoonCLI.main(args);
        assertTrue(outContent.toString().contains("Ready"));
        assertTrue(outContent.toString().contains("Building website..."));
    }

    @Test
    public void testMain_IOException() {
        String[] args = new String[2];
        args[0] = "nonexistent_file.xml";
        args[1] = "build";
        LagoonCLI.main(args);
        assertTrue(errContent.toString().contains("Error while initializing Lagoon:"));
        assertTrue(errContent.toString().contains("File not found: nonexistent_file.xml\n"));
    }

    @Test
    public void testGetProperty_PropertyMissing() {
        LagoonCLI.properties = new Properties();
        try {
            LagoonCLI.getProperty("targetURL");
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
    }

    @Test
    public void testGetProperty_PropertyFound() {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        try {
            String property = LagoonCLI.getProperty("targetURL");
            assertEquals("http://example.com", property);
        } catch (LagoonException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testShowTime_LessThan10Seconds() {
        LagoonCLI.showTime(5000L);
        assertTrue(outContent.toString().contains("in 5000 ms"));
    }

    @Test
    public void testShowTime_MoreThan10Seconds() {
        LagoonCLI.showTime(15000L);
        assertTrue(outContent.toString().contains("in 15 s"));
    }

}