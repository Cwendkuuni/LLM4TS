package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_EmptyArgs_EmptyArray() {
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArg_SingleElementArray() {
        String[] args = {"arg0"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"arg0"}, result);
    }

    @Test
    public void testFlatten_SingleArgWithOption_TwoElementArray() {
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        String[] args = {"-o", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"o", "value"}, result);
    }

    @Test
    public void testFlatten_MultipleArgs_MultipleElementArray() {
        String[] args = {"arg0", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"arg0", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_DoubleDash_SingleElementArray() {
        String[] args = {"--"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_DoubleDashWithValue_TwoElementArray() {
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        String[] args = {"--opt", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"opt", "value"}, result);
    }

    @Test
    public void testFlatten_EmptyArgsWithStopAtNonOption_EmptyArray() {
        String[] args = {};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArgWithStopAtNonOption_SingleElementArray() {
        String[] args = {"arg0"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"arg0"}, result);
    }

    @Test
    public void testFlatten_SingleArgWithOptionAndStopAtNonOption_TwoElementArray() {
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        String[] args = {"-o", "value"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--", "o", "value"}, result);
    }

    @Test
    public void testFlatten_MultipleArgsWithStopAtNonOption_ThreeElementArray() {
        String[] args = {"arg0", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--", "arg0", "arg1", "arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_ParseException() {
        Option option = new Option("x", "opt", true, "Option description");
        options.addOption(option);
        String[] args = {"-x"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_InvalidOptionWithStopAtNonOption_ThreeElementArray() {
        Option option = new Option("x", "opt", true, "Option description");
        options.addOption(option);
        String[] args = {"-x"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--", "--invalid-option--"}, result);
    }
}