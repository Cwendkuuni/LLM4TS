import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        options.addOption("d", true, "Option d");

        String[] arguments = {"-a", "-b", "value", "-c", "--", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "value", "-c", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlattenStopAtNonOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        options.addOption("d", true, "Option d");

        String[] arguments = {"-a", "-b", "value", "-c", "--", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "value", "-c"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlattenNoOptions() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlattenNullOption() {
        options.addOption(null, false, "Option a");

        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
}