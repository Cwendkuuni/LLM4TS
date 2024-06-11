package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .hasArg()
                .create('l');
        Assert.assertEquals("longopt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderWithArgName()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .withArgName("argName")
                .create('l');
        Assert.assertEquals("longopt", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .isRequired()
                .create('l');
        Assert.assertEquals(true, option.isRequired());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .hasOptionalArg()
                .create('l');
        Assert.assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .withValueSeparator(':')
                .create('l');
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .hasArgs()
                .create('l');
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasArgsWithSpecifiedNum()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .hasArgs(2)
                .create('l');
        Assert.assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasType()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .withType(Integer.class)
                .create('l');
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .withDescription("This is a test option")
                .create('l');
        Assert.assertEquals("This is a test option", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        Option option = OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder.withLongOpt("1longopt");
        Option option = OptionBuilder.create('l');
    }

    @Test
    public void testCreateWithValidArguments()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .isRequired()
                .hasArgs()
                .create('l');
        String[] args = {"-l", "arg1"};
        CommandLine cmd = OptionParser.parse(args, OptionParser.getDefaultParser());
        Assert.assertEquals("arg1", option.getValue(0));
    }

    @Test
    public void testCreateWithInvalidNumberOfArguments()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .isRequired()
                .hasArgs(2)
                .create('l');
        String[] args = {"-l"};
        CommandLine cmd = OptionParser.parse(args, OptionParser.getDefaultParser());
        Assert.assertNull(option.getValue(0));
        Assert.assertNull(option.getValue(1));
    }

    @Test
    public void testCreateWithOptionalArgument()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                .isRequired(false)
                .hasArgs()
                .create('l');
        String[] args = {"-l"};
        CommandLine cmd = OptionParser.parse(args, OptionParser.getDefaultParser());
        Assert.assertNull(option.getValue(0));

        String[] args2 = {"-l", "arg1"};
        CommandLine cmd2 = OptionParser.parse(args2, OptionParser.getDefaultParser());
        Assert.assertEquals("arg1", option.getValue(0));
    }
}