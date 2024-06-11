package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");

        String[] args = {"-a", "--b=value", "-c", "arg1", "arg2", "--", "arg3"};
        String[] expected = {"-a", "--b=value", "-c", "--", "arg3"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);

        // Test with stopAtNonOption = true
        String[] result2 = parser.flatten(options, args, true);
        String[] expected2 = {"-a", "--b=value", "-c", "arg1", "arg2", "--", "arg3"};
        Assert.assertArrayEquals(expected2, result2);
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)}.
     */
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        try {
            parser.processOptionToken("a", false);
            Assert.assertEquals(parser.currentOption, options.getOption("a"));
            parser.init();

            try {
                parser.processOptionToken("x", false);
                Assert.assertNull(parser.currentOption);
            } catch (IllegalArgumentException e) {
                // Expected
            }

            parser.init();
            parser.processOptionToken("b", true);
            Assert.assertEquals(parser.currentOption, options.getOption("b"));
            parser.init();

            String[] args = {"-ab", "--b=value"};
            parser.init();
            parser.flatten(options, args, false);
            Assert.assertEquals(parser.currentOption, options.getOption("b"));
            Assert.assertArrayEquals(new String[]{"--b=value"}, parser.tokens.toArray(new String[0]));
        } catch (ParseException e) {
            // Ignore
        }
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)}.
     */
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        options.addOption("a", true, "Option A");
        options.addOption("bc", 2, "Option BC with two arguments");

        String[] args = {"-abc"};
        String[] expected = {"-a", "--b=c"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);

        // Test with stopAtNonOption = true
        String[] result2 = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"-abc"}, result2);
    }
}