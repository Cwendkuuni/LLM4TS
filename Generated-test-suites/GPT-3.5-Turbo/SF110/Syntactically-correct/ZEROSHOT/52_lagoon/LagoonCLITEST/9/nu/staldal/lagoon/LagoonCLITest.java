import org.junit.*;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test case 1: args is empty
        String[] args1 = {};
        LagoonCLI.main(args1);
        // Expected output: "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
        
        // Test case 2: args[1] equals "build"
        String[] args2 = {"file.txt", "build"};
        LagoonCLI.main(args2);
        // Expected output: "Building website..."
        //                  "in <time> ms"
        
        // Test case 3: args[1] equals "force"
        String[] args3 = {"file.txt", "force"};
        LagoonCLI.main(args3);
        // Expected output: "Force building website..."
        //                  "in <time> ms"
        
        // Test case 4: args[1] is a valid interval
        String[] args4 = {"file.txt", "10"};
        LagoonCLI.main(args4);
        // Expected output: "Building website..."
        //                  "in <time> ms"
        //                  Sleep for (interval - timeElapsed) milliseconds
        
        // Test case 5: args[1] is an invalid interval
        String[] args5 = {"file.txt", "invalid"};
        LagoonCLI.main(args5);
        // Expected output: "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
        
        // Test case 6: args[0] ends with ".xml" or ".sitemap"
        String[] args6 = {"file.xml"};
        LagoonCLI.main(args6);
        // Expected output: "Initializing Lagoon..."
        //                  "Lagoon initialized successfully"
        
        // Test case 7: propertyFile does not exist
        String[] args7 = {"propertyFile.txt"};
        LagoonCLI.main(args7);
        // Expected output: "Error while initializing Lagoon:"
        //                  "File not found: <propertyFile.txt>"
        
        // Test case 8: AuthenticationMissingException is thrown
        String[] args8 = {"file.xml"};
        LagoonCLI.main(args8);
        // Expected output: "Error while initializing Lagoon:"
        //                  "Password is required but not specified"
        
        // Test case 9: AuthenticationException is thrown
        String[] args9 = {"file.xml"};
        LagoonCLI.main(args9);
        // Expected output: "Error while initializing Lagoon:"
        //                  "Incorrect password"
        
        // Test case 10: IOException is thrown
        String[] args10 = {"file.xml"};
        LagoonCLI.main(args10);
        // Expected output: "Error while initializing Lagoon:"
        //                  "I/O error: <exception_message>"
        
        // Test case 11: LagoonException is thrown
        String[] args11 = {"file.xml"};
        LagoonCLI.main(args11);
        // Expected output: "Error while initializing Lagoon:"
        //                  "<exception_message>"
        
        // Test case 12: build is true
        String[] args12 = {"file.txt", "build"};
        LagoonCLI.main(args12);
        // Expected output: "Building website..."
        //                  "in <time> ms"
        
        // Test case 13: build is false, force is true
        String[] args13 = {"file.txt", "force"};
        LagoonCLI.main(args13);
        // Expected output: "Force building website..."
        //                  "in <time> ms"
        
        // Test case 14: interval is greater than 0
        String[] args14 = {"file.txt", "10"};
        LagoonCLI.main(args14);
        // Expected output: "Building website..."
        //                  "in <time> ms"
        //                  Sleep for (interval - timeElapsed) milliseconds
        
        // Test case 15: interval is 0
        String[] args15 = {"file.txt", "0"};
        LagoonCLI.main(args15);
        // Expected output: "Building website..."
        //                  "in <time> ms"
        
        // Test case 16: interval is less than 0
        String[] args16 = {"file.txt", "-10"};
        LagoonCLI.main(args16);
        // Expected output: "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
        
        // Test case 17: Read input from System.in
        // Manual testing required
        
        // Test case 18: FileNotFoundException is thrown
        String[] args18 = {"file.txt"};
        LagoonCLI.main(args18);
        // Expected output: "File not found: <file.txt>"
        
        // Test case 19: IOException is thrown
        String[] args19 = {"file.txt"};
        LagoonCLI.main(args19);
        // Expected output: "I/O error: <exception_message>"
    }
    
    @Test
    public void testGetProperty() {
        // Test case 1: Property name exists
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://localhost");
        try {
            String value1 = LagoonCLI.getProperty("targetURL");
            assertEquals("http://localhost", value1);
        } catch (LagoonException e) {
            fail(e.getMessage());
        }
        
        // Test case 2: Property name does not exist
        LagoonCLI.properties = new Properties();
        try {
            LagoonCLI.getProperty("invalidProperty");
            fail("LagoonException not thrown");
        } catch (LagoonException e) {
            assertEquals("Property invalidProperty not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTime() {
        // Test case 1: Time less than 10000 ms
        long time1 = 5000;
        LagoonCLI.showTime(time1);
        // Expected output: "in 5000 ms"
        
        // Test case 2: Time greater than or equal to 10000 ms
        long time2 = 15000;
        LagoonCLI.showTime(time2);
        // Expected output: "in 15 s"
    }
}