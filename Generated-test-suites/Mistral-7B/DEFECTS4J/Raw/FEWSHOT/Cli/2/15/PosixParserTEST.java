package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with arg");
        options.addOption("d", true, "Option D with args");

        String[] arguments = {"-a", "--b", "-c arg", "--d arg1 arg2"};
        String[] expected = {"-a", "-b", "-c", "arg", "--d", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // test with stopAtNonOption
        String[] argumentsWithNonOption = {"-a", "--b", "-c arg", "nonOption"};
        String[] expectedWithNonOption = {"-a", "--b", "-c", "arg", "nonOption"};
        String[] resultWithNonOption = parser.flatten(options, argumentsWithNonOption, true);
        assertArrayEquals(expectedWithNonOption, resultWithNonOption);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-", "-h"};
        parser.flatten(null, arguments, false);
        String[] expected = {"-", "-h"};
        assertArrayEquals(expected, ((PosixParser) parser).tokens.toArray(new String[0]));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        PosixParser parser = new PosixParser();

        // test with valid option
        String[] argumentsA = {"-a"};
        parser.flatten(options, argumentsA, false);
        Option optionA = options.getOption("a");
        assertEquals(optionA, parser.currentOption);

        // test with invalid option and stopAtNonOption
        String[] argumentsInvalidOption = {"-z"};
        parser.init();
        parser.flatten(options, argumentsInvalidOption, true);
        assertNull(parser.currentOption);

        // test with invalid option and stopAtNonOption false
        String[] argumentsInvalidOptionFalse = {"-z", "option"};
        parser.init();
        parser.flatten(options, argumentsInvalidOptionFalse, false);
        assertEquals("-z", ((PosixParser) parser).tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B with arg");
        options.addOption("c", true, "Option C with args");
        PosixParser parser = new PosixParser();

        // test with valid option
        String[] argumentsOptionA = {"-ab"};
        parser.flatten(options, argumentsOptionA, false);
        String[] expectedOptionA = {"-a", "-b"};
        assertArrayEquals(expectedOptionA, ((PosixParser) parser).tokens.toArray(new String[0]));

        // test with option with arg
        String[] argumentsOptionB = {"-ac arg"};
        parser.init();
        parser.flatten(options, argumentsOptionB, false);
        assertEquals("-a", parser.currentOption.getLongOpt());
        assertEquals("arg", parser.currentOption.getValue());

        // test with option with multiple args
        String[] argumentsOptionC = {"-cd arg1 arg2"};
        parser.init();
        parser.flatten(options, argumentsOptionC, false);
        assertEquals("-c", parser.currentOption.getLongOpt());
        assertEquals(new String[]{"arg1", "arg2"}, parser.currentOption.getArgs());

        // test with invalid option and stopAtNonOption
        String[] argumentsInvalidOption = {"-de"};
        parser.init();
        parser.flatten(options, argumentsInvalidOption, true);
        assertEquals("--", ((PosixParser) parser).tokens.get(parser.tokens.size() - 1));
        assertEquals(Arrays.asList("de"), parser.tokens);
    }
}