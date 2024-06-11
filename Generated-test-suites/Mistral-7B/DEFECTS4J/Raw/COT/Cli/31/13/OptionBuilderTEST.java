package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for OptionBuilder class
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "", Option.NO_ARG, false);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("arg", "", Option.REQUIRED_ARG, false);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgWithName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("name");
        Option expectedOption = new Option("name", "", Option.REQUIRED_ARG, false);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("arg", "", Option.REQUIRED_ARG, true);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", "", Option.OPTIONAL_ARG, true);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("arg", "", Option.UNLIMITED_VALUES, false);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option expectedOption = new Option("arg", "", 3, false);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = new Option("arg", "", Option.NO_ARG, false, Integer.class);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreateWithNoSettings()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Assert.assertThrows(IllegalArgumentException.class, optionBuilder::create);
    }

    @Test
    public void testCreateWithNoLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Assert.assertThrows(IllegalArgumentException.class, () -> optionBuilder.create((char)0));
    }

    @Test
    public void testCreateWithOptionalArgAndNoArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", "", Option.OPTIONAL_ARG, true);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);

        Option optionWithArgs = OptionBuilder.hasOptionalArgs(2).create();
        Assert.assertEquals(expectedOption, optionWithArgs);
    }

    @Test
    public void testCreateWithOptionalArgAndArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().hasArgs(2);
        Option expectedOption = new Option("arg", "", Option.OPTIONAL_ARG, true, 2);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreateWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("arg", "", Option.UNLIMITED_VALUES, false, ":");
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }
}