package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("o", "option", false, "An option with no argument.");
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        String[] arguments = {"--arg"};
        String[] expected = {"--arg"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgumentWithValue() {
        options.addOption("a", "arg", true, "An option with a value.");
        String[] arguments = {"--arg", "value"};
        String[] expected = {"--arg", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleArguments() {
        String[] arguments = {"--option", "arg1", "--another", "arg2"};
        String[] expected = {"--option", "arg1", "--another"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"--option", "arg1", "-n", "arg2"};
        String[] expected = {"--option", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphenWithOption() {
        options.addOption("h", "help", false, "Display this help message.");
        String[] arguments = {"-h"};
        String[] expected = {"--help"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_TwoCharacterOption() {
        options.addOption("io", "inputOutput", false, "An option with two characters.");
        String[] arguments = {"--io"};
        String[] expected = {"--io"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithLongName() {
        options.addOption("longOption", "A long option.", false, "A long option.");
        String[] arguments = {"--longOption"};
        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithShortNameAndLongName() {
        options.addOption("l", "longOption", false, "A long option.");
        String[] arguments = {"-l", "--longOption"};
        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        options.addOption("v", "verbose", true, "Print verbose output.");
        String[] arguments = {"-v", "value"};
        String[] expected = {"--verbose", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithMultipleValues() {
        options.addOption("a", "arg", true, "An option that can take multiple values.");
        String[] arguments = {"-a", "value1", "-a", "value2"};
        List<String> expected = new ArrayList<>();
        expected.add("--arg");
        expected.add("value1");
        expected.add("--arg");
        expected.add("value2");
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_TokenWithValue() {
        String[] arguments = {"--arg=value"};
        String[] expected = {"--arg", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] arguments = {"--invalid"};
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testInit() {
        parser.init();
        assertNull(parser.getCurrentOption());
        assertTrue(parser.getTokens().isEmpty());
    }

    @Test
    public void testProcessSingleHyphen() {
        parser.processSingleHyphen("-");
        assertEquals(1, parser.getTokens().size());
        assertEquals("-", parser.getTokens().get(0));
    }

    @Test
    public void testProcessOptionToken() {
        parser.processOptionToken("--option", false);
        assertEquals(1, parser.getTokens().size());
        assertEquals("--option", parser.getTokens().get(0));
        assertNull(parser.getCurrentOption());

        parser.getCurrentOption = new Option("option", "An option.", false, null);
        parser.processOptionToken("--option", false);
        assertEquals(2, parser.getTokens().size());
        assertEquals("--option", parser.getTokens().get(0));
        assertSame(parser.getCurrentOption, parser.getOptions().getOption("option"));
    }

    @Test
    public void testBurstToken_OptionExists() {
        String[] result = parser.burstToken("-o", false);
        assertEquals(1, result.length);
        assertEquals("-o", result[0]);
        assertSame(options.getOption("o"), parser.getCurrentOption());
    }

    @Test
    public void testBurstToken_OptionDoesNotExist() {
        String[] arguments = {"--non-option"};
        String[] result = parser.burstToken("--non-option", false);
        assertEquals(2, result.length);
        assertEquals("--non-option", result[0]);
        assertEquals("--non-option", parser.getTokens().get(parser.getTokens().size() - 1));
    }

    @Test(expected = ParseException.class)
    public void testBurstToken_InvalidOption() {
        String[] arguments = {"--invalid"};
        parser.burstToken("--invalid", false);
    }
}