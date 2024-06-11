package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption");
        Option expectedOption = Option.builder("longOption").build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("arg").build().required(true);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder("arg").build().optionalArg(true);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = Option.builder("arg").build().valueSeparator(':');
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("arg").build().required(true);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder("arg").build().args(Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgsWithSpecifiedNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = Option.builder("arg").build().args(2);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = Option.builder("arg").build().optionalArg(true).args(Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgsWithSpecifiedNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option expectedOption = Option.builder("arg").build().optionalArg(true).args(3);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testCreateWithEmptyDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption").withDescription("");
        Option actualOption = optionBuilder.create();
        assertNull(actualOption.getLongOpt());
        assertNull(actualOption.getDescription());
    }

    @Test
    public void testCreateWithLongOptAndDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption").withDescription("Test Option");
        Option actualOption = optionBuilder.create();
        assertEquals("longOption", actualOption.getLongOpt());
        assertEquals("Test Option", actualOption.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasArgWithNegativeNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasOptionalArgsWithNegativeNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasOptionalArgsWithZeroNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(0);
    }
}