import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("h").build());
    }

    @Test
    public void testFlatten_NoArguments() {
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OnlyNonOptionArguments() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "-b", "value", "-h", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "value", "-h"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ContinuesAfterNonOption() {
        String[] arguments = {"-a", "-b", "value", "-h", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "value", "-h", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptionsInOneArgument() {
        String[] arguments = {"-abvalue"};
        String[] expected = {"-a", "-b", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionFormatWithValue() {
        String[] arguments = {"--b=value"};
        String[] expected = {"--b", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionFormatWithNoValue() {
        String[] arguments = {"--h"};
        String[] expected = {"--h"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] arguments = {"--", "arg1", "arg2"};
        String[] expected = {"--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

}