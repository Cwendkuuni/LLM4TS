import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LagoonCLITest {

    // Test for the main method with no arguments
    @Test
    public void testMainNoArgs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.main(new String[]{});
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
        System.setOut(System.out);
    }

    // Test for getProperty method with missing property
    @Test(expected = LagoonException.class)
    public void testGetPropertyMissingProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.getProperty("non_existing_property");
    }

    // Test for showTime method with time less than 10 seconds
    @Test
    public void testShowTimeLessThan10Seconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(5000);
        assertEquals("in 5000 ms\n", outContent.toString());
        System.setOut(System.out);
    }
}

