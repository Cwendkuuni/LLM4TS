package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for OptionBuilder class
 */
public class OptionBuilderTest
{
    @Test
    public void testSimpleOption()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("test option")
                                     .withLongOpt("test-option")
                                     .withArgName("arg")
                                     .create('t');

        Assert.assertEquals("test option", option.getDescription());
        Assert.assertEquals("test-option", option.getLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(' ', option.getValueSeparator());

        Option parsedOption = new Option('t', "test-option", false, "arg");
        Assert.assertEquals(option, parsedOption);
    }

    @Test
    public void testRequiredOption()
    {
        Option option = OptionBuilder.isRequired()
                                     .withDescription("test option")
                                     .withLongOpt("test-option")
                                     .withArgName("arg")
                                     .create('t');

        Assert.assertEquals("test option", option.getDescription());
        Assert.assertEquals("test-option", option.getLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertTrue(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(' ', option.getValueSeparator());

        Option parsedOption = new Option('t', "test-option", true, "arg");
        Assert.assertEquals(option, parsedOption);
    }

    @Test
    public void testOptionalOption()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                     .withDescription("test option")
                                     .withLongOpt("test-option")
                                     .withArgName("arg")
                                     .create('t');

        Assert.assertEquals("test option", option.getDescription());
        Assert.assertEquals("test-option", option.getLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(' ', option.getValueSeparator());

        Option parsedOption = new Option('t', "test-option", false, "arg");
        Assert.assertEquals(option, parsedOption);
    }

    @Test
    public void testOptionWithValue()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("test option")
                                     .withLongOpt("test-option")
                                     .withArgName("arg")
                                     .create('t');

        Option parsedOption = new Option('t', "test-option", false, "arg");
        parsedOption.setArgs(new String[]{"arg1", "arg2"});

        Assert.assertEquals(option, parsedOption);
        Assert.assertEquals("test option", option.getDescription());
        Assert.assertEquals("test-option", option.getLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals(new String[]{"arg1", "arg2"}, parsedOption.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(' ', option.getValueSeparator());
    }

    @Test
    public void testOptionWithValueSeparator()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("test option")
                                     .withLongOpt("test-option")
                                     .withArgName("arg")
                                     .withValueSeparator(':')
                                     .create('t');

        Option parsedOption = new Option('t', "test-option", false, "arg");
        parsedOption.setArgs(new String[]{"arg1:value1", "arg2:value2"});

        Assert.assertEquals(option, parsedOption);
        Assert.assertEquals("test option", option.getDescription());
        Assert.assertEquals("test-option", option.getLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals(new String[]{"arg1:value1", "arg2:value2"}, parsedOption.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutLongOpt()
    {
        OptionBuilder.create();
    }
}