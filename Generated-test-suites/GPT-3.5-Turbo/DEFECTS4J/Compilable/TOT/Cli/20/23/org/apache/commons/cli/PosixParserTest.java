package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        arguments = new String[0];
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Option option = new Option("foo", true, "option with value");
        options.addOption(option);
        arguments = new String[]{"--foo=bar"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithNoValue() {
        Option option = new Option("foo", false, "option with no value");
        options.addOption(option);
        arguments = new String[]{"--foo"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        arguments = new String[]{"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionWithNoValue() {
        Option option = new Option("f", false, "option with no value");
        options.addOption(option);
        arguments = new String[]{"-f"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        Option option = new Option("f", true, "option with value");
        options.addOption(option);
        arguments = new String[]{"-f", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_OptionWithMultipleArgs() {
        Option option = new Option("o", true, "option with multiple args");
        options.addOption(option);
        arguments = new String[]{"-o", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        arguments = new String[]{"-f", "value", "non-option"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("non-option", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_WithRemainingOptions() {
        Option option = new Option("o", true, "option with value");
        options.addOption(option);
        arguments = new String[]{"-o", "value", "non-option"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value", result[1]);
        assertEquals("non-option", result[2]);
    }

    @Test
    public void testFlatten_BurstToken() {
        Option option = new Option("o", true, "option with value");
        options.addOption(option);
        arguments = new String[]{"-opt"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-opt", result[0]);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        arguments = new String[]{"-opt", "non-option"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-opt", result[0]);
        assertEquals("non-option", result[1]);
    }

}