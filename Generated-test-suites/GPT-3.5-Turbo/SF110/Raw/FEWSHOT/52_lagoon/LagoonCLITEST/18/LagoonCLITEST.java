
package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import nu.staldal.lagoon.core.LagoonProcessor;

import java.io.*;

public class LagoonCLITest {
    private static final String DUMMY_FILE_PATH = "dummyFilePath";
    private LagoonProcessor processor;

    @Before
    public void setUp() throws FileNotFoundException {
        processor = new LagoonProcessor("", null, null, null, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
    }

    @Test
    public void testMain_NoArgs() {
        PrintStream stdout = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            LagoonCLI.main(new String[0]);

            assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", out.toString());
        } finally {
            System.setOut(stdout);
        }
    }

    @Test
    public void testMain_InvalidInterval() {
        PrintStream stdout = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            LagoonCLI.main(new String[]{"property.xml", "invalid"});

            assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", out.toString());
        } finally {
            System.setOut(stdout);
        }
    }

    @Test
    public void testMain_ValidInterval() {
        PrintStream stdout = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            LagoonCLI.main(new String[]{"property.xml", "1"});

            assertEquals("Lagoon initialized successfully\nBuilding website...\nSyntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", out.toString());
        } finally {
            System.setOut(stdout);
        }
    }

    @Test
    public void testMain_BuildFlag() {
        PrintStream stdout = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            LagoonCLI.main(new String[]{"property.xml", "build"});

            assertEquals("Lagoon initialized successfully\nBuilding website...\nSyntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", out.toString());
        } finally {
            System.setOut(stdout);
        }
    }

    @Test
    public void testMain_ForceFlag() {
        PrintStream stdout = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            LagoonCLI.main(new String[]{"property.xml", "force"});

            assertEquals("Lagoon initialized successfully\nForce building website...\nSyntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", out.toString());
        } finally {
            System.setOut(stdout);
        }
    }

    @Test
    public void testMain_SitemapFile() {
        PrintStream stdout = System.out;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            File sitemapFile = new File(DUMMY_FILE_PATH);
            File propertyFile = new File(DUMMY_FILE_PATH + ".xml");
            try {
                sitemapFile.createNewFile();
                propertyFile.createNewFile();
                LagoonCLI.main(new String[]{sitemapFile.getPath(), "interval"});

                assertEquals("Lagoon initialized successfully\nBuilding website...\nSyntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", out.toString());

            } finally {
                sitemapFile.delete();
                propertyFile.delete();
            }

        } finally {
            System.setOut(stdout);
        }
    }

    @Test
    public void testGetTargetURL_SitemapFile() {
        String[] args = new String[]{DUMMY_FILE_PATH + ".sitemap", ""};

        String targetURL = LagoonCLI.getTargetURL(args);
        assertEquals(System.getProperty("user.dir"), targetURL);
    }

    @Test
    public void testGetTargetURL_PropertyFile() {
        String[] args = new String[]{DUMMY_FILE_PATH + ".xml", ""};

        File propertyFile = new File(DUMMY_FILE_PATH + ".xml");
        try {
            propertyFile.createNewFile();
            LagoonCLI.properties = new Properties();
            LagoonCLI.properties.setProperty("targetURL", DUMMY_FILE_PATH);
            String targetURL = LagoonCLI.getTargetURL(args);
            assertEquals(DUMMY_FILE_PATH, targetURL);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            propertyFile.delete();
        }
    }

    @Test
    public void testGetProperty_NotSpecified() {
        LagoonCLI.properties = new Properties();

        try {
            LagoonCLI.getProperty("testProperty");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertEquals("Property testProperty not specified", e.getMessage());
        }
    }

    @Test
    public void testGetProperty_Specified() {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("testProperty", DUMMY_FILE_PATH);

        try {
            String propertyValue = LagoonCLI.getProperty("testProperty");
            assertEquals(DUMMY_FILE_PATH.trim(), propertyValue);
        } catch (LagoonException e) {
            fail("LagoonException thrown");
        }
    }
}
