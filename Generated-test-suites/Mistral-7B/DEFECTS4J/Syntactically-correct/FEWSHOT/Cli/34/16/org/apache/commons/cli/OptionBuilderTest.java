package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertNotNull(OptionBuilder.instance);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("newLongopt");
        assertEquals("newLongopt", OptionBuilder.longopt);
        OptionBuilder.reset();
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testHasArg_boolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
        OptionBuilder.reset();
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
        OptionBuilder.reset();
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
        OptionBuilder.reset();
    }

    @Test
    public void testWithValueSeparator_noArg() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
        OptionBuilder.reset();
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
        OptionBuilder.reset();
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
        OptionBuilder.reset();
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(5);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(5, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
        OptionBuilder.reset();
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("newDescription");
        assertEquals("newDescription", OptionBuilder.description);
        OptionBuilder.reset();
    }

    @Test
    public void testCreate_char() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("newLongopt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.withValueSeparator(';');
        OptionBuilder.hasArgs(3);
        OptionBuilder.withType(Integer.class);
        Option Option = OptionBuilder.create('n');
        assertEquals("newLongopt", Option.getLongOpt());
        assertTrue(Option.isRequired());
        assertEquals(1, Option.getArgs().length);
        assertEquals("argName", Option.getArgName());
        assertEquals(';', Option.getValueSeparator());
        assertEquals(Option.VALUE_OPTIONAL, Option.getArgs()[0].getArgsType());
        assertEquals(Option.TYPE_INT, Option.getType());
    }

    @Test
    public void testCreate_string() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("newLongopt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.withValueSeparator(';');
        OptionBuilder.hasArgs(3);
        OptionBuilder.withType(String[].class);
        Option Option = OptionBuilder.create("n");
        assertEquals("newLongopt", Option.getLongOpt());
        assertTrue(Option.isRequired());
        assertEquals(1, Option.getArgs().length);
        assertEquals("argName", Option.getArgName());
        assertEquals(';', Option.getValueSeparator());
        assertEquals(Option.VALUE_ARRAY_REQUIRED, Option.getArgs()[0].getArgsType());
        assertEquals(Option.TYPE_STRING, Option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() throws IllegalArgumentException {
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }
}