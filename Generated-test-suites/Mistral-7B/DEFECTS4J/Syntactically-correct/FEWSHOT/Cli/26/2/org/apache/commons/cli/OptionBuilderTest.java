package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithDefaults() {
        Option option = OptionBuilder.create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .create('option');
        assertEquals("longOpt", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        Option option = OptionBuilder.withDescription("description")
                .create('option');
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        Option option = OptionBuilder.isRequired().create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasArg() {
        Option option = OptionBuilder.hasArg().create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        Option option = OptionBuilder.hasArgs().create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber() {
        Option option = OptionBuilder.hasArgs(2).create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(2, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithType() {
        Option option = OptionBuilder.withType(String.class)
                .create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertEquals(String.class, option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(';')
                .create('option');
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertEquals(';', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create('option');
        fail("Expected IllegalArgumentException, but got " + option);
    }
}