package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli. OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * OptionBuilderTest class to test all methods in OptionBuilder
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}
     */
    @Test
    public void testWithLongOpt() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String longOpt = "testLongOpt";
        optionBuilder.withLongOpt(longOpt);
        Option option = optionBuilder.create();
        Assert.assertEquals(longOpt, option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}
     */
    @Test
    public void testHasArg() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}
     */
    @Test
    public void testHasArgBoolean() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg(true);
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.isRequired());

        optionBuilder.reset();
        optionBuilder.hasArg(false);
        option = optionBuilder.create();
        Assert.assertEquals(0, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}
     */
    @Test
    public void testWithArgName() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String argName = "testArgName";
        optionBuilder.withArgName(argName);
        Option option = optionBuilder.create();
        Assert.assertEquals(argName, option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}
     */
    @Test
    public void testIsRequired() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}
     */
    @Test
    public void testWithValueSeparator() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        char valueSeparator = ':';
        optionBuilder.withValueSeparator(valueSeparator);
        Option option = optionBuilder.create();
        Assert.assertEquals(valueSeparator, option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}
     */
    @Test
    public void testWithValueSeparatorEmpty() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withValueSeparator();
        Option option = optionBuilder.create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}
     */
    @Test
    public void testIsRequiredBoolean() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.reset();
        optionBuilder.isRequired(true);
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());

        optionBuilder.reset();
        optionBuilder.isRequired(false);
        option = optionBuilder.create();
        Assert.assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}
     */
    @Test
    public void testHasArgs() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}
     */
    @Test
    public void testHasArgsInt() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        int numArgs = 3;
        optionBuilder.hasArgs(numArgs);
        Option option = optionBuilder.create();
        Assert.assertArrayEquals(new String[]{"", "", ""}, option.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}
     */
    @Test
    public void testHasOptionalArg() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}
     */
    @Test
    public void testHasOptionalArgs() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}
     */
    @Test
    public void testHasOptionalArgsInt() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        int numArgs = 2;
        optionBuilder.hasOptionalArgs(numArgs);
        Option option = optionBuilder.create();
        String[] args = {null, null};
        option.parseArgument(args);
        Assert.assertArrayEquals(args, option.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}
     */
    @Test
    public void testWithType() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Class type = String.class;
        optionBuilder.withType(type);
        Option option = optionBuilder.create();
        Assert.assertEquals(type, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}
     */
    @Test
    public void testWithDescription() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "testDescription";
        optionBuilder.withDescription(description);
        Option option = optionBuilder.create();
        Assert.assertEquals(description, option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create()} with no settings
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoSettings() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    /**
     * Test method for {@link OptionBuilder#create()} with invalid long opt
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidLongOpt() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("");
        optionBuilder.create();
    }

    /**
     * Test method for {@link OptionBuilder#create()} with required option and no argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequiredNoArg() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        optionBuilder.create();
    }

    /**
     * Test method for {@link OptionBuilder#create()} with optional option with no argument
     */
    @Test
    public void testCreateOptionalNoArg() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertNull(option.getValue());
    }

    /**
     * Test method for {@link OptionBuilder#create()} with optional option with argument
     */
    @Test
    public void testCreateOptionalArg() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        String argument = "testArgument";
        Option option = optionBuilder.create();
        String[] args = {argument};
        option.parseArgument(args);
        Assert.assertEquals(argument, option.getValue());
    }

    /**
     * Test method for {@link OptionBuilder#create()} with required option and argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequiredArg() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        optionBuilder.hasArg();
        Option option = optionBuilder.create();
        String[] args = {};
        option.parseArgument(args);
    }

    /**
     * Test method for {@link OptionBuilder#create()} with optional option and multiple arguments
     */
    @Test
    public void testCreateOptionalMultiArgs() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create();
        String[] args = {"arg1", "arg2"};
        option.parseArgument(args);
        Assert.assertArrayEquals(args, option.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#create()} with invalid number of arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfArgs() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs(Option.UNINITIALIZED);
        Option option = optionBuilder.create();
        String[] args = {"test"};
        option.parseArgument(args);
    }

    /**
     * Test method for {@link OptionBuilder#create()} with invalid type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidType() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Class invalidType = new Object() {};
        optionBuilder.withType(invalidType);
        try
        {
            Option option = optionBuilder.create();
        }
        catch (IllegalArgumentException e)
        {
            Assert.assertEquals("Invalid type: " + invalidType.getName(), e.getMessage());
            throw e;
        }
    }

    /**
     * Test method for {@link OptionBuilder#create()} with missing description
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissingDescription() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg();
        try
        {
            Option option = optionBuilder.create();
        }
        catch (IllegalArgumentException e)
        {
            Assert.assertEquals("Option description is mandatory.", e.getMessage());
            throw e;
        }
    }
}