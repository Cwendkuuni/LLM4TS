package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultOption() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testRequiredOption() {
        Option option = OptionBuilder.isRequired().create();
        assertEquals("", option.getLongOpt());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionalOption() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithArgName() {
        Option option = OptionBuilder.withArgName("newArgName")
                .create();
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("newArgName", option.getArgName());
    }

    @Test
    public void testOptionWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':')
                .create();
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithArg() {
        Option option = OptionBuilder.hasArg().create();
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithArgs() {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(2, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithType() {
        Option option = OptionBuilder.withType(String.class).create();
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertEquals(String.class, option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithDescription() {
        Option option = OptionBuilder.withDescription("My description")
                .create();
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs(), 0);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
        assertEquals("My description", option.getDescription());
    }
}