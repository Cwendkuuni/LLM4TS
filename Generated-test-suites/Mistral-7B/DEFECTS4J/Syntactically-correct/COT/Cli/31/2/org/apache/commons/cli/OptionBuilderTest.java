package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * OptionBuilderTest class to test OptionBuilder methods.
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "long option description");
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option(null, null)
                .required(false)
                .argName("arg")
                .numberOfArgs(1);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option expectedOption = new Option(null, "argName option description")
                .required(false)
                .argName("argName")
                .numberOfArgs(1);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option(null, null)
                .required(true)
                .argName("arg")
                .numberOfArgs(1);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("option", "option description")
                .required(false)
                .argName("option")
                .numberOfArgs(2)
                .valueSeparator(':');
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option(null, null)
                .required(false)
                .argName("arg")
                .numberOfArgs(Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("option", null)
                .required(false)
                .argName("arg")
                .numberOfArgs(1)
                .optionalArg(true);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option expectedOption = new Option("option", null)
                .required(false)
                .argName("arg")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .optionalArg(true);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = new Option(null, null)
                .required(false)
                .argName("arg")
                .numberOfArgs(1)
                .type(String.class);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getClass(), actualOption.getClass());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option expectedOption = new Option("option", "description")
                .required(false)
                .argName("arg")
                .numberOfArgs(1);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }
}