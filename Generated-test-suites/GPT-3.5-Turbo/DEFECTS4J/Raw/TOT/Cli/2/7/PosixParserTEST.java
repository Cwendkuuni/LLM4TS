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
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
    }

    @Test
    public void testFlatten_NoArguments_ReturnsEmptyArray() {
        String[] arguments = new String[] {};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_SingleOption_ReturnsOption() {
        String[] arguments = new String[] {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_OptionWithArgument_ReturnsOptionAndArgument() {
        String[] arguments = new String[] {"-b", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOptionFalse_ReturnsOptionAndNonOption() {
        String[] arguments = new String[] {"-d", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-d", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOptionTrue_ReturnsOptionAndNonOptionStop() {
        String[] arguments = new String[] {"-d", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("-d", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_MultipleOptions_ReturnsAllOptions() {
        String[] arguments = new String[] {"-a", "-b", "value", "-c"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
    }

    @Test
    public void testFlatten_LongOptionWithArgument_ReturnsOptionAndArgument() {
        String[] arguments = new String[] {"--option", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithEquals_ReturnsOptionAndValue() {
        String[] arguments = new String[] {"--option=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_MultipleLongOptions_ReturnsAllOptionsAndValues() {
        String[] arguments = new String[] {"--option1=value1", "--option2=value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--option2", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse_ReturnsAllArguments() {
        String[] arguments = new String[] {"-a", "-b", "value", "nonOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue_ReturnsOptionAndStops() {
        String[] arguments = new String[] {"-a", "-b", "value", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsHyphen() {
        String[] arguments = new String[] {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_InvalidOptionSyntax_ReturnsAllTokens() {
        String[] arguments = new String[] {"-abc"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-abc", result[0]);
    }
}