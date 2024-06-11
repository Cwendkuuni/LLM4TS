
import org.junit.*;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test case for when args.length < 1
        String[] args = new String[]{};
        LagoonCLI.main(args);
        // Assert that the output is equal to the syntax message
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString());

        // Test case for when args[1] equals "build"
        args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        // Assert that the output is "Building website..."
        assertEquals("Building website...", outContent.toString());

        // Test case for when args[1] equals "force"
        args = new String[]{"property_file.xml", "force"};
        LagoonCLI.main(args);
        // Assert that the output is "Force building website..."
        assertEquals("Force building website...", outContent.toString());

        // Test case for when args[1] is a valid interval
        args = new String[]{"property_file.xml", "100"};
        LagoonCLI.main(args);
        // Assert that the output is "Building website..." followed by "in X ms"
        assertTrue(outContent.toString().contains("Building website..."));

        // Test case for when args[1] is an invalid interval
        args = new String[]{"property_file.xml", "abc"};
        LagoonCLI.main(args);
        // Assert that the output is equal to the syntax message
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString());

        // Test case for when args[1] is less than 1L
        args = new String[]{"property_file.xml", "0"};
        LagoonCLI.main(args);
        // Assert that the output is equal to the syntax message
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", outContent.toString());

        // Additional test cases can be added to cover more scenarios
    }

    @Test
    public void testGetProperty() {
        // Test case for when the property is not specified
        // ...

        // Test case for when the property is specified
        // ...

        // Additional test cases can be added to cover more scenarios
    }

    // Additional test methods can be added to cover the remaining public methods in the LagoonCLI class

}

