package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option(null, "longOpt", false, null);
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder().required(false).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        Assert.assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder().required(false).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        Assert.assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        Assert.assertTrue(actualOption.isOptional());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder().required(true).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertTrue(actualOption.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        Option expectedOption = Option.builder().required(false).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder().required(false).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, actualOption.getArgs());
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = Option.builder().required(false).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(2, actualOption.getArgs());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = Option.builder().required(false).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(String.class, actualOption.getType());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("option description");
        Option expectedOption = Option.builder().required(false).argName("arg").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals("option description", actualOption.getLongOpt());
    }
}