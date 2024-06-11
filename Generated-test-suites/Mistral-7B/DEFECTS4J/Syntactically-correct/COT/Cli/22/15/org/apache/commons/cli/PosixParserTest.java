package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test case for flatten method with empty options and arguments.
     */
    @Test
    public void testEmpty()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = new String[0];
        String[] expected = new String[0];

        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test case for flatten method with valid options and arguments.
     */
    @Test
    public void testValid()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        String[] arguments = {"--a", "--b", "1", "--c", "3"};
        String[] expected = {"Option A", "Option B", "1", "Option C"};

        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test case for flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testStopAtNonOption()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--a", "nonOption"};
        String[] expected = {"Option A", "nonOption"};

        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test case for flatten method with long options.
     */
    @Test
    public void testLongOptions()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");
        options.addOption("longOption2", 1, "Long Option 2");
        String[] arguments = {"--longOption1", "--longOption2=1"};
        String[] expected = {"Long Option 1", "Long Option 2"};

        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test case for burstToken method with valid option.
     */
    @Test
    public void testBurstTokenValidOption()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String token = "-aB";
        String[] expected = {"Option A", "B"};

        List result = parser.burstToken(token, false);

        String[] actual = (String[]) result.toArray(new String[0]);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test case for burstToken method with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testBurstTokenInvalidOption()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String token = "-invalid";

        parser.burstToken(token, false);
    }

    /**
     * Test case for burstToken method with stopAtNonOption set to true.
     */
    @Test
    public void testBurstTokenStopAtNonOption()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String token = "-aBnonOption";
        String[] expected = {"--", "nonOption"};

        List result = parser.burstToken(token, true);

        String[] actual = (String[]) result.toArray(new String[0]);

        Assert.assertArrayEquals(expected, actual);
    }
}