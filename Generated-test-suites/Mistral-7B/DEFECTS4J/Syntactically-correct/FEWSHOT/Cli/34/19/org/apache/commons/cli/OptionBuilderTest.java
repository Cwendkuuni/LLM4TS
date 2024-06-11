package org.apache.commons.cli.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

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
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withDescription("A long option with a description")
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndArgName() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("arg")
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndIsRequired() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("arg")
                .isRequired()
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndHasArg() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("arg")
                .hasArg()
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndHasArgAndValuesep() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("arg")
                .hasArg()
                .withValueSeparator(':')
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getType());
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndHasArgs() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("args")
                .hasArgs()
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("args", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("arg")
                .hasOptionalArg()
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue(0));
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("args")
                .hasOptionalArgs(3)
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("args", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(3, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndArgNameAndHasOptionalArgsAndValuesep() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withArgName("args")
                .hasOptionalArgs(3)
                .withValueSeparator(';')
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("args", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(3, option.getArgs());
        assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArg() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withDescription("A long option with a description")
                .hasArg()
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgs() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withDescription("A long option with a description")
                .hasArgs()
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withDescription("A long option with a description")
                .hasOptionalArg()
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withDescription("A long option with a description")
                .hasOptionalArgs(3)
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(3, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgAndType() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withDescription("A long option with a description")
                .hasArg()
                .withType(Integer.TYPE)
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getValueSeparator());
        assertNotNull(option.getType());
        assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgsAndType() {
        Option option = OptionBuilder.withLongOpt("--longopt")
                .withDescription("A long option with a description")
                .hasArgs()
                .withType(String[].class)
                .create();
        assertEquals("--longopt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getValueSeparator());
        assertNotNull(option.getType());
        assertEquals(String[].class, option.getType());
    }
}