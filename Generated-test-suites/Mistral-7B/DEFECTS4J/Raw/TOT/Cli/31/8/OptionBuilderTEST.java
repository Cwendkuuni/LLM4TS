package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionWithLongOptHasArgAndValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArg()
                                     .withValueSeparator(':')
                                     .create('L');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("hasArg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertFalse(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionWithLongOptHasArgOptionalArgAndValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArg(true)
                                     .hasOptionalArg()
                                     .withValueSeparator(':')
                                     .create('L');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertTrue(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionMissingLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testOptionWithLongOptHasArgAndValueSeparatorWithValue()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArg()
                                     .withValueSeparator(':')
                                     .create('L');

        String expectedValue = "expectedValue";
        option.setValue(expectedValue);

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertFalse(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertEquals(expectedValue, option.getValue());
    }

    @Test
    public void testOptionWithLongOptHasArgs()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArgs()
                                     .create('L');

        String[] expectedValues = {"expectedValue1", "expectedValue2"};
        option.setValues(expectedValues);

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testOptionWithLongOptHasArgsAndValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArgs()
                                     .withValueSeparator(':')
                                     .create('L');

        String[] expectedValues = {"expectedValue1", "expectedValue2"};
        option.setValues(expectedValues);

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testOptionWithLongOptHasArgsOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArgs(true)
                                     .create('L');

        String[] expectedValues = {"expectedValue1", "expectedValue2"};
        option.setValues(expectedValues);

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertArrayEquals(expectedValues, option.getValues());
    }
}