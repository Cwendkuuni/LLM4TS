import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.io.*;

public class LagoonCLITest {
    
    private LagoonCLI lagoonCLI;
    
    @Before
    public void setUp() {
        lagoonCLI = new LagoonCLI();
    }
    
    @Test
    public void testMainNoArgs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        String[] args = {};
        LagoonCLI.main(args);
        
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainBuild() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        String[] args = {"property_file", "build"};
        LagoonCLI.main(args);
        
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainForce() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        String[] args = {"property_file", "force"};
        LagoonCLI.main(args);
        
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainInvalidInterval() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        String[] args = {"property_file", "abc"};
        LagoonCLI.main(args);
        
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainNegativeInterval() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        String[] args = {"property_file", "-10"};
        LagoonCLI.main(args);
        
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainAuthenticationMissingException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        
        String[] args = {"property_file"};
        LagoonCLI.main(args);
        
        String expectedOutput = "Error while initializing Lagoon:\nPassword is required but not specified\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainAuthenticationException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        
        String[] args = {"property_file"};
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("password", "wrongpassword");
        LagoonCLI.main(args);
        
        String expectedOutput = "Error while initializing Lagoon:\nIncorrect password\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainFileNotFoundException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        
        String[] args = {"nonexistent_file"};
        LagoonCLI.main(args);
        
        String expectedOutput = "Error while initializing Lagoon:\nFile not found: nonexistent_file\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMainIOException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        
        String[] args = {"property_file"};
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "source");
        LagoonCLI.properties.setProperty("password", "password");
        LagoonCLI.DEBUG = true;
        LagoonCLI.main(args);
        
        String expectedOutput = "Error while initializing Lagoon:\nI/O error: java.io.IOException\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testGetProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "source");
        
        try {
            LagoonCLI.getProperty("targetURL");
        } catch (LagoonException e) {
            assertEquals("targetURL", e.getMessage());
        }
    }
    
    @Test
    public void testGetPropertyNull() throws LagoonException {
        LagoonCLI.properties = new Properties();
        
        try {
            LagoonCLI.getProperty("targetURL");
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTimeLessThan10() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        LagoonCLI.showTime(5000L);
        
        String expectedOutput = "in 5000 ms\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testShowTimeMoreThan10() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        LagoonCLI.showTime(15000L);
        
        String expectedOutput = "in 15 s\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
}