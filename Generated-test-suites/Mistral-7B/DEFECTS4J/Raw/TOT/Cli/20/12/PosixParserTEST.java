package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert;

/**
 * Tests for PosixParser class in Apache Commons CLI.
 */
public class PosixParserTest {

    @Test
    public void testBasicUsage() throws ParseException {
        String[] args = {"--option1", "--option2=value2", "--option3=value3"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", true, "o3")
                .hasArg(true)
                .required(true);

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(args, parsedArgs);
        Assert.assertEquals("o1 is set to true", options.getOptionValue("option1"), Boolean.TRUE);
        Assert.assertEquals("o2 is set to value2", options.getOptionValue("option2"), "value2");
        Assert.assertEquals("o3 is set to value3", options.getOptionValue("option3"), "value3");
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testLongOptionWithValue() throws ParseException {
        String[] args = {"--longOptionValue=value"};
        Options options = new Options();
        options.addOption("longOption", true)
                .longOpt("longOptionValue")
                .hasArg(true)
                .required(true);

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(args, parsedArgs);
        Assert.assertEquals("longOptionValue is set to value", options.getOptionValue("longOptionValue"), "value");
    }

    @Test
    public void testMultipleLongOptionsWithValues() throws ParseException {
        String[] args = {"--longOption1Value1", "--longOption2Value2"};
        Options options = new Options();
        options.addOption("longOption1", true)
                .longOpt("longOption1Value")
                .hasArg(true)
                .required(true);
        options.addOption("longOption2", true)
                .longOpt("longOption2Value")
                .hasArg(true)
                .required(true);

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(args, parsedArgs);
        Assert.assertEquals("longOption1Value is set to value1", options.getOptionValue("longOption1Value"), "value1");
        Assert.assertEquals("longOption2Value is set to value2", options.getOptionValue("longOption2Value"), "value2");
    }

    @Test
    public void testSingleDash() throws ParseException {
        String[] args = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertTrue("Single dash should not be consumed", parsedArgs.length > 0);
        Assert.assertFalse("Single dash should not set any options", options.hasOptions());
    }

    @Test
    public void testDoubleDash() throws ParseException {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertTrue("Double dash should not be consumed", parsedArgs.length == 0);
        Assert.assertEquals("-- should not set any options", 0, options.getOptions().size());
    }

    @Test
    public void testEmptyArguments() throws ParseException {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertTrue("Empty arguments should not be consumed", parsedArgs.length == 0);
        Assert.assertEquals("Empty arguments should not set any options", 0, options.getOptions().size());
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"--option1", "--", "test"};
        Options options = new Options();
        options.addOption("option1", true);
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(new String[]{"--option1", "--"}, parsedArgs);
        Assert.assertEquals("option1 is set to true", options.hasOption("option1"), Boolean.TRUE);
    }

    @Test
    public void testBursting() throws ParseException {
        String[] args = {"--long-option-value-1", "--long-option-value-2"};
        Options options = new Options();
        options.addOption("longOption", true)
                .longOpt("longOptionValue")
                .hasArg(true)
                .required(true);

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(args, parsedArgs);
        Assert.assertEquals("longOptionValue1 is set to long-option-value-1", options.getOptionValue("longOptionValue"), "long-option-value-1long-option-value-2");
    }
}