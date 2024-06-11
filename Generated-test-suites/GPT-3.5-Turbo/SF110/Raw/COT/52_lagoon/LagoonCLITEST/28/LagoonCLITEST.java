package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

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
    public void testMain() {
        String[] args1 = {"property_file", "interval"};
        LagoonCLI.main(args1);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        
        String[] args2 = {"property_file", "build"};
        LagoonCLI.main(args2);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        
        String[] args3 = {"property_file", "force"};
        LagoonCLI.main(args3);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        
        String[] args4 = {"property_file", "invalid"};
        LagoonCLI.main(args4);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        
        String[] args5 = {"property_file", "0"};
        LagoonCLI.main(args5);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        
        String[] args6 = {"property_file"};
        LagoonCLI.main(args6);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        
        String[] args7 = {"property_file", "1000"};
        LagoonCLI.main(args7);
        assertEquals("Initializing Lagoon...\nLagoon initialized successfully\n", outContent.toString());
        
        //Add more test cases that cover other branches of the main() method        
    }
    
    @Test
    public void testGetProperty() throws LagoonException {
        //Initialize properties
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "src");
        
        //Set properties in LagoonCLI class
        LagoonCLI.properties = properties;
        
        //Test getProperty() method
        String result1 = LagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result1);
        
        String result2 = LagoonCLI.getProperty("sourceDir");
        assertEquals("src", result2);
        
        //Test exception when property is not specified
        try {
            LagoonCLI.getProperty("invalidProperty");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertEquals("Property invalidProperty not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTime() {
        //Test showTime() method for ms less than 10000
        LagoonCLI.showTime(5000);
        assertEquals("in 5000 ms\n", outContent.toString());
        
        //Test showTime() method for ms greater than or equal to 10000
        LagoonCLI.showTime(15000);
        assertEquals("in 15 s\n", outContent.toString());
    }
}