package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "-b", "value1", "--option", "value2", "arg1", "--", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--option", result[3]);
        assertEquals("value2", result[4]);
        assertEquals("arg1", result[5]);
        assertEquals("--", result[6]);
        assertEquals("-c", result[7]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] arguments = {"-a", "-b", "value1", "--option", "value2", "arg1", "--", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--option", result[3]);
        assertEquals("value2", result[4]);
        assertEquals("arg1", result[5]);
    }

    @Test
    public void testFlatten_noTokens() {
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_singleHyphen() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_longOption() {
        String[] arguments = {"--option1", "--option2=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--option1", result[0]);
        assertEquals("--option2", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_invalidOption() {
        String[] arguments = {"-x"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_invalidOption_stopAtNonOption() {
        String[] arguments = {"-x"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_burstToken() {
        String[] arguments = {"-abc"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlatten_burstToken_stopAtNonOption() {
        String[] arguments = {"-abc"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-abc", result[0]);
    }

    @Test
    public void testProcessNonOptionToken() {
        String token = "value";

        parser.processNonOptionToken(token);

        assertTrue(parser.eatTheRest);
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        String token = "-a";
        boolean stopAtNonOption = false;

        parser.processOptionToken(token, stopAtNonOption);

        assertTrue(parser.tokens.contains("-a"));
    }

    @Test
    public void testProcessOptionToken_stopAtNonOption() {
        String token = "-x";
        boolean stopAtNonOption = true;

        parser.processOptionToken(token, stopAtNonOption);

        assertTrue(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);

        assertTrue(parser.tokens.contains("-a"));
        assertTrue(parser.tokens.contains("-b"));
        assertTrue(parser.tokens.contains("-c"));
    }

    @Test
    public void testBurstToken_stopAtNonOption() {
        String token = "-abc";
        boolean stopAtNonOption = true;

        parser.burstToken(token, stopAtNonOption);

        assertTrue(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
    }
}