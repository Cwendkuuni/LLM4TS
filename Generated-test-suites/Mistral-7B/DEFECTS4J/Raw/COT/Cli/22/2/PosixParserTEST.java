package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test for flatten method with no options and no arguments.
     */
    @Test
    public void testFlattenNoOptionsNoArguments()
    {
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    /**
     * Test for flatten method with no options and one argument.
     */
    @Test
    public void testFlattenNoOptionsOneArgument()
    {
        String[] arguments = new String[]{"arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("arg1", result[0]);
    }

    /**
     * Test for flatten method with options and arguments.
     */
    @Test
    public void testFlattenOptionsAndArguments()
    {
        String[] arguments = new String[]{"--option1", "--option2=value2", "--option3", "-o", "--"};
        Options options = new Options();
        options.addOption("option1", true, "o", "opt1");
        options.addOption("option2", true, "o", "opt2");
        options.addOption("option3", true, "o", "opt3");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals("opt1", result[0]);
        assertTrue(result[1].startsWith("--"));
        assertEquals("option2", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("opt3", result[4]);
    }

    /**
     * Test for flatten method with options and arguments with stopAtNonOption.
     */
    @Test
    public void testFlattenOptionsAndArgumentsStopAtNonOption()
    {
        String[] arguments = new String[]{"--option1", "--option2=value2", "--option3", "-o", "nonOption", "--"};
        Options options = new Options();
        options.addOption("option1", true, "o", "opt1");
        options.addOption("option2", true, "o", "opt2");
        options.addOption("option3", true, "o", "opt3");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals("opt1", result[0]);
        assertTrue(result[1].startsWith("--"));
        assertEquals("option2", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("--", result[4]);
        assertEquals("nonOption", result[5]);
    }

    /**
     * Test for burstToken method with a valid option.
     */
    @Test
    public void testBurstTokenValidOption()
    {
        String token = "--option";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o", "opt");
        parser.burstToken(token, false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("opt", parser.tokens.get(1));
    }

    /**
     * Test for burstToken method with an invalid option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenInvalidOption()
    {
        String token = "--invalidOption";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        parser.burstToken(token, false);
    }

    /**
     * Test for burstToken method with stopAtNonOption set.
     */
    @Test
    public void testBurstTokenStopAtNonOption()
    {
        String token = "--option=value--nonOption";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        parser.burstToken(token, true);
        assertEquals(3, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
        assertEquals("--nonOption", parser.tokens.get(2));
    }

    /**
     * Test for burstToken method with an empty token.
     */
    @Test
    public void testBurstTokenEmptyToken()
    {
        String token = "--";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
    }
}