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
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = optionBuilder.create('o');
        Assert.assertEquals("optionLong", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('r');
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('v');
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('x');
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.create('x');
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("This is a test option");
        Option option = optionBuilder.create('t');
        Assert.assertEquals("This is a test option", option.getDescription());
    }

    @Test
    public void testCreateWithType() throws Exception
    {
        int expected = 42;
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('i');
        Object value = option.getValue(0);
        Assert.assertEquals(expected, value);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('h');
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create('p');
        Assert.assertEquals(2, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasOptionalArgsNegativeNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(-1);
        Option option = optionBuilder.create('p');
    }

    @Test
    public void testHasArgsWithValues()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create('s');
        String[] values = {"value1", "value2"};
        option.setValue(values);
        Assert.assertArrayEquals(values, option.getArgs());
    }

    @Test
    public void testHasArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create('n');
        Assert.assertEquals("argName", option.getArgName());
    }
}