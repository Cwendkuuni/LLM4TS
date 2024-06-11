package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LagoonCLITest {
    @Test
    public void testMain() {
        // Test missing arguments
        LagoonCLI.main(new String[] {});
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outputStream.toString());

        // Test invalid interval format
        LagoonCLI.main(new String[] {"property_file.xml", "non_integer"});
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outputStream.toString());

        // Test invalid interval value
        LagoonCLI.main(new String[] {"property_file.xml", "0"});
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outputStream.toString());
        
        // Test Lagoon initialized successfully
        LagoonCLI.main(new String[] {"property_file.xml", "100"});
        assertTrue(outputStream.toString().contains("Lagoon initialized successfully"));

        // Test build website with no errors
        LagoonCLI.main(new String[] {"property_file.xml", "build"});
        assertTrue(outputStream.toString().contains("Building website..."));
        assertFalse(outputStream.toString().contains("...with errors..."));

        // Test build website with errors
        LagoonCLI.main(new String[] {"property_file.xml", "build"});
        assertTrue(outputStream.toString().contains("Building website..."));
        assertTrue(outputStream.toString().contains("...with errors..."));
        
        // Test force build website with no errors
        LagoonCLI.main(new String[] {"property_file.xml", "force"});
        assertTrue(outputStream.toString().contains("Force building website..."));
        assertFalse(outputStream.toString().contains("...with errors..."));

        // Test force build website with errors
        LagoonCLI.main(new String[] {"property_file.xml", "force"});
        assertTrue(outputStream.toString().contains("Force building website..."));
        assertTrue(outputStream.toString().contains("...with errors..."));

        // Test continuous build website with no errors
        LagoonCLI.main(new String[] {"property_file.xml", "100"});
        assertTrue(outputStream.toString().contains("Building website..."));
        assertFalse(outputStream.toString().contains("...with errors..."));

        // Test exit Lagoon with no errors
        LagoonCLI.main(new String[] {"property_file.xml"});
        assertTrue(outputStream.toString().contains("Exiting Lagoon..."));
    }
}