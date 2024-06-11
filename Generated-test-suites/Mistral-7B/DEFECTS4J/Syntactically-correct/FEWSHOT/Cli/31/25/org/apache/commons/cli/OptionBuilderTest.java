package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultOption() {
        Option option = OptionBuilder.create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(Option.NO_VALUE_SEPARATOR, OptionBuilder.valuesep);
        assertNotNull(option);
    }

    @Test
    public void testHasArgOption() {
        OptionBuilder.hasArg();
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.type);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.longopt);
    }

    @Test
    public void testHasArgOptionalArgOption() {
        OptionBuilder.hasArg(true);
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.type);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.longopt);
    }

    @Test
    public void testHasOptionalArgsOption() {
        OptionBuilder.hasArgs();
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.type);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.longopt);
    }

    @Test
    public void testHasOptionalArgsWithLimitOption() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.type);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.longopt);
    }

    @Test
    public void testWithLongOptOption() {
        OptionBuilder.withLongOpt("myOption");
        assertEquals("myOption", OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testIsRequiredOption() {
        OptionBuilder.isRequired();
        assertEquals("", OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertTrue(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testHasValueSeparatorOption() {
        OptionBuilder.withValueSeparator(':');
        assertEquals("", OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertEquals(':', OptionBuilder.valuesep);
        assertFalse(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testHasTypeOption() {
        OptionBuilder.withType(String.class);
        assertEquals("", OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(String.class, OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testHasDescriptionOption() {
        OptionBuilder.withDescription("My Option Description");
        assertEquals("My Option Description", OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertNull(OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testCreateOption() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create('o');
        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue(0));
    }

    @Test
    public void testCreateOptionWithArgValue() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create('o', "value");
        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
        assertEquals("value", option.getValue(0));
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithMultipleArgs() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create('o');
        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getValue(0));
        assertNull(option.getValue(1));
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithOptionalArg() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create('o');
        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValue(0));
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithOptionalArgs() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create('o');
        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getValue(0));
        assertNull(option.getValue(1));
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithValueSeparator() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('o');
        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
        assertEquals("", option.getValue(0));
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithType() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create('o');
        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
        assertEquals(Integer.class, option.getType());
        assertNull(option.getValue(0));
        assertNull(option.getValueSeparator());
    }
}