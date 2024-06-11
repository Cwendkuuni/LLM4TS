import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LagoonCLITest {

    @Test
    public void testMain() {
        String[] args1 = {"property_file", "interval"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args1);
        String expectedOutput1 = "Initializing Lagoon...\nLagoon initialized successfully\n";
        assertEquals(expectedOutput1, outContent.toString());

        String[] args2 = {"property_file"};
        outContent.reset();
        LagoonCLI.main(args2);
        String expectedOutput2 = "Initializing Lagoon...\nLagoon initialized successfully\n";
        assertEquals(expectedOutput2, outContent.toString());

        String[] args3 = {"property_file", "build"};
        outContent.reset();
        LagoonCLI.main(args3);
        String expectedOutput3 = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n";
        assertEquals(expectedOutput3, outContent.toString());

        String[] args4 = {"property_file", "force"};
        outContent.reset();
        LagoonCLI.main(args4);
        String expectedOutput4 = "Initializing Lagoon...\nLagoon initialized successfully\nForce building website...\n";
        assertEquals(expectedOutput4, outContent.toString());

        String[] args5 = {"property_file", "invalid_interval"};
        outContent.reset();
        LagoonCLI.main(args5);
        String expectedOutput5 = "Initializing Lagoon...\nLagoon initialized successfully\n";
        assertEquals(expectedOutput5, outContent.toString());

        String[] args6 = {"property_file", "0"};
        outContent.reset();
        LagoonCLI.main(args6);
        String expectedOutput6 = "Initializing Lagoon...\nLagoon initialized successfully\n";
        assertEquals(expectedOutput6, outContent.toString());

        String[] args7 = {"property_file", "interval"};
        outContent.reset();
        LagoonCLI.main(args7);
        String expectedOutput7 = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n";
        assertEquals(expectedOutput7, outContent.toString());

        String[] args8 = {"property_file", "interval"};
        outContent.reset();
        LagoonCLI.main(args8);
        String expectedOutput8 = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n";
        assertEquals(expectedOutput8, outContent.toString());

        String[] args9 = {"property_file", "interval"};
        outContent.reset();
        LagoonCLI.main(args9);
        String expectedOutput9 = "Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n";
        assertEquals(expectedOutput9, outContent.toString());

        System.setOut(originalOut);
    }

    @Test
    public void testGetProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "url");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemapFile");
        LagoonCLI.properties.setProperty("sourceDir", "sourceDir");

        assertEquals("url", LagoonCLI.getProperty("targetURL"));
        assertEquals("sitemapFile", LagoonCLI.getProperty("sitemapFile"));
        assertEquals("sourceDir", LagoonCLI.getProperty("sourceDir"));

        try {
            LagoonCLI.getProperty("invalidProperty");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertEquals("Property invalidProperty not specified", e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(5000);
        assertEquals("in 5 s\n", outContent.toString());

        outContent.reset();
        LagoonCLI.showTime(500);
        assertEquals("in 500 ms\n", outContent.toString());

        System.setOut(originalOut);
    }
}