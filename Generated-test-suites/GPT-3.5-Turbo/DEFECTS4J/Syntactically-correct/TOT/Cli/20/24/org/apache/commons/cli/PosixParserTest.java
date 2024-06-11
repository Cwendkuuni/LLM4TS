import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
        options.addOption(Option.builder("a").argName("arg1").hasArg().desc("Option a").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("Option b").build());
        options.addOption(Option.builder("c").hasArgs().desc("Option c").build());
    }

    @Test
    public void testFlatten_StopAtNonOption_NoNonOptionArguments() {
        String[] arguments = {"-a", "value1", "-b"};
        String[] expected = {"-a", "value1", "-b"};
        String[] result = posixParser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_NonOptionArgumentsExist() {
        String[] arguments = {"-a", "value1", "nonOption"};
        String[] expected = {"-a", "value1", "nonOption"};
        String[] result = posixParser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_NoNonOptionArguments() {
        String[] arguments = {"-a", "value1", "-b"};
        String[] expected = {"-a", "value1", "-b"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_NonOptionArgumentsExist() {
        String[] arguments = {"-a", "value1", "nonOption"};
        String[] expected = {"-a", "value1", "nonOption"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = {"--bar=value1"};
        String[] expected = {"--bar", "value1"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        String[] arguments = {"--bar"};
        String[] expected = {"--bar"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        String[] arguments = {"-a", "value1"};
        String[] expected = {"-a", "value1"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption() {
        String[] arguments = {"-d"};
        String[] expected = {"-d"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstOption() {
        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstOptionWithValue() {
        String[] arguments = {"-acbvalue1"};
        String[] expected = {"-a", "-c", "bvalue1"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}