package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\u0000', OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("--option");
        assertEquals("--option", OptionBuilder.longopt);
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
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator_noArg() {
        OptionBuilder.reset();
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
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Boolean.TYPE);
        assertEquals(Boolean.TYPE, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("A description");
        assertEquals("A description", OptionBuilder.description);
    }

    @Test
    public void testCreate_char() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--option");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create('o');
        assertNotNull(option);
        assertEquals("--option", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(Option.TYPE_ARG, option.getType());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValues());
    }

    @Test
    public void testCreate_string() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--option");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create("option");
        assertNotNull(option);
        assertEquals("--option", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(Option.TYPE_ARG, option.getType());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOptSet() throws IllegalArgumentException {
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }
}