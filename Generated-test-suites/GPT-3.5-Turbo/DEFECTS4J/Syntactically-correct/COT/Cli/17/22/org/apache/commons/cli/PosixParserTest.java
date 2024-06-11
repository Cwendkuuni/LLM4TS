
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
    public void testFlatten() {
        String[] arguments = {"-a", "-b", "value", "--c=d", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "value", "--c=d", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "-b", "value", "--c=d", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "value", "--c=d"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptions() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptionsStopAtNonOption() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArgs() {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] arguments = {"-a", "value1", "-b", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArgsStopAtNonOption() {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] arguments = {"-a", "value1", "-b", "value2", "arg1"};
        String[] expected = {"-a", "value1", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArgsStopAtNonOptionNoValue() {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] arguments = {"-a", "value1", "-b", "arg1"};
        String[] expected = {"-a", "value1"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenShortOptionArgEquals() {
        options.addOption(Option.builder("a").hasArg().build());
        String[] arguments = {"-a=value"};
        String[] expected = {"-a=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenInvalidOption() {
        String[] arguments = {"-x"};
        String[] expected = {"-x"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}
