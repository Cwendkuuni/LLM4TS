package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
        assertEquals(Character.MIN_VALUE, OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt");
        assertEquals("opt", OptionBuilder.longopt);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testHasArg_boolean() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testWithValueSeparator_empty() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
        assertSame(optionBuilder, OptionBuilder.instance);
    }

    @Test
    public void testCreate_char() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt");
        Option option = OptionBuilder.create('o');
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        assertEquals(null, option.getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
        assertFalse(option.isOptional());

        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt")
                .withArgName("arg")
                .isRequired(false);
        Option option1 = OptionBuilder.create('o');
        assertEquals("opt", option1.getLongOpt());
        assertFalse(option1.isRequired());
        assertEquals(Option.ONE_ARGUMENT, option1.getArgs());
        assertNull(option1.getValue());
        assertNull(option1.getType());
        assertEquals("arg", option1.getArgName());
        assertTrue(option1.isOptional());
    }

    @Test
    public void testCreate_string() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt");
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
        assertFalse(option.isOptional());

        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt")
                .withArgName("arg")
                .isRequired(false);
        Option option1 = OptionBuilder.create("opt");
        assertEquals("opt", option1.getLongOpt());
        assertFalse(option1.isRequired());
        assertEquals(Option.ONE_ARGUMENT, option1.getArgs());
        assertNull(option1.getValue());
        assertNull(option1.getType());
        assertEquals("arg", option1.getArgName());
        assertTrue(option1.isOptional());
    }
}