package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test cases for PosixParser class
 */
public class PosixParserTest {

    

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = new Options();
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[0], result);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_SingleArg() {
        String[] args = {"arg1"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = new Options();
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"arg1"}, result);
        assertEquals(1, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_SingleDash() {
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = new Options();
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
        assertEquals(1, parser.tokens.size());
        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = new Options();
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
        assertEquals(1, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"-o"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"-o"}, result);
        assertEquals(1, parser.tokens.size());
        assertSame(options.findOption("option1"), parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_SingleOptionWithArg() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "a");
        String[] args = {"-o", "arg1", "-a", "arg2"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"-o", "arg1", "-a", "arg2"}, result);
        assertEquals(4, parser.tokens.size());
        assertSame(options.findOption("option1"), parser.currentOption);
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption_WithArg() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--arg"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        parser.flatten(parser.options, args, false);
    }

    @Test
    public void testFlatten_InvalidOption_WithoutArg() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--arg"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        String[] result = parser.flatten(parser.options, args, true);
        assertArrayEquals(new String[]{"--arg"}, result);
        assertEquals(1, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--long-option"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"--long-option"}, result);
        assertEquals(1, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "a");
        String[] args = {"--long-option", "arg1", "--long-option2", "arg2"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"--long-option", "arg1", "--long-option2", "arg2"}, result);
        assertEquals(4, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_MixedArguments() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--long-option1", "arg1", "-o", "arg2"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        String[] result = parser.flatten(parser.options, args, false);
        assertArrayEquals(new String[]{"--long-option1", "arg1", "-o", "arg2"}, result);
        assertEquals(4, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--long-option1", "arg1", "-o", "arg2", "non-option"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        String[] result = parser.flatten(parser.options, args, true);
        assertArrayEquals(new String[]{"--long-option1", "arg1", "-o", "arg2"}, result);
        assertEquals(3, parser.tokens.size());
        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
    }

    
}