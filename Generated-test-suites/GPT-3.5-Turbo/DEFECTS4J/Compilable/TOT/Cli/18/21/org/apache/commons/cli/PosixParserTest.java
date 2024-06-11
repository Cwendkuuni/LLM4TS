package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_NoOptions_ReturnsEmptyArray() {
        Options options = new Options();
        String[] arguments = new String[0];
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(0, flattened.length);
    }

    @Test
    public void testFlatten_SingleOptionWithNoArgument_ReturnsOptionInArray() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        options.addOption(opt);
        String[] arguments = new String[]{"-a"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(1, flattened.length);
        assertEquals("-a", flattened[0]);
    }

    @Test
    public void testFlatten_SingleOptionWithArgument_ReturnsOptionAndArgumentInArray() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        opt.setArgs(1);
        options.addOption(opt);
        String[] arguments = new String[]{"-a", "value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArguments_ReturnsOptionsAndArgumentsInArray() {
        Options options = new Options();
        Option opt1 = new Option("a", "first option");
        opt1.setArgs(1);
        options.addOption(opt1);
        Option opt2 = new Option("b", "second option");
        opt2.setArgs(1);
        options.addOption(opt2);
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value2", flattened[3]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFlagSet_StopFlatteningAtFirstNonOption() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        options.addOption(opt);
        String[] arguments = new String[]{"-a", "value", "non-option"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_LongOptionWithEqualSign_SplitsOptionAndValueInArray() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        options.addOption(opt);
        String[] arguments = new String[]{"--option=value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("--option", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_SingleHyphenOption_ReturnsOptionInArray() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        options.addOption(opt);
        String[] arguments = new String[]{"-"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(1, flattened.length);
        assertEquals("-", flattened[0]);
    }

    @Test
    public void testFlatten_InvalidOption_IgnoreAndAddToProcessedTokens() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        options.addOption(opt);
        String[] arguments = new String[]{"-x"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(1, flattened.length);
        assertEquals("-x", flattened[0]);
    }

    @Test
    public void testFlatten_ArgumentAfterInvalidOptionWithStopFlag_SetStopFlagAndAddValuesToProcessedTokens() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        options.addOption(opt);
        String[] arguments = new String[]{"-x", "value1", "value2"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals(3, flattened.length);
        assertEquals("-x", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("value2", flattened[2]);
    }

    @Test
    public void testFlatten_ArgumentAfterInvalidOptionWithoutStopFlag_AddOptionToProcessedTokens() {
        Options options = new Options();
        Option opt = new Option("a", "test option");
        options.addOption(opt);
        String[] arguments = new String[]{"-x", "value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("-x", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_MultipleOptionsWithoutArguments_ReturnsOptionsInArray() {
        Options options = new Options();
        Option opt1 = new Option("a", "first option");
        options.addOption(opt1);
        Option opt2 = new Option("b", "second option");
        options.addOption(opt2);
        String[] arguments = new String[]{"-a", "-b"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
    }
}