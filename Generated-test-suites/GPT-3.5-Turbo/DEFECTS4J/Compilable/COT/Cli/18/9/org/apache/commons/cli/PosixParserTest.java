package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse_ReturnsFlattenedArguments() {
        String[] arguments = {"-a", "-b", "value", "-c"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-b", "value", "-c"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue_ReturnsFlattenedArgumentsUntilNonOption() {
        String[] arguments = {"-a", "-b", "value", "-c"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "-b", "value"}, result);
    }

    @Test
    public void testFlatten_WithLongOption_ReturnsFlattenedArgumentsWithLongOption() {
        options.addOption(new Option("long", false, "Long option"));

        String[] arguments = {"-a", "--long"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "--long"}, result);
    }

    @Test
    public void testFlatten_WithOptionAndArgument_ReturnsFlattenedArgumentsWithOptionAndArgument() {
        options.addOption(OptionBuilder.withArgName("arg").hasArg().create("opt"));

        String[] arguments = {"-a", "-opt", "value"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-opt", "value"}, result);
    }

    @Test
    public void testFlatten_WithOptionAndMultipleArguments_ReturnsFlattenedArgumentsWithOptionAndMultipleArguments() {
        options.addOption(OptionBuilder.withArgName("args").hasArgs().create("opt"));

        String[] arguments = {"-a", "-opt", "value1", "value2"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-opt", "value1", "value2"}, result);
    }

    @Test
    public void testFlatten_WithNonExistingOptionAndStopAtNonOptionFalse_ReturnsAllArguments() {
        String[] arguments = {"-a", "-nonExisting", "-b"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-nonExisting", "-b"}, result);
    }

    @Test
    public void testFlatten_WithNonExistingOptionAndStopAtNonOptionTrue_ReturnsAllArguments() {
        String[] arguments = {"-a", "-nonExisting", "-b"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "-nonExisting", "-b"}, result);
    }

    @Test
    public void testFlatten_WithNonExistingOptionAndStopAtNonOptionFalse_ReturnsAllArgumentsIncludingRemainingTokens() {
        String[] arguments = {"-a", "-nonExisting", "-b"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-nonExisting", "-b"}, result);
    }

    @Test
    public void testFlatten_WithNonExistingOptionAndStopAtNonOptionTrue_ReturnsAllArgumentsIncludingRemainingTokens() {
        String[] arguments = {"-a", "-nonExisting", "-b"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "-nonExisting", "-b"}, result);
    }

    @Test
    public void testFlatten_WithMultipleCharactersOption_ReturnsFlattenedArgumentsWithOption() {
        options.addOption(new Option("opt", false, "Option"));

        String[] arguments = {"-a", "-opt"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-opt"}, result);
    }

    @Test
    public void testFlatten_WithEmptyString_ReturnsEmptyArray() {
        String[] arguments = {""};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_WithSingleHyphen_ReturnsSingleHyphenAsArgument() {
        String[] arguments = {"-"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_WithSingleCharacterOptionAndArgument_ReturnsFlattenedArgumentsWithOptionAndArgument() {
        options.addOption(OptionBuilder.withArgName("arg").hasArg().create("o"));

        String[] arguments = {"-a", "-o", "value"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-o", "value"}, result);
    }
}