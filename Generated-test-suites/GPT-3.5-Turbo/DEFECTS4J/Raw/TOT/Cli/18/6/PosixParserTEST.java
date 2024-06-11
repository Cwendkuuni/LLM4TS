import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

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
    public void testFlatten_NoOptions_ReturnsInputArguments() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithOptionWithNoArgument_AddsOptionToTokens() {
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithOptionWithArgument_AddsOptionAndArgumentToTokens() {
        options.addOption("f", true, "Option f");

        String[] arguments = {"-f", "file.txt"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-f", "file.txt"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithOptionWithMultipleArguments_AddsOptionAndArgumentsToTokens() {
        options.addOption(Option.builder("m").hasArgs().valueSeparator(',').build());

        String[] arguments = {"-m", "1,2,3"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-m", "1", "2", "3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_StopFlatteningWhenNonOptionIsFound() {
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "file.txt", "arg1"};
        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "file.txt", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_IgnoreSpecialToken_IgnoreSpecialTokenAndAddRemainingTokens() {
        String[] arguments = {"--", "file.txt", "-a"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"file.txt", "-a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_ExplodeTokenIntoConstituentParts() {
        options.addOption("x", false, "Option x");

        String[] arguments = {"-xyz"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-x", "-y", "-z"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOption_StopFlatteningWhenNonOptionIsFound() {
        options.addOption("x", false, "Option x");

        String[] arguments = {"-xyz", "file.txt"};
        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-x", "-y", "-z", "file.txt"};
        assertArrayEquals(expected, result);
    }

}