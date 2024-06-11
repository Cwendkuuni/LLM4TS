package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for PosixParser
 */
public class PosixParserTest
{
    /**
     * Test for flatten method with no arguments and empty options.
     */
    @Test
    public void testEmptyFlatten()
    {
        String[] arguments = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {};
        assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with no arguments and options.
     */
    @Test
    public void testFlattenWithOptions()
    {
        String[] arguments = {};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "--b"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with arguments and empty options.
     */
    @Test
    public void testFlattenWithArguments()
    {
        String[] arguments = {"arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with arguments and options.
     */
    @Test
    public void testFlattenWithArgumentsAndOptions()
    {
        String[] arguments = {"arg1", "--optionA", "arg3", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", 1, "Option B with an integer value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-optionA", "--optionB=valueB", "arg1", "arg3"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenWithStopAtNonOptionTrue()
    {
        String[] arguments = {"--nonOption", "arg1", "--optionA"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"--nonOption", "arg1", "--optionA"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test for burstToken method with a valid option.
     */
    @Test
    public void testBurstTokenValidOption()
    {
        String token = "--optionA";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        List<String> result = parser.burstToken(token, false, options);
        List<String> expected = Arrays.asList("-optionA");
        assertEquals(expected, result);
    }

    /**
     * Test for burstToken method with an invalid option and stopAtNonOption set to true.
     */
    @Test
    public void testBurstTokenInvalidOptionWithStopAtNonOptionTrue()
    {
        String token = "--invalidOption";
        PosixParser parser = new PosixParser();
        Options options = new Options();

        List<String> result = parser.burstToken(token, true, options);
        List<String> expected = Arrays.asList("--invalidOption");
        assertEquals(expected, result);
    }

    /**
     * Test for burstToken method with an invalid option and stopAtNonOption set to false.
     */
    @Test(expected = AssertionError.class)
    public void testBurstTokenInvalidOptionWithStopAtNonOptionFalse()
    {
        String token = "--invalidOption";
        PosixParser parser = new PosixParser();
        Options options = new Options();

        List<String> result = parser.burstToken(token, false, options);
        assertNull(result);
    }

    /**
     * Test for burstToken method with a valid option and an argument.
     */
    @Test
    public void testBurstTokenOptionWithArgument()
    {
        String token = "--optionA=arg";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        List<String> result = parser.burstToken(token, false, options);
        List<String> expected = Arrays.asList("-optionA", "arg");
        assertEquals(expected, result);
    }
}