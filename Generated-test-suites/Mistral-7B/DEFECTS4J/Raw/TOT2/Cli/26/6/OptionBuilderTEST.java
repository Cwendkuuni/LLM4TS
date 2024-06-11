package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test for OptionBuilder withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    /**
     * Test for OptionBuilder hasArg method with one argument.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
    }

    /**
     * Test for OptionBuilder hasArg method with no argument.
     */
    @Test
    public void testHasArgNoArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.create();
        Option option = optionBuilder.create();
        Assert.assertArrayEquals(new String[0], option.getArgs());
    }

    /**
     * Test for OptionBuilder withArgName method.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    /**
     * Test for OptionBuilder isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test for OptionBuilder withValueSeparator method.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test for OptionBuilder hasArgs method with unlimited arguments.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test for OptionBuilder hasArgs method with specific number of arguments.
     */
    @Test
    public void testHasArgsWithSpecificNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(2, option.getArgs().length);
    }

    /**
     * Test for OptionBuilder hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isOptional());
    }

    /**
     * Test for OptionBuilder hasOptionalArgs method with unlimited number of optional arguments.
     */
    @Test
    public void testHasOptionalArgsWithUnlimitedArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        String[] expectedArgs = {"arg1", "arg2", "arg3"};
        option.parseArgument(Arrays.asList("--option", "arg1", "arg2", "arg3"));
        Assert.assertArrayEquals(expectedArgs, option.getArgs());
    }

    /**
     * Test for OptionBuilder hasOptionalArgs method with specific number of optional arguments.
     */
    @Test
    public void testHasOptionalArgsWithSpecificNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();
        String[] expectedArgs = {"arg1", "arg2", "arg3"};
        option.parseArgument(Arrays.asList("--option", "arg1", "arg2", "arg3"));
        Assert.assertArrayEquals(expectedArgs, option.getArgs());
    }

    /**
     * Test for OptionBuilder withType method.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(String.class, option.getType());
    }

    /**
     * Test for OptionBuilder withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        String description = "This is a description.";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option option = optionBuilder.create();
        Assert.assertEquals(description, option.getDescription());
    }

    /**
     * Test for create method with default settings.
     */
    @Test
    public void testCreateWithDefaultSettings()
    {
        Option option = OptionBuilder.create().create();
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals(0, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getType());
        Assert.assertArrayEquals(new String[0], option.getArgs());
    }

    /**
     * Test for create method with custom settings.
     */
    @Test
    public void testCreateWithCustomSettings()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasArgs(2)
                .hasOptionalArg()
                .withType(Integer.class)
                .withDescription("This is a description.");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals(2, option.getArgs().length);
        Assert.assertFalse(option.isOptional());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertEquals("This is a description.", option.getDescription());
    }
}