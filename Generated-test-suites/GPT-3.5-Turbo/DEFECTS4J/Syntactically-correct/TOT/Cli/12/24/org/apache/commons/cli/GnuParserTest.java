import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");

        // Test typical use case
        try {
            String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};
            String[] flattened = parser.flatten(options, arguments, false);
            assertEquals(5, flattened.length);
            assertEquals("-a", flattened[0]);
            assertEquals("-b", flattened[1]);
            assertEquals("value", flattened[2]);
            assertEquals("arg1", flattened[3]);
            assertEquals("arg2", flattened[4]);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Test edge case - empty arguments
        try {
            String[] arguments = {};
            String[] flattened = parser.flatten(options, arguments, false);
            assertEquals(0, flattened.length);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Test error scenario - missing option
        try {
            String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};
            String[] flattened = parser.flatten(options, arguments, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}