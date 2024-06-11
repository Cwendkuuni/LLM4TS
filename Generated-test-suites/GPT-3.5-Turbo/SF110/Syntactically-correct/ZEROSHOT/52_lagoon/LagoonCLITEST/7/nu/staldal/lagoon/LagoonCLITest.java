import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // test case where args.length < 1
        // expected output: "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
        LagoonCLI.main(new String[]{});
        
        // test case where args.length > 1 and args[1] equals "build"
        // expected output: "Building website..." and showTime() will be called
        LagoonCLI.main(new String[]{"property_file", "build"});
        
        // test case where args.length > 1 and args[1] equals "force"
        // expected output: "Force building website..." and showTime() will be called
        LagoonCLI.main(new String[]{"property_file", "force"});
        
        // test case where args.length > 1 and args[1] is a valid interval
        // expected output: "Building website..." and showTime() will be called and the program will sleep for (interval - timeElapsed) milliseconds each iteration        
        LagoonCLI.main(new String[]{"property_file", "1000"});
        
        // test case where args[0] ends with ".xml" or ".sitemap"
        // expected output: sitemapTree will be assigned and LagoonProcessor will be initialized
        LagoonCLI.main(new String[]{"sitemap_file"});
        
        // test case where neither args[0] ends with ".xml" or ".sitemap" nor args[0] is a valid property file
        // expected output: print error message "File not found: <file_name>"
        LagoonCLI.main(new String[]{"invalid_file"});        
    }
    
    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "src");
        properties.setProperty("password", "password123");
        LagoonCLI.properties = properties;
        
        try {
            // test case where the property name is valid
            // expected output: return the value of the property with the specified name
            assertEquals("http://example.com", LagoonCLI.getProperty("targetURL"));
            
            // test case where the property name is not specified in the properties
            // expected output: throw a LagoonException with the error message "Property <property_name> not specified"
            LagoonCLI.getProperty("invalidProperty");
        } catch (LagoonException e) {
            assertEquals("Property invalidProperty not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTime() {
        // test case where ms < 10000
        // expected output: print "in <ms> ms"
        LagoonCLI.showTime(5000L);
        
        // test case where ms >= 10000
        // expected output: print "in <ms/1000> s"
        LagoonCLI.showTime(15000L);
    }
}