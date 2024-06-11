package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateWithDefaults() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgCount());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .create();
        assertEquals("myOption", option.getLongOpt());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt")
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt", option.getDescription());
    }

    @Test
    public void testCreateWithShortOpt() {
        Option option = OptionBuilder.create('a')
                                       .create();
        assertEquals('a', option.getValue());
    }

    @Test
    public void testCreateWithShortOptAndLongOpt() {
        Option option = OptionBuilder.create('a')
                                       .withLongOpt("myLongOpt")
                                       .create();
        assertEquals('a', option.getValue());
        assertEquals("myLongOpt", option.getLongOpt());
    }

    @Test
    public void testCreateWithShortOptAndDescription() {
        Option option = OptionBuilder.create('a')
                                       .withDescription("An option with a short opt")
                                       .create();
        assertEquals('a', option.getValue());
        assertEquals("An option with a short opt", option.getDescription());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArg() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt and an argument")
                                       .hasArg()
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt and an argument", option.getDescription());
        assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgName() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt and an argument named 'foo'")
                                       .hasArg("foo")
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt and an argument named 'foo'", option.getDescription());
        assertEquals("foo", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgAndIsRequired() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt, an argument, and is required")
                                       .hasArg()
                                       .isRequired()
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt, an argument, and is required", option.getDescription());
        assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgAndOptionalArg() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt, an argument, and is optional")
                                       .hasArg(true)
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt, an argument, and is optional", option.getDescription());
        assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgAndOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt, multiple arguments, and is optional")
                                       .hasArgs(3)
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt, multiple arguments, and is optional", option.getDescription());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgCount());
        assertEquals(true, option.hasOptionalArgs());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgAndIsRequiredAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt, an argument, is required, and a value separator")
                                       .hasArg()
                                       .isRequired()
                                       .withValueSeparator(',')
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt, an argument, is required, and a value separator", option.getDescription());
        assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
        assertEquals(true, option.isRequired());
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasArgAndIsRequiredAndValueSeparatorAndType() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt, an argument, is required, a value separator, and a type")
                                       .hasArg()
                                       .isRequired()
                                       .withValueSeparator(',')
                                       .withType(Integer.TYPE)
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt, an argument, is required, a value separator, and a type", option.getDescription());
        assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
        assertEquals(true, option.isRequired());
        assertEquals(',', option.getValueSeparator());
        assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt and an optional argument")
                                       .hasOptionalArg()
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt and an optional argument", option.getDescription());
        assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt and multiple optional arguments")
                                       .hasOptionalArgs(2)
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt and multiple optional arguments", option.getDescription());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgCount());
        assertEquals(true, option.hasOptionalArgs());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndHasOptionalArgsAndType() {
        Option option = OptionBuilder.withLongOpt("myOption")
                                       .withDescription("An option with a long opt, multiple optional arguments, and a type")
                                       .hasOptionalArgs(2)
                                       .withType(String[].class)
                                       .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a long opt, multiple optional arguments, and a type", option.getDescription());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgCount());
        assertEquals(true, option.hasOptionalArgs());
        assertEquals(String[].class, option.getType());
    }
}