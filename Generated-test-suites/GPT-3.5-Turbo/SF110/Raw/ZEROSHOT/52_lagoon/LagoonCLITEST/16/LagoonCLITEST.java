import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.xml.sax.*;
import java.io.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String[] args1 = {};
        LagoonCLI.main(args1);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        String[] args2 = {"property_file"};
        LagoonCLI.main(args2);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        String[] args3 = {"property_file", "build"};
        LagoonCLI.main(args3);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        String[] args4 = {"property_file", "force"};
        LagoonCLI.main(args4);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        String[] args5 = {"property_file", "invalid"};
        LagoonCLI.main(args5);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        String[] args6 = {"property_file", "500"};
        LagoonCLI.main(args6);
        
        // Write assertion for expected output

        String[] args7 = {"property_file", "-1"};
        LagoonCLI.main(args7);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        String[] args8 = {"sitemap_file"};
        LagoonCLI.main(args8);
        
        // Write assertion for expected output

        String[] args9 = {"sitemap_file", "build"};
        LagoonCLI.main(args9);
        
        // Write assertion for expected output

        String[] args10 = {"sitemap_file", "force"};
        LagoonCLI.main(args10);
        
        // Write assertion for expected output

        String[] args11 = {"sitemap_file", "invalid"};
        LagoonCLI.main(args11);
        
        // Write assertion for expected output

        String[] args12 = {"sitemap_file", "500"};
        LagoonCLI.main(args12);
        
        // Write assertion for expected output

        String[] args13 = {"sitemap_file", "-1"};
        LagoonCLI.main(args13);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());

        System.setOut(originalOut);
    }

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "/path/to/source");
        LagoonCLI.properties = properties;

        try {
            String result1 = LagoonCLI.getProperty("targetURL");
            assertEquals("http://example.com", result1);

            String result2 = LagoonCLI.getProperty("sitemapFile");
            assertEquals("sitemap.xml", result2);

            String result3 = LagoonCLI.getProperty("sourceDir");
            assertEquals("/path/to/source", result3);
        } catch (LagoonException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShowTime() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(500L);
        assertEquals("in 0.5 s\n", outContent.toString());

        LagoonCLI.showTime(10000L);
        assertEquals("in 10 s\n", outContent.toString());

        System.setOut(originalOut);
    }
}