import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;
    
    public PosixParserTest() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "-b", "--option1", "value1", "-c", "value2", "arg1"};
        String[] expected = {"-a", "-b", "--option1", "value1", "-c", "value2", "arg1"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "-b", "--option1", "value1", "-c", "value2", "arg1"};
        String[] expected = {"-a", "-b", "--option1", "value1"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithoutArgument() {
        String[] arguments = {"-a", "-b", "--option1", "value1"};
        String[] expected = {"-a", "-b", "--option1", "value1"};

        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("option1", false, "");

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithArgument() {
        String[] arguments = {"-a", "value1", "-b", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};

        options.addOption("a", true, "");
        options.addOption("b", true, "");

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithMultipleArguments() {
        String[] arguments = {"-a", "value1", "value2"};
        String[] expected = {"-a", "value1", "value2"};

        Option option = new Option("a", true, "");
        option.setArgs(2);
        options.addOption(option);

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionBursting() {
        String[] arguments = {"-abc", "value"};
        String[] expected = {"-a", "-b", "-c", "value"};

        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("c", true, "");

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithUnknownOptionNoStopAtNonOption() {
        String[] arguments = {"-a", "-b", "-x", "value"};
        String[] expected = {"-a", "-b", "-x", "value"};

        options.addOption("a", false, "");
        options.addOption("b", false, "");

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithUnknownOptionStopAtNonOption() {
        String[] arguments = {"-a", "-b", "-x", "value"};
        String[] expected = {"-a", "-b"};

        options.addOption("a", false, "");
        options.addOption("b", false, "");

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithHyphen() {
        String[] arguments = {"-a", "-", "-b"};
        String[] expected = {"-a", "-", "-b"};

        options.addOption("a", false, "");
        options.addOption("b", false, "");

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}