package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithSingleArgument()
    {
        Option option = OptionBuilder.hasArg()
                .withDescription("Test option")
                .withLongOpt("testOption")
                .create('t');

        assertEquals("Test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertEquals('t', option.getOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithOptionalArgument()
    {
        Option option = OptionBuilder.hasOptionalArg()
                .withDescription("Test option")
                .withLongOpt("testOption")
                .create('t');

        assertEquals("Test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertEquals('t', option.getOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertTrue(option.isOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(',')
                .hasArg()
                .withDescription("Test option")
                .withLongOpt("testOption")
                .create('t');

        assertEquals("Test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertEquals('t', option.getOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithRequiredArgument()
    {
        Option option = OptionBuilder.isRequired()
                .withDescription("Test option")
                .withLongOpt("testOption")
                .create('t');

        assertEquals("Test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertEquals('t', option.getOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithUnlimitedArguments()
    {
        Option option = OptionBuilder.hasArgs()
                .withDescription("Test option")
                .withLongOpt("testOption")
                .create('t');

        assertEquals("Test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertEquals('t', option.getOpt());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withType(String.class)
                .hasArg()
                .withDescription("Test option")
                .withLongOpt("testOption")
                .create('t');

        assertEquals("Test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertEquals('t', option.getOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(String.class, option.getType());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithArgName()
    {
        Option option = OptionBuilder.withArgName("argName")
                .hasArg()
                .withDescription("Test option")
                .withLongOpt("testOption")
                .create('t');

        assertEquals("Test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertEquals('t', option.getOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }
}