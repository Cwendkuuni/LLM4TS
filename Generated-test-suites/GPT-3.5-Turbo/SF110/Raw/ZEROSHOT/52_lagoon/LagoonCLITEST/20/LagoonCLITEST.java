import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {
    
    private LagoonCLI lagoonCLI;
    
    @Before
    public void setUp() {
        lagoonCLI = new LagoonCLI();
    }
    
    @Test
    public void testMain() {
        // Test case 1: when args.length < 1
        String[] args1 = new String[0];
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString());
        
        // Test case 2: when args.length > 1 and args[1] equals "build"
        String[] args2 = new String[2];
        args2[1] = "build";
        assertEquals("Building website...", outContent.toString());
        
        // Test case 3: when args.length > 1 and args[1] equals "force"
        String[] args3 = new String[2];
        args3[1] = "force";
        assertEquals("Force building website...", outContent.toString());
        
        // Test case 4: when args.length > 1 and args[1] is an integer
        String[] args4 = new String[2];
        args4[1] = "1000";
        assertEquals("Building website...", outContent.toString());
        
        // Test case 5: when args.length > 1 and args[1] is not an integer
        String[] args5 = new String[2];
        args5[1] = "abc";
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString());
        
        // Test case 6: when interval < 1L
        String[] args6 = new String[2];
        args6[1] = "0";
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString());
        
        // Test case 7: when args[0].endsWith(".xml")
        String[] args7 = new String[1];
        args7[0] = "file.xml";
        assertEquals("Initializing Lagoon...\nLagoon initialized successfully", outContent.toString());
        
        // Test case 8: when args[0].endsWith(".sitemap")
        String[] args8 = new String[1];
        args8[0] = "file.sitemap";
        assertEquals("Initializing Lagoon...\nLagoon initialized successfully", outContent.toString());
        
        // Test case 9: when args[0] is not a valid file
        String[] args9 = new String[1];
        args9[0] = "invalidfile";
        assertEquals("Error while initializing Lagoon:\nFile not found: invalidfile", outContent.toString());
        
        // Test case 10: when exception thrown while initializing Lagoon
        String[] args10 = new String[1];
        args10[0] = "exception";
        assertEquals("Error while initializing Lagoon\nException: Exception message", outContent.toString());
        
        // Test case 11: when build is true
        String[] args11 = new String[2];
        args11[1] = "build";
        assertEquals("Building website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 12: when force is true
        String[] args12 = new String[2];
        args12[1] = "force";
        assertEquals("Force building website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 13: when interval > 0L
        String[] args13 = new String[2];
        args13[1] = "1000";
        assertEquals("Building website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 14: when intervalElapsed < interval
        String[] args14 = new String[2];
        args14[1] = "10000";
        assertEquals("Building website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 15: when intervalElapsed = interval
        String[] args15 = new String[2];
        args15[1] = "1000";
        assertEquals("Building website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 16: when s.length() < 1
        String[] args16 = new String[2];
        args16[1] = "1000";
        assertEquals("Ready\nBuilding website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 17: when c == 'b'
        String[] args17 = new String[2];
        args17[1] = "1000";
        assertEquals("Ready\nBuilding website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 18: when c == 'f'
        String[] args18 = new String[2];
        args18[1] = "1000";
        assertEquals("Ready\nForce building website...\n...with errors...\nin TimeElapsed ms", outContent.toString());
        
        // Test case 19: when c == 'q'
        String[] args19 = new String[2];
        args19[1] = "1000";
        assertEquals("Ready\n", outContent.toString());
    }

    @Test
    public void testGetProperty() {
        // Test case 1: when property is not specified
        try {
            lagoonCLI.getProperty("targetURL");
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
        
        // Test case 2: when property is specified
        try {
            String property = lagoonCLI.getProperty("property1");
            assertEquals("value1", property);
        } catch (LagoonException e) {
            fail("Unexpected LagoonException occurred");
        }
    }
    
    @Test
    public void testShowTime() {
        // Test case 1: when ms < 10000L
        lagoonCLI.showTime(5000L);
        assertEquals("in 5000 ms", outContent.toString());
        
        // Test case 2: when ms >= 10000L
        lagoonCLI.showTime(15000L);
        assertEquals("in 15 s", outContent.toString());
    }

}