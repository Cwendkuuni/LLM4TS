package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    

    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option expectedOption = new Option("optionShort", "Option long description", true, "long-opt");
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("optionShort", null, true, null);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = new Option("optionShort", null, true, null, "argName");
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getArgName(), actualOption.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("optionShort", null, true, null);
        Option actualOption = optionBuilder.create();
        Assert.assertTrue(actualOption.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("optionShort", null, false, null, null, Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getOptionalArg(), actualOption.getOptionalArg());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("optionShort", null, Option.UNLIMITED_VALUES, null);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testHasArgsWithNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = new Option("optionShort", null, 2, null);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("optionShort", null, true, null, null, Option.UNLIMITED_VALUES, ':');
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = new Option("optionShort", null, true, null, null, Option.UNLIMITED_VALUES, null).setValueType(String.class);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getValueType(), actualOption.getValueType());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        Option expectedOption = new Option("optionShort", "Option description", true, null);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    @Test
    public void testReset()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = OptionBuilder.withLongOpt("optionLong").create();
        optionBuilder.reset();
        Option actualOption = optionBuilder.create();
        Assert.assertNull(actualOption.getLongOpt());
    }

    
}