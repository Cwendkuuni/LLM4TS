package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for OptionBuilder
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
    public void testHasArgWithName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Option option = optionBuilder.create();
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(3, option.getArgs().length);
        Assert.assertTrue(option.isOptional());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.create();
        Option option = optionBuilder.create();
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description")
                                                  .hasArg()
                                                  .withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("description", option.getDescription());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertArrayEquals(new String[]{"argName"}, option.getArgs());
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class)
                                                  .withDescription("description")
                                                  .hasArg()
                                                  .withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertArrayEquals(new String[]{"argName"}, option.getArgs());
    }
}