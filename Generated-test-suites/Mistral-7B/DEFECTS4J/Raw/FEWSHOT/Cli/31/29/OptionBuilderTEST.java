package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        OptionBuilder.instance = null;
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.longopt);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithValueSeparatorEmpty() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderIsRequiredFalse() {
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgsInt() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder.withType(Integer.TYPE);
        assertEquals(Integer.TYPE, OptionBuilder.type);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testOptionBuilderCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(';');
        OptionBuilder.hasArgs();

        Option option = OptionBuilder.create('o');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_OPTIONAL, option.getOpt());
        assertEquals(1, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderCreateWithValue() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(';');
        OptionBuilder.hasArgs(3);

        Option option = OptionBuilder.create('o');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getOpt());
        assertEquals(3, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals(new String[]{"arg1", "arg2", "arg3"}, option.getArgs());
        assertNull(option.getValue());

        OptionBuilder.reset();
        option.setValue("value");
        assertEquals("value", option.getValue());
    }
}