import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_WithNoSpecialToken() {
        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "valueA", "-b", "valueB"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithSpecialToken() {
        String[] arguments = {"--option", "value"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"--option", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithSingleHyphenToken() {
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithInvalidOptionAndStopAtNonOptionSet() {
        String[] arguments = {"-c", "value"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithBurstingToken() {
        String[] arguments = {"-abc"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithBurstingTokenAndStopAtNonOptionSet() {
        String[] arguments = {"-abc", "value"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "-b", "-c", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithBurstingTokenAndOptionArg() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");

        String[] arguments = {"-abc", "value"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "c", "value"};
        assertArrayEquals(expected, result);
    }

}