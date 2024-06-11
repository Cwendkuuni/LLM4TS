package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateWithDefaults() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithDescription() {
        Option option = OptionBuilder.withDescription("An option with description")
                .create();
        assertNotNull(option);
        assertNull(option.getLongOpt());
        assertEquals("An option with description", option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertNotNull(option);
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("argName", option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithType() {
        Option option = OptionBuilder.withType(Boolean.TYPE)
                .create();
        assertNotNull(option);
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertEquals(Boolean.TYPE, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option and description")
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option and description", option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndArgName() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withArgName("argName")
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndType() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withType(Integer.TYPE)
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertEquals(Integer.TYPE, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndType() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option, description and type")
                .withType(String[].class)
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option, description and type", option.getDescription());
        assertNull(option.getArgName());
        assertEquals(String[].class, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgName() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option, description and argName")
                .withArgName("argName")
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option, description and argName", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndTypeAndArgName() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option, description, type and argName")
                .withArgName("argName")
                .withType(String.class)
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option, description, type and argName", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndTypeAndArgNameAndHasArg() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option, description, type, argName and hasArg")
                .withArgName("argName")
                .withType(String.class)
                .hasArg()
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option, description, type, argName and hasArg", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_OPTIONAL, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndTypeAndArgNameAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option, description, type, argName and hasOptionalArg")
                .withArgName("argName")
                .withType(String.class)
                .hasOptionalArg()
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option, description, type, argName and hasOptionalArg", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.VALUE_OPTIONAL, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertFalse(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndTypeAndArgNameAndHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option, description, type, argName and hasOptionalArgs")
                .withArgName("argName")
                .withType(String[].class)
                .hasOptionalArgs(2)
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option, description, type, argName and hasOptionalArgs", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(String[].class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.VALUE_OPTIONAL, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.isUnlimited());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndTypeAndArgNameAndHasArgAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .withDescription("An option with long option, description, type, argName and hasArg and valueSeparator")
                .withArgName("argName")
                .withType(String.class)
                .hasArg()
                .withValueSeparator(':')
                .create();
        assertNotNull(option);
        assertEquals("optionLong", option.getLongOpt());
        assertEquals("An option with long option, description, type, argName and hasArg and valueSeparator", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.isValueSeparatorPresent());
        assertEquals(':', option.getValueSeparator());
        assertFalse(option.isUnlimited());
    }
}