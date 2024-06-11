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
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptOption");
        assertEquals("longOptOption", OptionBuilder.longopt);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testWithValueSeparatorNoArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
        assertEquals(OptionBuilder.class, optionBuilder.getClass());
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptOption")
                .hasArg().isRequired().withValueSeparator(';');
        Option option = optionBuilder.create('L');
        assertEquals("longOptOption", option.getLongOpt());
        assertEquals(Option.REQUIRED_ARG, option.getOptType());
        assertEquals(1, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.isRequired());
        assertNull(option.getValue());
        assertEquals("longOptOption", option.getOpt());

        optionBuilder = OptionBuilder.withLongOpt("longOptOption")
                .hasArg(true).isRequired().withValueSeparator(';');
        option = optionBuilder.create('L');
        assertEquals("longOptOption", option.getLongOpt());
        assertEquals(Option.OPTIONAL_ARG, option.getOptType());
        assertEquals(1, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.isRequired());
        assertNull(option.getValue());
        assertEquals("longOptOption", option.getOpt());

        optionBuilder = OptionBuilder.withLongOpt("longOptOption")
                .hasArgs(3).isRequired().withValueSeparator(';');
        option = optionBuilder.create('L');
        assertEquals("longOptOption", option.getLongOpt());
        assertEquals(Option.MULTI_VAL_ARG, option.getOptType());
        assertEquals(3, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.isRequired());
        assertNull(option.getValue());
        assertEquals("longOptOption", option.getOpt());
    }

    @Test
    public void testCreateWithString() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptOption")
                .hasArg().isRequired().withValueSeparator(';');
        Option option = optionBuilder.create("longOptOption");
        assertEquals("longOptOption", option.getLongOpt());
        assertEquals(Option.REQUIRED_ARG, option.getOptType());
        assertEquals(1, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.isRequired());
        assertNull(option.getValue());
        assertEquals("longOptOption", option.getOpt());

        optionBuilder = OptionBuilder.withLongOpt("longOptOption")
                .hasArg(true).isRequired().withValueSeparator(';');
        option = optionBuilder.create("longOptOption");
        assertEquals("longOptOption", option.getLongOpt());
        assertEquals(Option.OPTIONAL_ARG, option.getOptType());
        assertEquals(1, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.isRequired());
        assertNull(option.getValue());
        assertEquals("longOptOption", option.getOpt());

        optionBuilder = OptionBuilder.withLongOpt("longOptOption")
                .hasArgs(3).isRequired().withValueSeparator(';');
        option = optionBuilder.create("longOptOption");
        assertEquals("longOptOption", option.getLongOpt());
        assertEquals(Option.MULTI_VAL_ARG, option.getOptType());
        assertEquals(3, option.getArgs().length);
        assertEquals(';', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.isRequired());
        assertNull(option.getValue());
        assertEquals("longOptOption", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOptWithChar() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.create('L');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOptWithString() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.create("longOptOption");
    }
}