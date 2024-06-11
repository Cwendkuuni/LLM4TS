
package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LagoonCLITest {
    private static final String nl = System.getProperty("line.separator");

    @Test
    public void testMain() {
        String[] args = {"property_file", "interval"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Initializing Lagoon..." + nl +
                "Lagoon initialized successfully";
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_NoArgs() {
        String[] args = {};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_InvalidInterval() {
        String[] args = {"property_file", "non-integer"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_IntervalLessThanOne() {
        String[] args = {"property_file", "0"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_Build() {
        String[] args = {"property_file", "build"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Initializing Lagoon..." + nl +
                "Lagoon initialized successfully" + nl +
                "Building website..." + nl +
                "...with errors..." + nl;
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_Force() {
        String[] args = {"property_file", "force"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Initializing Lagoon..." + nl +
                "Lagoon initialized successfully" + nl +
                "Force building website..." + nl +
                "...with errors..." + nl;
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_ValidInterval() {
        String[] args = {"property_file", "2"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);
        
        LagoonCLI.main(args);

        String expectedOutput = "Initializing Lagoon..." + nl +
                "Lagoon initialized successfully" + nl +
                "Building website..." + nl +
                "...with errors..." + nl +
                "Building website..." + nl +
                "...with errors..." + nl;
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_ReadyCommandB() {
        String[] args = {"property_file"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Initializing Lagoon..." + nl +
                "Lagoon initialized successfully" + nl +
                "Ready" + nl;
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_ReadyCommandF() {
        String[] args = {"property_file"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Initializing Lagoon..." + nl +
                "Lagoon initialized successfully" + nl +
                "Ready" + nl;
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }

    @Test
    public void testMain_ReadyCommandQ() {
        String[] args = {"property_file"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        LagoonCLI.main(args);

        String expectedOutput = "Initializing Lagoon..." + nl +
                "Lagoon initialized successfully" + nl +
                "Ready" + nl;
        assertEquals(expectedOutput, outContent.toString().trim());

        System.setOut(originalOut);
    }
}
