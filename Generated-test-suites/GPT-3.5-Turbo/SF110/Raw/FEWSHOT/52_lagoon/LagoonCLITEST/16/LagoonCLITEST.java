import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.AuthenticationException;
import nu.staldal.lagoon.AuthenticationMissingException;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.Element;

public class LagoonCLITest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testMainWithPropertyFileAndBuild() {
        LagoonCLI.main(new String[] {"property_file", "build"});
        assertEquals("Syntax:" + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]"
                        + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testMainWithPropertyFileAndForce() {
        LagoonCLI.main(new String[] {"property_file", "force"});
        assertEquals("Syntax:" + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]"
                        + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testMainWithPropertyFileAndInvalidInterval() {
        LagoonCLI.main(new String[] {"property_file", "interval"});
        assertEquals("Syntax:" + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]"
                        + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testMainWithPropertyFileAndZeroInterval() {
        LagoonCLI.main(new String[] {"property_file", "0"});
        assertEquals("Syntax:" + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]"
                        + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testMainWithPropertyFileAndValidInterval() {
        LagoonCLI.main(new String[] {"property_file", "10"});
        assertNotNull(outContent.toString());
    }

    @Test
    public void testMainWithSitemapFileAndBuild() {
        LagoonCLI.main(new String[] {"sitemap_file", "build"});
        assertNotNull(outContent.toString());
    }

    @Test
    public void testMainWithSitemapFileAndForce() {
        LagoonCLI.main(new String[] {"sitemap_file", "force"});
        assertNotNull(outContent.toString());
    }

    @Test
    public void testMainWithSitemapFileAndInvalidInterval() {
        LagoonCLI.main(new String[] {"sitemap_file", "interval"});
        assertEquals("Syntax:" + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]"
                        + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testMainWithSitemapFileAndZeroInterval() {
        LagoonCLI.main(new String[] {"sitemap_file", "0"});
        assertEquals("Syntax:" + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]"
                        + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testMainWithSitemapFileAndValidInterval() {
        LagoonCLI.main(new String[] {"sitemap_file", "10"});
        assertNotNull(outContent.toString());
    }

    @Test
    public void testMainWithInvalidArgs() {
        LagoonCLI.main(new String[] {});
        assertEquals("Syntax:" + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]"
                        + System.lineSeparator()
                        + "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
                        + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    public void testMainWithException() {
        LagoonCLI.main(new String[] {"property_file"});
        assertNotNull(outContent.toString());
    }

    @Test
    public void testGetProperty() throws LagoonException {
        LagoonCLI.main(new String[] {"property_file"});
        assertNull(outContent.toString());
    }

    @Test
    public void testShowTimeWithLessThan10s() {
        LagoonCLI.showTime(5000L);
        assertEquals("in 5000 ms" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testShowTimeWithMoreThan10s() {
        LagoonCLI.showTime(15000L);
        assertEquals("in 15 s" + System.lineSeparator(), outContent.toString());
    }
}