package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testPosixParser_flatten_simpleOptions() {
        String[] args = {"--foo", "--foo=bar", "-f", "--", "-x"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("x", false, "x");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(6, result.length);
        assertEquals("--foo", result[0]);
        assertNull(result[1]);
        assertEquals("foo", result[2]);
        assertEquals("bar", result[3]);
        assertEquals("--", result[4]);
        assertEquals("x", result[5]);
    }

    @Test
    public void testPosixParser_flatten_longOptions() {
        String[] args = {"--long-option", "--long-option=value"};
        Options options = new Options();
        options.addOption("longOption", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("--long-option", result[0]);
        assertNull(result[1]);
        assertEquals("longOption", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testPosixParser_flatten_singleHyphen() {
        String[] args = {"-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testPosixParser_flatten_multiHyphen() {
        String[] args = {"--", "-hhhh"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("hhhh", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testPosixParser_flatten_optionWithValue() {
        String[] args = {"--foo=bar"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("--foo", result[0]);
        assertNull(result[1]);
        assertEquals("foo", result[2]);
    }

    @Test
    public void testPosixParser_flatten_optionWithMultipleValues() {
        String[] args = {"--foo=bar --foo=baz"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("--foo", result[0]);
        assertNull(result[1]);
        assertEquals("foo", result[2]);
        assertEquals("bar", result[3]);
        assertEquals("baz", result[4]);
    }

    @Test
    public void testPosixParser_flatten_stopAtNonOption() {
        String[] args = {"--foo", "bar"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testPosixParser_flatten_stopAtNonOption_noOptionFound() {
        String[] args = {"--foo", "bar"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("foo", result[1]);
        assertEquals("bar", result[2]);
    }

    @Test
    public void testPosixParser_flatten_burstOption() {
        String[] args = {"--ba-zing"};
        Options options = new Options();
        options.addOption("ba", true, "b");
        options.addOption("zing", true, "z");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("--ba", result[0]);
        assertNull(result[1]);
        assertEquals("ba", result[2]);
        assertEquals("zing", result[3]);
    }

    @Test
    public void testPosixParser_flatten_burstOptionWithValue() {
        String[] args = {"--ba-z--ing=value"};
        Options options = new Options();
        options.addOption("ba", true, "b");
        options.addOption("zing", true, "z");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("--ba--zing", result[0]);
        assertNull(result[1]);
        assertEquals("ba", result[2]);
        assertEquals("zing", result[3]);
        assertEquals("value", result[4]);
    }

    @Test
    public void testPosixParser_flatten_burstOption_stopAtNonOption() {
        String[] args = {"--ba-z--ing", "value"};
        Options options = new Options();
        options.addOption("ba", true, "b");
        options.addOption("zing", true, "z");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("--ba--zing", result[0]);
        assertEquals("value", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testPosixParser_flatten_burstOption_noOptionFound() {
        String[] args = {"--ba-z--ing"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("ba-z--ing", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testPosixParser_flatten_burstOption_noOptionFound_stopAtNonOption() {
        String[] args = {"--ba-z--ing"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("--", result[0]);
        assertEquals("ba-z--ing", result[1]);
        assertNull(result[2]);
        assertEquals("--", result[3]);
        assertNull(result[4]);
    }

    @Test
    public void testPosixParser_burstToken() {
        String token = "--ba-z--ing";
        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken(token, false);

        assertEquals(4, result.length);
        assertEquals("--ba", result[0]);
        assertEquals("--zing", result[1]);
        assertNull(result[2]);
        assertEquals("ba-zing", result[3]);
    }

    @Test
    public void testPosixParser_burstToken_stopAtNonOption() {
        String token = "--ba-z--ing";
        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken(token, true);

        assertEquals(5, result.length);
        assertEquals("--ba--zing", result[0]);
        assertNull(result[1]);
        assertEquals("ba-zing", result[2]);
        assertNull(result[3]);
    }

    @Test
    public void testPosixParser_burstToken_noOptionFound() {
        String token = "--ba-z--ing";
        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken(token, false);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("ba-z--ing", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testPosixParser_burstToken_noOptionFound_stopAtNonOption() {
        String token = "--ba-z--ing";
        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken(token, true);

        assertEquals(5, result.length);
        assertEquals("--", result[0]);
        assertEquals("ba-z--ing", result[1]);
        assertNull(result[2]);
        assertEquals("--", result[3]);
        assertNull(result[4]);
    }

    @Test
    public void testPosixParser_processOptionToken() {
        String token = "--foo";
        PosixParser parser = new PosixParser();

        parser.processOptionToken(token, false);

        assertTrue(parser.currentOption != null);
        assertEquals("foo", parser.currentOption.getValue());
        assertFalse(parser.eatTheRest);

        parser.init();

        parser.processOptionToken(token, true);

        assertEquals(2, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("foo", parser.tokens.get(1));
    }

    @Test
    public void testPosixParser_processOptionToken_noOptionFound() {
        String token = "--foo";
        PosixParser parser = new PosixParser();

        parser.processOptionToken(token, false);

        assertEquals("--foo", parser.tokens.get(0));
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testPosixParser_processOptionToken_noOptionFound_stopAtNonOption() {
        String token = "--foo";
        PosixParser parser = new PosixParser();

        parser.processOptionToken(token, true);

        assertEquals(2, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
    }
}