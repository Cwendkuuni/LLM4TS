package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.ParsePosition;
import org.junit.Test;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten_NoOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(null, args, false));
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "arg"};
        String[] expected = {"--option", "arg"};
        assertArrayEquals(expected, parser.flatten(null, args, false));
    }

    @Test
    public void testFlatten_OptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option", "value"};
        String[] expected = {"--option", "value"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option2", 2, "o2");
        String[] args = {"--option=value1", "--option2=value2"};
        String[] expected = {"--option=value1", "--option2=value2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_OptionWithArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", 2, "o");
        String[] args = {"--option=value1", "value2"};
        String[] expected = {"--option=value1", "--option=value2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "arg1", "nonOption"};
        String[] expected = {"--option", "arg1"};
        assertArrayEquals(expected, parser.flatten(null, args, true));
    }

    @Test
    public void testFlatten_LongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", true, "l");
        String[] args = {"--long-option", "value"};
        String[] expected = {"--long-option", "value"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", true, "l");
        String[] args = {"--long-option=value"};
        String[] expected = {"--long-option=value"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", 1, "l");
        String[] args = {"--long-option=value1", "value2"};
        String[] expected = {"--long-option=value1", "--long-option=value2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_LongOptionWithArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", 2, "l");
        String[] args = {"--long-option=value1", "value2"};
        String[] expected = {"--long-option=value1", "--long-option=value2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--invalid", "option"};
        parser.flatten(options, args, false);
    }

    @Test(expected = NullPointerException.class)
    public void testFlatten_NullOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "arg"};
        parser.flatten(null, args, false);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFlatten_InvalidArgumentIndex() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "arg"};
        parser.flatten(null, args, false);
        parser.tokens.remove(parser.tokens.size() - 1);
        parser.flatten(null, args, false);
    }

    @Test
    public void testBurstToken_SingleOption() {
        PosixParser parser = new PosixParser();
        String token = "--option";
        List tokens = parser.burstToken(token, false);
        assertEquals(1, tokens.size());
        assertEquals("--option", tokens.get(0).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken_OptionWithValue() {
        PosixParser parser = new PosixParser();
        String token = "--option=value";
        List tokens = parser.burstToken(token, false);
        assertEquals(2, tokens.size());
        assertEquals("--option", tokens.get(0).toString());
        assertEquals("value", tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken_OptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", 1, "o");
        String token = "--option=value";
        List tokens = parser.burstToken(token, false);
        assertEquals(2, tokens.size());
        assertEquals("--option", tokens.get(0).toString());
        assertEquals("value", tokens.get(1).toString());
        assertSame(options.getOption("option"), parser.currentOption);
    }

    @Test
    public void testBurstToken_OptionWithArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", 2, "o");
        String token = "--option=value1 value2";
        List tokens = parser.burstToken(token, false);
        assertEquals(4, tokens.size());
        assertEquals("--option", tokens.get(0).toString());
        assertEquals("value1", tokens.get(1).toString());
        assertEquals("value2", tokens.get(2).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--option=value nonOption";
        List tokens = parser.burstToken(token, true);
        assertEquals(2, tokens.size());
        assertEquals("--option=value", tokens.get(0).toString());
        assertEquals("nonOption", tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken_InvalidOption() {
        PosixParser parser = new PosixParser();
        String token = "--invalid";
        List tokens = parser.burstToken(token, false);
        assertEquals(1, tokens.size());
        assertEquals("--invalid", tokens.get(0).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0).toString());
    }

    @Test
    public void testProcess_SingleOption() {
        PosixParser parser = new PosixParser();
        parser.process("--option");
        assertEquals(1, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess_OptionWithValue() {
        PosixParser parser = new PosixParser();
        parser.process("--option=value");
        assertEquals(2, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0).toString());
        assertEquals("value", parser.tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess_OptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o");
        parser.process("--option");
        assertEquals(1, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0).toString());
        assertSame(options.getOption("option"), parser.currentOption);
    }

    @Test
    public void testProcess_LongOption() {
        PosixParser parser = new PosixParser();
        parser.process("--long-option");
        assertEquals(1, parser.tokens.size());
        assertEquals("--long-option", parser.tokens.get(0).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", true, "l");
        parser.process("--long-option=value");
        assertEquals(2, parser.tokens.size());
        assertEquals("--long-option", parser.tokens.get(0).toString());
        assertEquals("value", parser.tokens.get(1).toString());
        assertSame(options.getOption("long-option"), parser.currentOption);
    }

    @Test
    public void testProcess_OptionWithArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", 1, "o");
        parser.process("--option value1 value2");
        assertEquals(4, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0).toString());
        assertEquals("value1", parser.tokens.get(1).toString());
        assertEquals("value2", parser.tokens.get(2).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess_LongOptionWithArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", 2, "l");
        parser.process("--long-option value1 value2");
        assertEquals(6, parser.tokens.size());
        assertEquals("--long-option", parser.tokens.get(0).toString());
        assertEquals("value1", parser.tokens.get(1).toString());
        assertEquals("value2", parser.tokens.get(2).toString());
        assertEquals("--long-option", parser.tokens.get(3).toString());
        assertEquals("value1", parser.tokens.get(4).toString());
        assertEquals("value2", parser.tokens.get(5).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess_NonOption() {
        PosixParser parser = new PosixParser();
        parser.process("nonOption");
        assertEquals(1, parser.tokens.size());
        assertEquals("nonOption", parser.tokens.get(0).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.process("option nonOption");
        assertEquals(2, parser.tokens.size());
        assertEquals("option", parser.tokens.get(0).toString());
        assertEquals("nonOption", parser.tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    
}