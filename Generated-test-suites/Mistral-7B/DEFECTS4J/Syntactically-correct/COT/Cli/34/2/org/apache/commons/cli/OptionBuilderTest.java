package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option");
        Option expectedOption = new Option("option", "An option");
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        Assert.assertEquals(expectedOption.getName(), actualOption.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWhenLongOptNotSet()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();

        Option expectedOption = Option.builder("option")
                .required(false)
                .argName("arg")
                .build();

        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getRequired(), actualOption.isRequired());
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
        Assert.assertEquals(expectedOption.getArgName(), actualOption.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();

        Option expectedOption = Option.builder("option")
                .required(true)
                .build();

        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');

        Option expectedOption = Option.builder("option")
                .required(false)
                .argName("arg")
                .valueSeparator(':')
                .build();

        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator('');

        optionBuilder.create();
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();

        Option expectedOption = Option.builder("option")
                .required(false)
                .argName("args")
                .args(Option.UNLIMITED_VALUES)
                .build();

        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();

        Option expectedOption = Option.builder("option")
                .required(false)
                .argName("arg")
                .args(1)
                .optional(true)
                .build();

        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.isOptional(), actualOption.isOptional());
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);

        Option expectedOption = Option.builder("option")
                .required(false)
                .argName("arg")
                .args(1)
                .type(Integer.class)
                .build();

        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getType(), actualOption.getType());
    }

    @Test(expected = IllegalAccessException.class)
    public void testCreateWithPrivateConstructor() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
    }

    @Test
    public void testReset()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("--option");
        Option option = optionBuilder.create();

        optionBuilder.reset();

        Option newOption = optionBuilder.create();

        Assert.assertNull(newOption.getLongOpt());
        Assert.assertNull(newOption.getName());
    }

    @Test
    public void testCreateWithEmptyArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("");

        try
        {
            optionBuilder.create();
        }
        catch (IllegalArgumentException e)
        {
            List<String> messages = new ArrayList<>();
            messages.add("argName can't be empty");
            Assert.assertEquals(messages.toString(), e.getMessage());
        }
    }

    @Test
    public void testCreateWithInvalidArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("--argname");

        try
        {
            optionBuilder.create();
        }
        catch (IllegalArgumentException e)
        {
            List<String> messages = new ArrayList<>();
            messages.add("argName can't be a long opt");
            Assert.assertEquals(messages.toString(), e.getMessage());
        }
    }

    @Test
    public void testCreateWithInvalidNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgs(-1);

        try
        {
            optionBuilder.create();
        }
        catch (IllegalArgumentException e)
        {
            List<String> messages = new ArrayList<>();
            messages.add("Invalid number of arguments");
            Assert.assertEquals(messages.toString(), e.getMessage());
        }
    }
}