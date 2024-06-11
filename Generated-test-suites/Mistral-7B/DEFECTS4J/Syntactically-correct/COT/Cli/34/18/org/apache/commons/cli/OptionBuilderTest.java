package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

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
        Option expectedOption = Option.builder("argOption").required(false).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getClass(), actualOption.getClass());
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("requiredOption").required(true).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = Option.builder("valueOption").required(false).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testCreateWithOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder("optionalArgOption").required(false).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.isOptional(), actualOption.isOptional());
    }

    @Test
    public void testCreateWithValueType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = Option.builder("typeOption").required(false).argType(Integer.class).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getType(), actualOption.getType());
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option expectedOption = Option.builder("descriptionOption").build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        Assert.assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }

    @Test
    public void testCreateWithMultipleArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option expectedOption = Option.builder("multiArgOption").required(false).args(3).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
        optionBuilder.create();
    }

    @Test
    public void testCreateWithUnlimitedArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder("unlimitedArgOption").required(false).args(Option.UNLIMITED_VALUES).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }
}