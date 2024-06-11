package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", optionBuilder.getLongOpt());
        assertNull(optionBuilder.getArgName());
        assertNull(optionBuilder.getType());
        assertFalse(optionBuilder.isRequired());
        assertFalse(optionBuilder.hasArg());
        assertFalse(optionBuilder.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertNull(optionBuilder.getValueSeparator());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertTrue(optionBuilder.hasArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
        assertFalse(optionBuilder.isRequired());
        assertNull(optionBuilder.getType());
        assertNull(optionBuilder.getValueSeparator());
        assertNull(optionBuilder.getLongOpt());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.hasOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
        assertFalse(optionBuilder.isRequired());
        assertNull(optionBuilder.getType());
        assertNull(optionBuilder.getValueSeparator());
        assertNull(optionBuilder.getLongOpt());
    }

    @Test(expected = IllegalStateException.class)
    public void testHasArgAndHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().hasOptionalArg();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> optionBuilder.getNumberOfArgs());
        assertEquals("Both hasArg and hasOptionalArg cannot be set", exception.getMessage());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
        assertFalse(optionBuilder.isRequired());
        assertNull(optionBuilder.getType());
        assertNull(optionBuilder.getValueSeparator());
        assertNull(optionBuilder.getLongOpt());
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
        assertFalse(optionBuilder.isRequired());
        assertNull(optionBuilder.getType());
        assertNull(optionBuilder.getValueSeparator());
        assertNull(optionBuilder.getLongOpt());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
        assertFalse(optionBuilder.hasArg());
        assertFalse(optionBuilder.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertNull(optionBuilder.getType());
        assertNull(optionBuilder.getValueSeparator());
        assertNull(optionBuilder.getLongOpt());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.getType());
        assertFalse(optionBuilder.isRequired());
        assertNull(optionBuilder.getLongOpt());
        assertNull(optionBuilder.getArgName());
        assertFalse(optionBuilder.hasArg());
        assertFalse(optionBuilder.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertNull(optionBuilder.getValueSeparator());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", optionBuilder.getDescription());
        assertNull(optionBuilder.getLongOpt());
        assertNull(optionBuilder.getArgName());
        assertFalse(optionBuilder.isRequired());
        assertFalse(optionBuilder.hasArg());
        assertFalse(optionBuilder.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertNull(optionBuilder.getType());
        assertNull(optionBuilder.getValueSeparator());
    }

    @Test
    public void testCreate() throws Exception
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("description")
                .isRequired()
                .hasArg()
                .withValueSeparator('=')
                .create('l');

        assertNotNull(option);
        assertEquals("description", option.getDescription());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals('l', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(Option.VALUE_REQUIRED, option.getArgType());
        assertEquals('=', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithNullLongOpt() throws Exception
    {
        try
        {
            Option option = OptionBuilder.create();
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("must specify longopt", e.getMessage());
        }
    }
}