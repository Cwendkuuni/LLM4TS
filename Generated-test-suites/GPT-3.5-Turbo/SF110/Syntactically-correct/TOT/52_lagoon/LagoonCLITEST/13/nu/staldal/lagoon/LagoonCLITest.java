import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class LagoonCLITest {
    
    //Test case for main method
    @Test
    public void testMain() {
        // Test case for when args.length < 1
        // When there are no arguments
        String[] args = {};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        System.setOut(originalOut);
        
        // Test case for when args.length > 1 and args[1].equals("build")
        // When the second argument is "build"
        args = new String[]{"property_file.xml", "build"};
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args);
        assertEquals("Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n", outContent.toString());
        System.setOut(originalOut);
        
        // Test case for when args.length > 1 and args[1].equals("force")
        // When the second argument is "force"
        args = new String[]{"property_file.xml", "force"};
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args);
        assertEquals("Initializing Lagoon...\nLagoon initialized successfully\nForce building website...\n", outContent.toString());
        System.setOut(originalOut);
        
        // Test case for when args.length > 1
        // When the second argument is an interval
        args = new String[]{"property_file.xml", "500"};
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(args);
        assertEquals("Initializing Lagoon...\nLagoon initialized successfully\nBuilding website...\n", outContent.toString());
        System.setOut(originalOut);
    }
    
    //Test case for getProperty method
    @Test
    public void testGetProperty() {
        // Test case for when the property is specified
        String value = null;
        try {
            LagoonCLI.properties = new Properties();
            LagoonCLI.properties.setProperty("targetURLTest", "example.com");
            value = LagoonCLI.getProperty("targetURLTest");
        } catch (LagoonException e) {
            fail("Exception should not be thrown.");
        }
        assertEquals("example.com", value);
        
        // Test case for when the property is not specified
        try {
            LagoonCLI.getProperty("invalidProperty");
            fail("Exception should be thrown.");
        } catch (LagoonException e) {
            assertEquals("Property invalidProperty not specified", e.getMessage());
        }
    }
    
    //Test case for showTime method
    @Test
    public void testShowTime() {
        // Test case for less than 10 seconds
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(5000);
        assertEquals("in 5000 ms\n", outContent.toString());
        System.setOut(originalOut);
        
        // Test case for more than 10 seconds
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(15000);
        assertEquals("in 15 s\n", outContent.toString());
        System.setOut(originalOut);
    }
    
    //Add more test cases for the remaining methods
    
}