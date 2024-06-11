package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("optionLongName");
        assertEquals("optionLongName", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_boolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator_char() {
        OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType_Object() {
        OptionBuilder.withType(String.class);
        assertSame(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("optionLongName");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(';');
        OptionBuilder.hasArgs(2);
        OptionBuilder.withType(Integer.class);
        OptionBuilder.withDescription("description");

        Option option = OptionBuilder.create('o');

        assertEquals("optionLongName", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(2, option.getArgs().length);
        assertEquals(true, option.hasOptionalArg());
        assertEquals(';', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_char_noLongOpt() throws IllegalArgumentException {
        OptionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_string_noLongOpt() throws IllegalArgumentException {
        OptionBuilder.create("option");
    }
}