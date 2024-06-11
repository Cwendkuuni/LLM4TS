package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_withNoOptionsAndArguments_shouldReturnEmptyArray() {
        Options options = new Options();
        String[] arguments = new String[]{};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withSingleOptionAndNoArguments_shouldReturnOptionInArray() {
        Option option = new Option("a", false, "description");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_withSingleOptionAndSingleArgument_shouldReturnOptionAndArgumentInArray() {
        Option option = new Option("a", true, "description");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{"value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_withMultipleOptionsAndArguments_shouldReturnOptionsAndArgumentsInArray() {
        Option option1 = new Option("a", false, "description");
        Option option2 = new Option("b", true, "description");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = new String[]{"value1", "-a", "value2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("value1", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlatten_withStopAtNonOptionTrue_shouldStopFlatteningAtNonOptionAndReturnRemainingArguments() {
        Option option = new Option("a", false, "description");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{"value1", "value2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlatten_withStopAtNonOptionFalse_shouldContinueFlatteningAndIgnoreNonOptions() {
        Option option = new Option("a", false, "description");
        Option option2 = new Option("b", false, "description");
        Options options = new Options();
        options.addOption(option);
        options.addOption(option2);
        String[] arguments = new String[]{"value1", "-c", "value2", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("value1", result[0]);
        assertEquals("-c", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("-b", result[3]);
    }

    // Edge Cases

    @Test
    public void testFlatten_withLongOptionButNoEqualsSign_shouldAddOptionToResultWithoutArgument() {
        Option option = new Option("option", false, "description");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{"--option"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlatten_withLongOptionAndEqualsSign_shouldAddOptionToResultWithArgument() {
        Option option = new Option("option", true, "description");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{"--option=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_withBurstableToken_shouldAddBurstedOptionsToResult() {
        Option option = new Option("a", false, "description");
        Option option2 = new Option("b", false, "description");
        Options options = new Options();
        options.addOption(option);
        options.addOption(option2);
        String[] arguments = new String[]{"-ab"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    // Error Scenarios

    @Test
    public void testFlatten_withUnknownOption_shouldIgnoreOptionIfNotStopAtNonOption() {
        Option option = new Option("a", false, "description");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{"-a", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_withUnknownOptionAndStopAtNonOption_shouldReturnRemainingArgumentsIncludingNonOption() {
        Option option = new Option("a", false, "description");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{"-a", "-b", "value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }
}