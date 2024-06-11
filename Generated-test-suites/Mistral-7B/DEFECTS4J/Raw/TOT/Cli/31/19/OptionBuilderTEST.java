package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link OptionBuilder} class.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionWithNoArguments() throws ParseException
    {
        Options options = new Options();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.withLongOpt("myOption")
                                      .create('m');
        options.addOption(option);

        String[] args = {"-m"};
        CommandLine parsedArgs = new DefaultParser().parse(options, args);
        Assert.assertFalse(parsedArgs.hasOption("myOption"));
    }

    @Test
    public void testOptionWithOneRequiredArgument() throws ParseException
    {
        Options options = new Options();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.withLongOpt("myOption")
                                      .withDescription("A test option")
                                      .isRequired()
                                      .create('m');
        options.addOption(option);

        String[] args = {"-m", "arg1"};
        CommandLine parsedArgs = new DefaultParser().parse(options, args);
        Assert.assertEquals("arg1", parsedArgs.getOptionValue("myOption"));
    }

    @Test(expected = ParseException.class)
    public void testOptionWithOneRequiredArgumentMissing() throws ParseException
    {
        Options options = new Options();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.withLongOpt("myOption")
                                      .isRequired()
                                      .create('m');
        options.addOption(option);

        String[] args = {"-m"};
        new DefaultParser().parse(options, args);
    }

    @Test
    public void testOptionWithOneOptionalArgument() throws ParseException
    {
        Options options = new Options();
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.withLongOpt("myOption")
                                      .withDescription("A test option")
                                      .create('m');
        options.addOption(option);

        String[] args = {"-m", "arg1"};
        CommandLine parsedArgs = new DefaultParser().parse(options, args);
        Assert.assertEquals("arg1", parsedArgs.getOptionValue("myOption"));

        args = new String[]{"-m"};
        parsedArgs = new DefaultParser().parse(options, args);
        Assert.assertNull(parsedArgs.getOptionValue("myOption"));
    }

    @Test
    public void testOptionWithMultipleArguments() throws ParseException
    {
        Options options = new Options();
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.withLongOpt("myOption")
                                      .withDescription("A test option")
                                      .create('m');
        options.addOption(option);

        String[] args = {"-m", "arg1", "arg2"};
        CommandLine parsedArgs = new DefaultParser().parse(options, args);
        String[] values = parsedArgs.getOptionValues("myOption");
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, values);
    }

    @Test(expected = ParseException.class)
    public void testOptionWithMultipleArgumentsNotFound() throws ParseException
    {
        Options options = new Options();
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.withLongOpt("myOption")
                                      .create('m');
        options.addOption(option);

        String[] args = {"-m", "arg1", "arg2"};
        new DefaultParser().parse(options, args);
    }

    @Test
    public void testOptionWithValueSeparator() throws ParseException
    {
        Options options = new Options();
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.withLongOpt("myOption")
                                      .withDescription("A test option")
                                      .create('m');
        options.addOption(option);

        String[] args = {"-m", "arg1:value1,value2"};
        CommandLine parsedArgs = new DefaultParser().parse(options, args);
        String[] values = parsedArgs.getOptionValues("myOption");
        Assert.assertArrayEquals(new String[]{"arg1", "value1,value2"}, values);
    }
}