package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        Options options = new Options();
        String[] expected = {};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_BasicOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");

        String[] arguments = {"-a", "--b=value", "-c", "arg1", "arg2"};
        String[] expected = {"-a", "--b=value", "-c", "arg1", "arg2"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] arguments = {"-a", "--no-option", "-b=value"};
        String[] expected = {"-a", "--no-option", "--", "-b=value"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testProcess_SimpleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a"};

        parser.init();
        parser.flatten(options, arguments, false);

        assertTrue(parser.tokens.contains("-a"));
        assertEquals(null, parser.currentOption);
    }

    @Test
    public void testProcess_OptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("b", 1, "Option B with an argument");
        String[] arguments = {"-b", "value"};

        parser.init();
        parser.flatten(options, arguments, false);

        assertTrue(parser.tokens.contains("-b"));
        assertEquals("value", parser.currentOption.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testProcess_UnknownOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-x"};

        parser.init();
        parser.flatten(options, arguments, false);

        fail("Expected an AssertionError due to an unknown option");
    }

    @Test
    public void testProcessOptionToken_ValidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a"};

        parser.init();
        parser.processOptionToken("-a", false);

        assertTrue(parser.tokens.contains("-a"));
        assertEquals(options.getOption("a"), parser.currentOption);
    }

    @Test
    public void testProcessOptionToken_InvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-x"};

        parser.init();
        parser.processOptionToken("-x", false);

        assertTrue(parser.tokens.contains("--"));
        assertEquals(0, parser.tokens.size() - 1);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken_SimpleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--a"};

        parser.init();
        parser.burstToken("--a", false);

        assertTrue(parser.tokens.contains("--a"));
        assertEquals(null, parser.currentOption);
    }

    @Test
    public void testBurstToken_OptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("b", 1, "Option B with an argument");
        String[] arguments = {"--b=value"};

        parser.init();
        parser.burstToken("--b=value", false);

        assertTrue(parser.tokens.contains("-b"));
        assertTrue(parser.tokens.contains("value"));
        assertEquals(options.getOption("b"), parser.currentOption);
    }

    @Test
    public void testBurstToken_UnknownOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--x"};

        parser.init();
        parser.burstToken("--x", false);

        assertTrue(parser.tokens.contains("--"));
        assertEquals(0, parser.tokens.size() - 1);
        assertNull(parser.currentOption);
    }
}