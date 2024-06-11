
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        // Test case: Empty arguments
        String[] arguments = {};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(0, flattened.length);

        // Test case: Non-option argument
        arguments = new String[] { "arg1" };
        flattened = parser.flatten(options, arguments, true);
        assertEquals(1, flattened.length);
        assertEquals("arg1", flattened[0]);

        // Test case: Single character option
        options.addOption("a", false, "Option a");
        arguments = new String[] { "-a" };
        flattened = parser.flatten(options, arguments, false);
        assertEquals(1, flattened.length);
        assertEquals("-a", flattened[0]);

        // Test case: Option with parameter
        options.addOption("b", true, "Option b");
        arguments = new String[] { "-b", "value" };
        flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("-b", flattened[0]);
        assertEquals("value", flattened[1]);

        // Test case: Stop at non-option argument
        arguments = new String[] { "-a", "arg1", "-b", "value", "arg2" };
        flattened = parser.flatten(options, arguments, true);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);

        // Test case: Option with "--" separator
        arguments = new String[] { "-a", "--", "-b", "value", "arg2" };
        flattened = parser.flatten(options, arguments, false);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value", flattened[3]);
    }
}
