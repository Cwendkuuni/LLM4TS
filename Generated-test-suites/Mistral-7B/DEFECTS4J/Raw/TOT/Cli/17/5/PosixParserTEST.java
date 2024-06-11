package org.apache.commons.cli.test;

import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test case for flattening a simple command line.
     */
    @Test
    public void testSimple() {
        String[] args = {"-a", "--optionB=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, "Option B");
        options.addOption("optionC", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB=valueB", "--optionC=valueC"}, result);
    }

    /**
     * Test case for flattening a command line with stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption() {
        String[] args = {"--optionA=valueA", "nonOption", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        try {
            String[] result = parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // Expected since stopAtNonOption is set
        }

        Assert.assertEquals(3, parser.getTokens().size());
        Assert.assertEquals("--optionA=valueA", parser.getTokens().get(0).toString());
        Assert.assertEquals("nonOption", parser.getTokens().get(1).toString());
        Assert.assertEquals("--optionB=valueB", parser.getTokens().get(2).toString());
    }

    /**
     * Test case for flattening a command line with a long option.
     */
    @Test
    public void testLongOption() {
        String[] args = {"--longOption", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption", "arg1", "arg2"}, result);
    }

    /**
     * Test case for flattening a command line with a short and long option.
     */
    @Test
    public void testShortAndLongOption() {
        String[] args = {"-a", "--optionB=valueB", "-c", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, "Option B");
        options.addOption("optionC", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB=valueB", "-c", "--optionC=valueC"}, result);
    }

    /**
     * Test case for flattening a command line with short and long options having arguments.
     */
    @Test
    public void testShortAndLongOptionWithArguments() {
        String[] args = {"-a", "--optionB=valueB", "-c=valueC", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, true, "Option B");
        options.addOption("optionC", true, "Option C");
        options.addOption("optionD", true, true, "Option D");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB=valueB", "-c=valueC", "--optionD=valueD"}, result);
    }

    /**
     * Test case for flattening a command line with multiple long options.
     */
    @Test
    public void testMultipleLongOptions() {
        String[] args = {"--optionA", "--optionB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");
        options.addOption("optionC", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--optionA", "--optionB", "--optionC=valueC"}, result);
    }

    /**
     * Test case for flattening a command line with complex options.
     */
    @Test
    public void testComplexOptions() {
        String[] args = {"--optionA=valueA", "--optionB=valueB:valueC", "--optionD"};
        Options options = new Options();
        options.addOption("optionA", true, true, "Option A");
        options.addOption("optionB", true, true, "Option B with argument");
        options.addOption("optionD", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--optionA=valueA", "--optionB=valueB:valueC", "--optionD"}, result);
    }

    /**
     * Test case for flattening a command line with a long option having an argument and multiple short options.
     */
    @Test
    public void testLongOptionWithMultipleShortOptions() {
        String[] args = {"--optionB=valueB", "-a", "-c"};
        Options options = new Options();
        options.addOption("optionB", true, true, "Option B with argument");
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--optionB=valueB", "-a", "-c"}, result);
    }

    /**
     * Test case for flattening a command line with a short option having an argument and a long option.
     */
    @Test
    public void testShortOptionWithLongOption() {
        String[] args = {"-a=valueA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("optionB", true, true, "Option B with argument");
        options.addOption("a", true, true, "Option A with argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a=valueA", "--optionB=valueB"}, result);
    }
}