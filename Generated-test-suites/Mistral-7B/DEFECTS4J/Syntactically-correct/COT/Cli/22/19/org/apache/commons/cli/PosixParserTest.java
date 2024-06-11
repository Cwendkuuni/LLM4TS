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
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PosixParser
 */
public class PosixParserTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C with argument");
    }

    @Test
    public void testFlatten_NoOptions()
    {
        String[] arguments = new String[0];
        String[] expected = new String[0];

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption()
    {
        String[] arguments = {"--a"};
        String[] expected = {"--a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArg()
    {
        String[] arguments = {"--a", "value"};
        String[] expected = {"--a", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption()
    {
        String[] arguments = {"--x"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_LongOptionWithArg()
    {
        String[] arguments = {"--long-option", "value"};
        String[] expected = {"--long-option", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions()
    {
        String[] arguments = {"--a", "--b", "--c=3"};
        String[] expected = {"--a", "--b", "--c", "3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption()
    {
        String[] arguments = {"--a", "arg", "--x", "nonOption"};
        String[] expected = {"--a", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_Bursting()
    {
        String[] arguments = {"-a--b--c"};
        String[] expected = {"-a", "--b", "--c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstingWithLongOption()
    {
        String[] arguments = {"-a--long-option"};
        String[] expected = {"-a", "--long-option"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstingWithArg()
    {
        String[] arguments = {"-a--long-option=value"};
        String[] expected = {"-a", "--long-option", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstingWithStopAtNonOption()
    {
        String[] arguments = {"-a--long-option=value", "--nonOption"};
        String[] expected = {"-a", "--long-option=value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstingLongOptionWithArg()
    {
        String[] arguments = {"-a--long-option=value1", "--long-option2=value2"};
        String[] expected = {"-a", "--long-option=value1", "--long-option2=value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstingLongOptionWithArgAndStopAtNonOption()
    {
        String[] arguments = {"-a--long-option=value1", "--long-option2=value2", "--nonOption"};
        String[] expected = {"-a", "--long-option=value1"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testInit()
    {
        PosixParser parser = new PosixParser();
        assertFalse(parser.isInit());

        parser.init();
        assertTrue(parser.isInit());

        parser.init();
        assertFalse(parser.isInit());
    }

    @Test
    public void testProcessNonOptionToken()
    {
        PosixParser parser = new PosixParser();
        String token = "nonOption";

        parser.processNonOptionToken(token);

        assertEquals("--", parser.getCurrentOption());
        assertEquals(token, parser.getNextToken());

        parser.init();
        assertEquals(null, parser.getCurrentOption());
        assertEquals(null, parser.getNextToken());
    }

    @Test
    public void testProcessOptionToken()
    {
        PosixParser parser = new PosixParser();
        String token = "a";

        parser.processOptionToken(token, false);

        assertEquals(token, parser.getCurrentOption().getValue());
        assertEquals(null, parser.getNextToken());

        parser.init();
        assertEquals(null, parser.getCurrentOption());
        assertEquals(null, parser.getNextToken());
    }

    @Test
    public void testBurstToken()
    {
        PosixParser parser = new PosixParser();
        String token = "--long-option";

        parser.burstToken(token, false);

        assertEquals("--long-option", parser.getCurrentOption().getLongOpt());
        assertEquals(null, parser.getNextToken());

        parser.init();
        assertEquals(null, parser.getCurrentOption());
        assertEquals(null, parser.getNextToken());
    }

    @Test
    public void testBurstTokenWithArg()
    {
        PosixParser parser = new PosixParser();
        String token = "--long-option=value";

        parser.burstToken(token, false);

        assertEquals("--long-option", parser.getCurrentOption().getLongOpt());
        assertEquals("value", parser.getNextToken());

        parser.init();
        assertEquals(null, parser.getCurrentOption());
        assertEquals(null, parser.getNextToken());
    }

    @Test
    public void testBurstTokenWithStopAtNonOption()
    {
        PosixParser parser = new PosixParser();
        String token = "--long-option=value";

        parser.burstToken(token, true);

        assertNull(parser.getCurrentOption());
        assertEquals("--nonOption", parser.getNextToken());

        parser.init();
        assertEquals(null, parser.getCurrentOption());
        assertEquals(null, parser.getNextToken());
    }
}