package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateWithDefaults() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndArgName() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withArgName("arg")
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndDescription() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withArgName("arg")
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndType() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withType(String.class)
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndDescription() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withType(String.class)
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndArgName() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withArgName("arg")
                .withType(String.class)
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndArgNameAndDescription() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withArgName("arg")
                .withType(String.class)
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndDescriptionAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withType(String.class)
                .withValueSeparator(',')
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(',', option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndArgNameAndDescriptionAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withArgName("arg")
                .withType(String.class)
                .withValueSeparator(',')
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(',', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndArgNameAndDescriptionAndHasArg() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withArgName("arg")
                .withType(String.class)
                .hasArg()
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndArgNameAndDescriptionAndHasArgs() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withArgName("arg")
                .withType(String[].class)
                .hasArgs()
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String[].class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndArgNameAndDescriptionAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withArgName("arg")
                .withType(String.class)
                .hasOptionalArg()
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndArgNameAndDescriptionAndHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("--long-opt")
                .withDescription("This is a long option")
                .withArgName("arg")
                .withType(String[].class)
                .hasOptionalArgs(2)
                .create();
        assertEquals("--long-opt", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(String[].class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(2, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArgs());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

}