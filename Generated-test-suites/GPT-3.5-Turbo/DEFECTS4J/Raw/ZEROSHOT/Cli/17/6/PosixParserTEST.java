import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_NoStopAtNonOption() {
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "-b", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_Hyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = {"--option=value"};
        String[] expected = {"--option", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        String[] arguments = {"--option"};
        String[] expected = {"--option"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        String[] arguments = {"-o", "value"};
        String[] expected = {"-o", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithoutValue() {
        String[] arguments = {"-o"};
        String[] expected = {"-o"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOption() {
        String[] arguments = {"-a", "unknown", "-b", "arg2"};
        String[] expected = {"-a", "unknown", "-b", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithValues() {
        String[] arguments = {"-abc", "value1", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithoutValues() {
        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithMixedValues() {
        String[] arguments = {"-abc", "value", "-d"};
        String[] expected = {"-a", "value", "-b", "-c", "-d"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

}