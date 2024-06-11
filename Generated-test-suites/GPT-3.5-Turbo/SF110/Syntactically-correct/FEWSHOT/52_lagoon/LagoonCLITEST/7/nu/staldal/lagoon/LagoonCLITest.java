package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class LagoonCLITest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testMainWithPropertyFile() {
        String[] args = {"property_file", "interval"};
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "Property targetURL not specified\n", outContent.toString());

        args[1] = "build";
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "Property targetURL not specified\n", outContent.toString());

        args[1] = "force";
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "Property targetURL not specified\n", outContent.toString());
    }

    @Test
    public void testMainWithSitemapFile() {
        String[] args = {"sitemap_file", "interval"};
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "File not found: sitemap_file\n", outContent.toString());

        args[1] = "build";
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "File not found: sitemap_file\n", outContent.toString());

        args[1] = "force";
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "File not found: sitemap_file\n", outContent.toString());
    }

    @Test
    public void testMainWithInvalidInterval() {
        String[] args = {"property_file", "invalid_interval"};
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        args = new String[]{"sitemap_file", "invalid_interval"};
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testMainWithValidInterval() {
        String[] args = {"property_file", "1"};
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "Property targetURL not specified\n", outContent.toString());

        args = new String[]{"sitemap_file", "1"};
        LagoonCLI.main(args);

        assertEquals("Initializing Lagoon...\n" +
                "Error while initializing Lagoon:\n" +
                "File not found: sitemap_file\n", outContent.toString());
    } 
}