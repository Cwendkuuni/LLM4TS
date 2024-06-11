package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * OptionBuilderTest class for testing OptionBuilder methods
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Assert.assertEquals("longOpt", optionBuilder.longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Assert.assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Assert.assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Assert.assertEquals("argName", optionBuilder.argName);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Assert.assertTrue(optionBuilder.required);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', optionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', optionBuilder.valuesep);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Assert.assertEquals(3, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Assert.assertTrue(optionBuilder.optionalArg);
        Assert.assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Assert.assertTrue(optionBuilder.optionalArg);
        Assert.assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithNum()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        Assert.assertTrue(optionBuilder.optionalArg);
        Assert.assertEquals(5, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithTypeString()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType("String");
        Assert.assertEquals("String", optionBuilder.type.getClass().getName());
    }

    @Test
    public void testWithTypeInteger()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Assert.assertEquals(Integer.class, optionBuilder.type.getClass());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        Assert.assertEquals("test description", optionBuilder.description);
    }

    @Test
    public void testCreateWithLongOpt()
    {
        OptionOption = OptionBuilder.withLongOpt("longOpt")
                                     .withDescription("test description")
                                     .create('l');
        Assert.assertEquals("longOpt", OptionOption.getLongOpt());
        Assert.assertEquals("test description", OptionOption.getDescription());
    }

    @Test
    public void testCreateWithChar()
    {
        OptionOption = OptionBuilder.create('t')
                                     .withDescription("test description")
                                     .create();
        Assert.assertEquals('t', OptionOption.getOpt());
        Assert.assertEquals("test description", OptionOption.getDescription());
        Assert.assertNull(OptionOption.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt()
    {
        OptionBuilder.reset();
        OptionOption = OptionBuilder.create('u');
    }
}