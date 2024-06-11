import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
    }

    @Test
    public void testFlatten_withNoArguments() {
        String[] arguments = {};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(0, flattened.length);
    }

    @Test
    public void testFlatten_withSingleHyphen() {
        String[] arguments = {"-"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(1, flattened.length);
        assertEquals("-", flattened[0]);
    }

    @Test
    public void testFlatten_withOptionWithoutArgument() {
        String[] arguments = {"-a"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(1, flattened.length);
        assertEquals("-a", flattened[0]);
    }

    @Test
    public void testFlatten_withOptionWithArgument() {
        String[] arguments = {"-b", "value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("-b", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_withOptionWithMissingArgument() {
        String[] arguments = {"-b"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(1, flattened.length);
        assertEquals("-b", flattened[0]);
    }

    @Test
    public void testFlatten_withMultipleOptions() {
        String[] arguments = {"-a", "-b", "value", "-c", "123"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("123", flattened[4]);
    }

    @Test
    public void testFlatten_withStopAtNonOption() {
        String[] arguments = {"-a", "nonOption", "-b", "value"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("nonOption", flattened[1]);
    }

    @Test
    public void testFlatten_withUnknownOptionStopAtNonOption() {
        String[] arguments = {"-a", "-d", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-d", flattened[1]);
    }

    @Test
    public void testFlatten_withRemainingArguments() {
        String[] arguments = {"-a", "nonOption", "-b", "value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("nonOption", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value", flattened[3]);
    }

    @Test
    public void testFlatten_withRemainingArgumentsAndOptionAfter() {
        String[] arguments = {"-a", "nonOption", "-b", "value", "-c"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("nonOption", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value", flattened[3]);
        assertEquals("-c", flattened[4]);
    }

    @Test
    public void testFlatten_withBurstToken() {
        String[] arguments = {"-abc"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("-c", flattened[2]);
    }

}