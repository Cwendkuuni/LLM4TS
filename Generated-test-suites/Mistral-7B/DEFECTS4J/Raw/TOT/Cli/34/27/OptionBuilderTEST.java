package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionBuilderTest
{
    private OptionBuilder optionBuilder;

    @Before
    public void resetOptionBuilder()
    {
        optionBuilder = OptionBuilder.instance();
        OptionBuilder.reset();
    }

    @Test
    public void testLongOpt()
    {
        Option option = optionBuilder.withLongOpt("longOption")
                .create();
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        Option option = optionBuilder.hasArg().create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        Option optionWithArg = optionBuilder.hasArg().withArgName("argName").create();
        assertEquals(1, optionWithArg.getArgs());
        assertEquals("argName", optionWithArg.getArgName());
    }

    @Test
    public void testHasOptionalArg()
    {
        Option optionWithOptionalArg = optionBuilder.hasOptionalArg().create();
        assertEquals(1, optionWithOptionalArg.getArgs());
        assertEquals(Option.UNINITIALIZED, optionWithOptionalArg.getOptionalArgCount());
        Option optionWithOptionalArgs = optionBuilder.hasOptionalArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, optionWithOptionalArgs.getArgs());
        assertEquals(Option.UNINITIALIZED, optionWithOptionalArgs.getOptionalArgCount());
    }

    @Test
    public void testIsRequired()
    {
        Option option = optionBuilder.isRequired().create();
        assertFalse(option.isRequired());
        Option requiredOption = optionBuilder.isRequired(true).create();
        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testValueSeparator()
    {
        Option optionWithValueSeparator = optionBuilder.withValueSeparator(':').create();
        assertEquals(':', optionWithValueSeparator.getValueSeparator());

        Option optionWithNoValueSeparator = optionBuilder.create();
        assertNull(optionWithNoValueSeparator.getValueSeparator());
    }

    @Test
    public void testWithType()
    {
        Option optionWithStringType = optionBuilder.withType(String.class).create();
        assertEquals(String.class, optionWithStringType.getType());

        Option optionWithIntegerType = optionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, optionWithIntegerType.getType());

        assertThrows(IllegalArgumentException.class, () -> optionBuilder.withType(Double.class).create());
    }

    @Test
    public void testCreateWithNoSettings()
    {
        assertThrows(IllegalArgumentException.class, () -> optionBuilder.create());
    }

    @Test
    public void testCreateWithMissingLongOpt()
    {
        OptionBuilder.reset();
        assertThrows(IllegalArgumentException.class, () -> optionBuilder.create());
    }

    @Test
    public void testCreateWithEmptyDescription()
    {
        OptionBuilder.reset();
        OptionBuilder.withDescription("");
        Option option = optionBuilder.create();
        assertNull(option.getDescription());
    }

    @Test
    public void testCreateWithNullDescription()
    {
        OptionBuilder.reset();
        Option option = optionBuilder.withDescription(null).create();
        assertNull(option.getDescription());
    }

    @Test
    public void testCreateWithEmptyArgName()
    {
        OptionBuilder.reset();
        OptionBuilder.withArgName("");
        Option option = optionBuilder.create();
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithNullArgName()
    {
        OptionBuilder.reset();
        Option option = optionBuilder.create();
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithMissingLongOptAndArgName()
    {
        OptionBuilder.reset();
        assertThrows(IllegalArgumentException.class, () -> optionBuilder.create());
    }

    @Test
    public void testCreateWithMissingArgName()
    {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("longOpt");
        assertThrows(IllegalArgumentException.class, () -> optionBuilder.create());
    }

    @Test
    public void testCreateWithMissingOptionalArg()
    {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("longOpt")
                .hasArgs()
                .create();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testCreateWithMissingRequiredArg()
    {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("longOpt")
                .hasArgs(1)
                .create();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testCreateWithMissingValueSeparator()
    {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("longOpt")
                .hasArgs()
                .create();
        assertEquals(Option.UNINITIALIZED, OptionBuilder.valuesep);
    }

    @Test
    public void testCreateWithMissingType()
    {
        OptionBuilder.reset();
        Option option = optionBuilder.withLongOpt("longOpt")
                .hasArgs()
                .create();
        assertNull(option.getType());
    }
}