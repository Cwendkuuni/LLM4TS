package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "description");
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("arg")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg_withArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                .withArgName("argName");
        Option expectedOption = Option.builder("argName")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("arg")
                .required(true)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = Option.builder("arg")
                .required(false)
                .valueSeparator(':')
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOptSet()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withInvalidLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt(" ");
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder("arg")
                .required(false)
                .args(Option.UNLIMITED_VALUES)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs_withNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = Option.builder("arg")
                .required(false)
                .args(2)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder("arg")
                .required(false)
                .args(1)
                .optionalArg(true)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = Option.builder("arg")
                .required(false)
                .args(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs_withNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option expectedOption = Option.builder("arg")
                .required(false)
                .args(3)
                .optionalArg(true)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithType()
    {
        String type = String.class.getName();
        OptionBuilder optionBuilder = OptionBuilder.withType(type);
        Option expectedOption = Option.builder("arg")
                .required(false)
                .args(Option.UNINITIALIZED)
                .type(type)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithDescription()
    {
        String description = "description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option expectedOption = Option.builder("arg")
                .required(false)
                .args(Option.UNINITIALIZED)
                .type(String.class)
                .build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption, actualOption);
        Assert.assertEquals(description, actualOption.getLongOpt());
        Assert.assertEquals(description, actualOption.getDescription());
    }
}