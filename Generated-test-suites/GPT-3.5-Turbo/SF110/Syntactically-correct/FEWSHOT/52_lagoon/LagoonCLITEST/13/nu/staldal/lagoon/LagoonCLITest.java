
package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.xtree.Element;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    @Test
    public void testMainNoArgs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(new String[]{});
        String expectedOutput = "Syntax:\r\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\r\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString().trim());
        System.setOut(System.out);
    }

    @Test
    public void testMainBuildArg() {
        // TODO: Implement test case
    }

    @Test
    public void testMainForceArg() {
        // TODO: Implement test case
    }

    @Test
    public void testMainIntervalArg() {
        // TODO: Implement test case
    }

    @Test
    public void testMainInvalidIntervalArg() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(new String[]{"property.xml", "invalid"});
        String expectedOutput = "Syntax:\r\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\r\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString().trim());
        System.setOut(System.out);
    }

    @Test
    public void testMainInvalidIntervalValueArg() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(new String[]{"property.xml", "0"});
        String expectedOutput = "Syntax:\r\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\r\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString().trim());
        System.setOut(System.out);
    }

    @Test
    public void testMainSitemapFileArg() {
        // TODO: Implement test case
    }

    @Test
    public void testMainInvalidSitemapFileArg() {
        // TODO: Implement test case
    }

    @Test
    public void testMainPropertyFileArg() {
        // TODO: Implement test case
    }

    @Test
    public void testMainInvalidPropertyFileArg() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(new String[]{"property.xml"});
        String expectedOutput = "File not found: property.xml";
        assertEquals(expectedOutput, outContent.toString().trim());
        System.setOut(System.out);
    }

    @Test
    public void testMainIOException() throws FileNotFoundException, IOException {
        // TODO: Implement test case
    }

    @Test
    public void testGetProperty() {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "source");
        LagoonCLI.properties.setProperty("password", "password123");
        
        try {
            String result = LagoonCLI.getProperty("targetURL");
            assertEquals("http://example.com", result);
            
            result = LagoonCLI.getProperty("sitemapFile");
            assertEquals("sitemap.xml", result);
            
            result = LagoonCLI.getProperty("sourceDir");
            assertEquals("source", result);
            
            result = LagoonCLI.getProperty("password");
            assertEquals("password123", result);
        } catch(LagoonException e) {
            fail("Unexpected LagoonException occurred.");
        }
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyMissingProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.getProperty("targetURL");
    }

    @Test
    public void testShowTimeUnder1Second() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(500);
        assertEquals("in 500 ms", outContent.toString().trim());
        System.setOut(System.out);
    }

    @Test
    public void testShowTimeOver1Second() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(2000);
        assertEquals("in 2 s", outContent.toString().trim());
        System.setOut(System.out);
    }

}
