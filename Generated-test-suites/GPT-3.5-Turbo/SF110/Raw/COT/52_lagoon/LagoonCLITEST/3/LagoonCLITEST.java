import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        String[] args = new String[]{"property_file", "build"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args);
        assertEquals("Building website...\n...with errors...\n", outContent.toString());
    }
    
    @Test
    public void testMainNoArgs() {
        String[] args = new String[]{};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }
    
    @Test
    public void testMainInvalidArgs() {
        String[] args = new String[]{"property_file", "invalid"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testGetProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "src");
        String result = LagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class) 
    public void testGetPropertyNull() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.getProperty("targetURL");
    }

    @Test(expected = LagoonException.class) 
    public void testGetPropertyEmpty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "");
        LagoonCLI.getProperty("targetURL");
    }

    @Test
    public void testShowTimeBelow10s() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(5000);
        assertEquals("in 5000 ms\n", outContent.toString());
    }

    @Test
    public void testShowTimeAbove10s() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(15000);
        assertEquals("in 15 s\n", outContent.toString());
    }
}