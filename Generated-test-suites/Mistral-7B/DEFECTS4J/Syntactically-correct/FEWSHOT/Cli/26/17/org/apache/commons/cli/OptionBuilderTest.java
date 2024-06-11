package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("long-opt");
        assertEquals("long-opt", OptionBuilder.longopt);
    }

    @Test
    public void testCreateWithNoArgs() {
        OptionBuilder builder = new OptionBuilder();
        Option option = builder.create('A');
        assertEquals("A", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithOneArg() {
        OptionBuilder builder = new OptionBuilder();
        Option option = builder.create('A').hasArg();
        assertEquals("A", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(Option.REQUIRED_ARGUMENT, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithOptionalArg() {
        OptionBuilder builder = new OptionBuilder();
        Option option = builder.create('A').hasOptionalArg();
        assertEquals("A", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(Option.OPT_ARG, option.getArgs());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithValueSeparator() {
        OptionBuilder builder = new OptionBuilder();
        Option option = builder.create('A').withValueSeparator(':');
        assertEquals("A", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithDescription() {
        OptionBuilder builder = new OptionBuilder().withDescription("An option with a description");
        Option option = builder.create('A');
        assertEquals("An option with a description", option.getDescription());
        assertEquals("A", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        OptionBuilder builder = new OptionBuilder().withLongOpt("long-opt").withDescription("A long option with a description");
        Option option = builder.create('A');
        assertEquals("A", option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("A long option with a description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptArgAndDescription() {
        OptionBuilder builder = new OptionBuilder().withLongOpt("long-opt")
                .withArgName("value")
                .withDescription("An option that takes a value");
        Option option = builder.create('A');
        assertEquals("A", option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("An option that takes a value", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.REQUIRED_ARGUMENT, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptArgValueSeparatorAndDescription() {
        OptionBuilder builder = new OptionBuilder().withLongOpt("long-opt")
                .withArgName("value")
                .withValueSeparator(':')
                .withDescription("An option that takes multiple values");
        Option option = builder.create('A');
        assertEquals("A", option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("An option that takes multiple values", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptArgValueSeparatorTypeAndDescription() {
        OptionBuilder builder = new OptionBuilder().withLongOpt("long-opt")
                .withArgName("value")
                .withValueSeparator(':')
                .withType(String.class)
                .withDescription("An option that takes multiple values of type String");
        Option option = builder.create('A');
        assertEquals("A", option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("An option that takes multiple values of type String", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(':', option.getValueSeparator());
        assertSame(String.class, option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptArgValueSeparatorTypeAndValue() {
        OptionBuilder builder = new OptionBuilder().withLongOpt("long-opt")
                .withArgName("value")
                .withValueSeparator(':')
                .withType(String.class)
                .isRequired()
                .create('A', "value1:value2");
        Option option = builder.create('A');
        assertEquals("A", option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertNull(option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(':', option.getValueSeparator());
        assertSame(String.class, option.getType());
        assertEquals(Arrays.asList("value1", "value2"), option.getValues());
    }
}