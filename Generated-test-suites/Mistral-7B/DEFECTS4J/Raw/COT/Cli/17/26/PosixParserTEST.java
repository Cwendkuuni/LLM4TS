package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", 1, "Option C");
        options.addOption("d", 2, "Option D");
        options.addOption("e", 3, "Option E");
        parser = new PosixParser();
    }

    /**
     * Test flatten method with valid arguments.
     */
    @Test
    public void testFlattenValidArguments() {
        String[] arguments = {"-a", "-b", "-c", "1", "--d=2", "--e=3", "--"};
        String[] expected = {"-a", "-b", "-c", "-d", "-e"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "-b", "-c", "1", "--d=2", "--e=3", "--foo=bar", "--"};
        String[] expected = {"-a", "-b", "-c", "1", "--d=2", "--e=3", "--foo=bar"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test burstToken method with valid option.
     */
    @Test
    public void testBurstTokenValidOption() {
        String token = "-ac";
        List<String> expected = Arrays.asList("-a", "-c");
        parser.burstToken(token, false);
        List<String> result = (List<String>) parser.tokens;
        Assert.assertEquals(expected, result);
    }

    /**
     * Test burstToken method with invalid option and stopAtNonOption set to true.
     */
    @Test(expected = ParseException.class)
    public void testBurstTokenInvalidOptionStopAtNonOption() {
        String token = "-foo";
        parser.burstToken(token, true);
    }

    /**
     * Test burstToken method with invalid option and stopAtNonOption not set.
     */
    @Test
    public void testBurstTokenInvalidOption() {
        String token = "-foo";
        List<String> expected = Arrays.asList("--", "-foo");
        parser.burstToken(token, false);
        List<String> result = (List<String>) parser.tokens;
        Assert.assertEquals(expected, result);
    }
}