package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultOption() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .create();
        assertEquals("longOption", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithDescription() {
        Option option = OptionBuilder.withDescription("Option description")
                .create();
        assertNull(option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithArg() {
        Option option = OptionBuilder.hasArg().create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithArgName() {
        Option option = OptionBuilder.withArgName("argName").create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testOptionWithLongOptAndArg() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg().create();
        assertEquals("longOption", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .withDescription("Option description").create();
        assertEquals("longOption", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg().withValueSeparator(':').create();
        assertEquals("longOption", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testOptionWithLongOptArgDescriptionAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .withDescription("Option description")
                .hasArg().withValueSeparator(':').create();
        assertEquals("longOption", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testOptionWithLongOptArgDescriptionValueSeparatorAndType() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .withDescription("Option description")
                .hasArg()
                .withValueSeparator(':')
                .withType(String.class)
                .create();
        assertEquals("longOption", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertEquals(String.class, option.getType());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getArgName());
    }
}