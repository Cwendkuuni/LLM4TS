package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "a", false, "Option A");
        options.addOption("b", "b", true, "Option B");
        parser = new PosixParser();
    }

    @Test
    public void testFlattenEmpty() {
        String[] result = parser.flatten(options, new String[0], false);
        assertNull(result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenSingleOption() {
        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
        assertEquals("Option A", parser.getCurrentOption().getLongOpt());
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] args = {"-a", "-b", "--"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenLongOption() {
        String[] args = {"--long-option"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--long-option", result[0]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        options.addOption("l", "long-option", true, "Long option");
        String[] args = {"--long-option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--long-option", result[0]);
        assertEquals("value", result[1]);
        assertSame(options.getOption("l"), parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithArguments() {
        String[] args = {"-a", "arg1", "--", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertNull(parser.getCurrentOption());
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"-a", "-b", "-c", "--"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithBurstedToken() {
        String[] args = {"-ab", "cd"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("cd", result[2]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithBurstedTokenAndValue() {
        options.addOption("ab", "option-ab", true, "Option AB");
        String[] args = {"-ab", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertSame(options.getOption("ab"), parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithBurstedTokenAndMultipleValues() {
        options.addOption("ab", "option-ab", true, "Option AB");
        String[] args = {"-ab", "value1", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("value2", result[3]);
        assertSame(options.getOption("ab"), parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithEmptyArgument() {
        String[] args = {"-a", "", "--"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertNull(result[1]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithEmptyArgumentAndStopAtNonOption() {
        String[] args = {"-a", "", "-b"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertNull(parser.getCurrentOption());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNull() {
        parser.flatten(options, null, false);
    }

    @Test
    public void testReset() {
        String[] args = {"-a", "arg1", "--"};
        parser.flatten(options, args, false);
        parser.init();
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testEatTheRest() {
        String[] args = {"-a", "--"};
        parser.flatten(options, args, false);
        parser.init();
        parser.flatten(options, new String[]{"--", "value"}, false);
        String[] result = parser.flatten(options, new String[]{"--", "another-value"}, false);
        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("value", result[1]);
        assertEquals("another-value", result[2]);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testEatTheRestWithStopAtNonOption() {
        String[] args = {"-a", "--", "-b"};
        parser.flatten(options, args, true);
        String[] result = parser.flatten(options, new String[]{"--", "value"}, false);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("value", result[1]);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testProcessSingleHyphen() {
        parser.processSingleHyphen("-");
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionToken() {
        String[] args = {"-a"};
        parser.init();
        parser.flatten(options, args, false);
        parser.processOptionToken("-a", false);
        assertEquals("Option A", parser.getCurrentOption().getLongOpt());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        String[] args = {"-a", "-b"};
        parser.init();
        parser.flatten(options, args, true);
        parser.processOptionToken("-a", false);
        assertEquals("-b", parser.getCurrentOption());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        String[] args = {"-ab"};
        parser.init();
        parser.flatten(options, args, false);
        parser.burstToken("-ab", false);
        assertEquals("-a", parser.getCurrentOption().getValue());
        assertEquals("-b", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String[] args = {"-ab", "-c"};
        parser.init();
        parser.flatten(options, args, true);
        parser.burstToken("-ab", false);
        assertEquals("-c", parser.getCurrentOption());
        assertNull(parser.getCurrentOption());
    }
}