
package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void testMainPropertyFileBuild() {
        String[] args = {"property_file", "build"};
        LagoonCLI.main(args);
        assertEquals("Initializing Lagoon...\n" +
                "Lagoon initialized successfully\n" +
                "Building website...\n" +
                "...with errors...\n", outContent.toString());
    }

    @Test
    public void testMainPropertyFileForce() {
        String[] args = {"property_file", "force"};
        LagoonCLI.main(args);
        assertEquals("Initializing Lagoon...\n" +
                "Lagoon initialized successfully\n" +
                "Force building website...\n" +
                "...with errors...\n", outContent.toString());
    }

    @Test
    public void testMainPropertyFileInterval() {
        String[] args = {"property_file", "10"};
        LagoonCLI.main(args);
        assertTrue(outContent.toString().startsWith("Initializing Lagoon...\n" +
                "Lagoon initialized successfully\n" +
                "Building website...\n"));
    }

    @Test
    public void testMainPropertyFileInvalidInterval() {
        String[] args = {"property_file", "abc"};
        LagoonCLI.main(args);
        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> " +
                "[<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI " +
                "<sitemap_file> [<interval>|build|force]\n", errContent.toString());
    }
    // ... Add more test scenarios for other methods in LagoonCLI class
}

