package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithDefaults() {
        Option option = OptionBuilder.create();
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertNull(option.getArgName());
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .create();
        Assert.assertEquals("myLongOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderWithShortOpt() {
        Option option = OptionBuilder.create('m')
                .create();
        Assert.assertEquals("m", option.getOpt());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        Option option = OptionBuilder.withDescription("My description")
                .create();
        Assert.assertEquals("My description", option.getDescription());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        Option option = OptionBuilder.isRequired()
                .create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArg() {
        Option option = OptionBuilder.hasArg()
                .create();
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.REQUIRED_ARGUMENT, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        Option option = OptionBuilder.hasArgs()
                .create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgsWithLimit() {
        Option option = OptionBuilder.hasArgs(2)
                .create();
        Assert.assertEquals(2, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderInvalidArgsLimit() {
        OptionBuilder.hasArgs(-1);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':')
                .create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithType() {
        Option option = OptionBuilder.withType(Integer.class)
                .create();
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidType() {
        OptionBuilder.withType(String[].class);
    }

    @Test
    public void testOptionBuilderCreateWithAllOptions() {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .isRequired()
                .hasArg()
                .withDescription("My description")
                .withValueSeparator(':')
                .withArgName("argName")
                .hasOptionalArg()
                .hasArgs(2)
                .withType(Integer.class)
                .create();

        Assert.assertEquals("myLongOpt", option.getLongOpt());
        Assert.assertTrue(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals("My description", option.getDescription());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(2, option.getArgs());
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionParserParsesCorrectly() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasArg()
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt", "argValue"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertEquals("argValue", commandLine.getOptionValue("myLongOpt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionParserParsesInvalidWithAllOptions() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .isRequired()
                .hasArg()
                .withDescription("My description")
                .withValueSeparator(':')
                .withArgName("argName")
                .hasOptionalArg()
                .hasArgs(2)
                .withType(Integer.class)
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt", "argValue1", "argValue2"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertEquals("argValue1", commandLine.getOptionValue("myLongOpt"));
        Assert.assertEquals(2, commandLine.getOptionValues("myLongOpt").length);
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2"}, commandLine.getOptionValues("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithNoArg() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithNoValue() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertNull(commandLine.getOptionValue("myLongOpt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionParserParsesMissingValue() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasArg()
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionParserParsesMissingLongOpt() {
        OptionParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(new String[]{"--wrongOpt"});
        Assert.assertFalse(commandLine.hasOption("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithValue() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasArg()
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt", "argValue"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertEquals("argValue", commandLine.getOptionValue("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithMultipleValues() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasArgs(2)
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt", "argValue1", "argValue2"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2"}, commandLine.getOptionValues("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithValueSeparators() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasArgs(2)
                .withValueSeparator(',')
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt", "argValue1,argValue2"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2"}, commandLine.getOptionValues("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithValueSeparatorAndSpaces() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasArgs(2)
                .withValueSeparator(',')
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt", "argValue1, argValue2"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2"}, commandLine.getOptionValues("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithValueSeparatorAndNoSpaces() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasArgs(2)
                .withValueSeparator(',')
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt", "argValue1,argValue2"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2"}, commandLine.getOptionValues("myLongOpt"));
    }

    @Test
    public void testOptionParserParsesWithOptionalValue() {
        OptionParser parser = new DefaultParser();
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .hasOptionalArg()
                .create();
        CommandLine commandLine = parser.parse(new String[]{"--myLongOpt"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertNull(commandLine.getOptionValue("myLongOpt"));

        commandLine = parser.parse(new String[]{"--myLongOpt", "argValue"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertEquals("argValue", commandLine.getOptionValue("myLongOpt"));

        commandLine = parser.parse(new String[]{"--myLongOpt"});
        Assert.assertTrue(commandLine.hasOption("myLongOpt"));
        Assert.assertNull(commandLine.getOptionValue("myLongOpt"));
    }
}