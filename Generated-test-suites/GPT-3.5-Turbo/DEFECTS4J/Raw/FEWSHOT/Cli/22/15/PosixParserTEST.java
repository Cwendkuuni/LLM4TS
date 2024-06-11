package org.apache.commons.cli;

import org.junit.Assert;
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
        String[] arguments = {"-f", "--foo", "-a", "arg1", "--bar=arg2"};
        String[] expected = {"-f", "--foo", "-a", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        String token = "-f";
        boolean stopAtNonOption = false;

        parser.processOptionToken(token, stopAtNonOption);

        Assert.assertEquals("-f", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption() {
        String token = "-z";
        boolean stopAtNonOption = true;

        parser.processOptionToken(token, stopAtNonOption);

        Assert.assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessNonOptionToken() {
        String value = "value";

        parser.processNonOptionToken(value);

        Assert.assertEquals("--", parser.tokens.get(0));
        Assert.assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);

        Assert.assertEquals("-a", parser.tokens.get(0));
        Assert.assertEquals("-b", parser.tokens.get(1));
        Assert.assertEquals("-c", parser.tokens.get(2));
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        String token = "-abc";
        boolean stopAtNonOption = true;

        parser.burstToken(token, stopAtNonOption);

        Assert.assertEquals("--", parser.tokens.get(0));
        Assert.assertEquals("bc", parser.tokens.get(1));
    }
}