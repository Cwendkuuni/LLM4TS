package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        assertEquals("optionLongName", optionBuilder.longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", optionBuilder.argName);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.required);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.required);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
        assertFalse(optionBuilder.optionalArg);
    }

    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, optionBuilder.numberOfArgs);
        assertFalse(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(optionBuilder.type);
        assertEquals(String.class, optionBuilder.type.getClass());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("option description");
        assertEquals("option description", optionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create('X'));
    }

    @Test
    public void testCreateValidOption()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance()
                .withLongOpt("optionLongName")
                .withDescription("option description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .withType(String.class);
        Option option = OptionBuilder.create('O');
        assertEquals("optionLongName", option.getLongOpt());
        assertEquals("option description", option.getDescription());
        assertEquals(Option.VALUE_REQUIRED, option.getOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
        assertEquals(String.class, option.getType());
    }
}