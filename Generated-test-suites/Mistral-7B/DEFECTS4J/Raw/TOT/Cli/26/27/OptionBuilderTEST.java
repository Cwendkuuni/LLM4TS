package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.hasArg().withLongOpt("opt")
                .withDescription("an option that requires an argument").create('o');
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-o", "argValue"};
        try {
            CommandLine cmd = parser.parse(option.getClass().getDeclaredConstructor().newInstance(), args);
            Assert.assertEquals("argValue", option.getValue(cmd));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("test failed: " + e.getMessage());
        }
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.hasOptionalArg().withLongOpt("opt")
                .withDescription("an option that can have an optional argument")
                .create('o');
        CommandLineParser parser = new DefaultParser();
        String[] args1 = {"-o", "argValue1"};
        String[] args2 = {"-o"};
        try {
            CommandLine cmd1 = parser.parse(option.getClass().getDeclaredConstructor().newInstance(), args1);
            Assert.assertEquals("argValue1", option.getValue(cmd1));
            CommandLine cmd2 = parser.parse(option.getClass().getDeclaredConstructor().newInstance(), args2);
            Assert.assertNull(option.getValue(cmd2));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("test failed: " + e.getMessage());
        }
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.hasArgs().withLongOpt("opts")
                .withDescription("an option that can have multiple arguments")
                .create('s');
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-s", "arg1", "arg2", "arg3"};
        try {
            CommandLine cmd = parser.parse(option.getClass().getDeclaredConstructor().newInstance(), args);
            String[] values = option.getArgs(cmd);
            Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, values);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("test failed: " + e.getMessage());
        }
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withValueSeparator(':').hasArgs().withLongOpt("opt")
                .withDescription("an option that separates values with a colon")
                .create('o');
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-o", "value1:value2:value3"};
        try {
            CommandLine cmd = parser.parse(option.getClass().getDeclaredConstructor().newInstance(), args);
            String[] values = option.getArgs(cmd);
            Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, values);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("test failed: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
    }
}