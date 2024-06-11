package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flattening method with basic options.
     */
    @Test
    public void testBasicOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C with two arguments");

        String[] args = {"-a", "--b", "1", "-c", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a", "--b", "1", "-c", "arg1", "arg2"}, parsedArgs);
    }

    /**
     * Test flattening method with long options.
     */
    @Test
    public void testLongOptions() {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long option A");
        options.addOption("long-option-b", 1, "Long option B");

        String[] args = {"--long-option-a", "--long-option-b=value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"--long-option-a", "--long-option-b=value"}, parsedArgs);
    }

    /**
     * Test flattening method with mixed options.
     */
    @Test
    public void testMixedOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C with two arguments");

        String[] args = {"-a", "--long-option-c=arg1 arg2", "1", "-b"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a", "--long-option-c=arg1 arg2", "1", "-b"}, parsedArgs);
    }

    /**
     * Test flattening method with no options.
     */
    @Test
    public void testNoOptions() {
        Options options = new Options();

        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{}, parsedArgs);
    }

    /**
     * Test flattening method with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
    }

    /**
     * Test flattening method with stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"-a", "--no-option", "1", "--b=value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a", "--no-option", "--b=value"}, parsedArgs);
    }
}