import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LagoonCLITest {

    // Test case for main method when no arguments are provided
    @Test
    public void testMain_NoArgs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{});

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for main method when property file is specified with build argument
    @Test
    public void testMain_PropertyFile_Build() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"property_file", "build"});

        String expectedOutput = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n...with errors...\nExiting Lagoon...\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for main method when property file is specified with force argument
    @Test
    public void testMain_PropertyFile_Force() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"property_file", "force"});

        String expectedOutput = "Initializing Lagoon...\nLagoon initialized successfully\nForce building website...\n...with errors...\nExiting Lagoon...\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for main method when property file is specified with interval argument
    @Test
    public void testMain_PropertyFile_Interval() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"property_file", "100"});

        String expectedOutput = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n...with errors...\nExiting Lagoon...\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for main method when sitemap file is specified with build argument
    @Test
    public void testMain_SitemapFile_Build() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap_file", "build"});

        String expectedOutput = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n...with errors...\nExiting Lagoon...\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for main method when sitemap file is specified with force argument
    @Test
    public void testMain_SitemapFile_Force() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap_file", "force"});

        String expectedOutput = "Initializing Lagoon...\nLagoon initialized successfully\nForce building website...\n...with errors...\nExiting Lagoon...\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for main method when sitemap file is specified with interval argument
    @Test
    public void testMain_SitemapFile_Interval() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"sitemap_file", "100"});

        String expectedOutput = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n...with errors...\nExiting Lagoon...\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for main method when incorrect property file is specified
    @Test
    public void testMain_IncorrectPropertyFile() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        LagoonCLI.main(new String[]{"incorrect_property_file", "build"});

        String expectedError = "Error while initializing Lagoon:\nFile not found: incorrect_property_file\n";

        assertEquals(expectedError, errContent.toString());

        System.setErr(originalErr);
    }

    // Test case for main method when authentication is missing
    @Test
    public void testMain_AuthenticationMissing() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        LagoonCLI.main(new String[]{"property_file"});

        String expectedError = "Error while initializing Lagoon:\nPassword is required but not specified\n";

        assertEquals(expectedError, errContent.toString());

        System.setErr(originalErr);
    }

    // Test case for getProperty method
    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("testProperty", "testValue");
        LagoonCLI.properties = properties;

        try {
            String result = LagoonCLI.getProperty("testProperty");
            assertEquals("testValue", result);
        } catch (LagoonException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for getProperty method when property is not specified
    @Test
    public void testGetProperty_PropertyNotSpecified() {
        Properties properties = new Properties();
        LagoonCLI.properties = properties;

        try {
            LagoonCLI.getProperty("testProperty");
            fail("LagoonException should be thrown");
        } catch (LagoonException e) {
            String expectedMessage = "Property testProperty not specified";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    // Test case for showTime method when time is less than 10 seconds
    @Test
    public void testShowTime_LessThan10Seconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(5000);

        String expectedOutput = "in 5000 ms\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    // Test case for showTime method when time is more than 10 seconds
    @Test
    public void testShowTime_MoreThan10Seconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(15000);

        String expectedOutput = "in 15 s\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }
}