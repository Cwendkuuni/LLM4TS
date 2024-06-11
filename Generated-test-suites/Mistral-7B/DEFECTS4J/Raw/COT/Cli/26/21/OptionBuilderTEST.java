package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{

    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName").hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType("String").create('s');
        Option option = optionBuilder.create('s');
        Assert.assertEquals("String", option.getType().getClass().getName());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description").create('d');
        Option option = optionBuilder.create('d');
        Assert.assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.create();
        Assert.fail("Should throw an IllegalArgumentException");
    }

    @Test
    public void testCreateWithInvalidValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try
        {
            Option option = optionBuilder.withValueSeparator('%').create();
            Assert.fail("Should throw an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateWithOptionalArgAndInvalidNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try
        {
            Option option = OptionBuilder.hasOptionalArgs(5).create();
            Assert.fail("Should throw an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }
}