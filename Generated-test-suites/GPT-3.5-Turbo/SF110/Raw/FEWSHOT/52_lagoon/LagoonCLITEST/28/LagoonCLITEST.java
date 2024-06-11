package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Properties;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.Element;
import org.xml.sax.SAXException;

public class LagoonCLITest {

    private static final String SAMPLE_PROPERTY_FILE = "/path/to/sample/property/file";
    private static final String SAMPLE_SITEMAP_FILE = "/path/to/sample/sitemap/file";

    @Test
    public void testMainWithPropertyFile() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE};
        simulateUserInput("q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainWithSitemapFile() throws Exception {
        String[] args = {SAMPLE_SITEMAP_FILE};
        simulateUserInput("q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainWithBuildFlag() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE, "build"};
        simulateUserInput("q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainWithForceFlag() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE, "force"};
        simulateUserInput("q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainWithInvalidInterval() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE, "invalid_interval"};
        simulateUserInput("q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainWithZeroInterval() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE, "0"};
        simulateUserInput("q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainWithInterval() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE, "10000"};
        simulateUserInput("q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainInteractiveBuild() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE};
        simulateUserInput("b", "q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    @Test
    public void testMainInteractiveForceBuild() throws Exception {
        String[] args = {SAMPLE_PROPERTY_FILE};
        simulateUserInput("f", "q");

        LagoonCLITestUtil.captureSystemOut();
        LagoonCLI.main(args);
        String consoleOutput = LagoonCLITestUtil.getSystemOutContent();

        assertTrue(consoleOutput.contains("Syntax:"));
        assertFalse(consoleOutput.contains("Error while initializing Lagoon"));
        assertFalse(consoleOutput.contains("Lagoon initialized successfully"));
        assertFalse(consoleOutput.contains("Building website..."));
        assertFalse(consoleOutput.contains("Exiting Lagoon..."));
    }

    private void simulateUserInput(String... input) {
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(String.join(System.lineSeparator(), input).getBytes()));
    }

}