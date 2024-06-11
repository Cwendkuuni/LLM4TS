
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        // Initialize test data
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = new String[]{"-a", "value", "--", "nonOption"};

        GnuParser parser = new GnuParser();

        // Test the flatten method
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(result.length, 3);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "value");
        assertEquals(result[2], "--");
    }
}
