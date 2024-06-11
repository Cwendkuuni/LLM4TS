package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * JUnit tests for OptionBuilder class
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optLong");
        Option expectedOption = new Option(null, "optLong", false, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option(null, null, true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option expectedOption = new Option(null, null, true, "argName");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option(null, null, true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option(null, null, Option.UNINITIALIZED, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = new Option(null, null, Option.UNINITIALIZED, null);
        expectedOption.setValueClass(String.class);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        Option expectedOption = new Option(null, null, Option.UNINITIALIZED, null);
        expectedOption.setDescription("test description");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreateWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option(null, null, Option.UNINITIALIZED, null);
        expectedOption.setValueSeparator(':');
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }
}