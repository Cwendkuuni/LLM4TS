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
        assertEquals(null, OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("myOpt");
        assertEquals("myOpt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean() {
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
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorNoArg() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
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
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertSame(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder.hasArg();
        OptionBuilder.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create("*");
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();

        Option option = OptionBuilder.create('o');

        assertEquals("opt", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getNumberOfargs());
        assertNull(option.getType());
        assertEquals("arg", option.getArgName());
        assertSame('o', option.getValue());
    }

    @Test
    public void testCreateWithValue() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withValueSeparator(',');

        Option option = OptionBuilder.create('o');

        assertEquals("opt", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getNumberOfargs());
        assertNull(option.getType());
        assertEquals("arg", option.getArgName());
        assertEquals("", option.getValue());

        option.setValue("value1");
        assertEquals("value1", option.getValue());

        option.setValue("value1,value2");
        assertEquals("value1,value2", option.getValue());
    }

    @Test
    public void testCreateWithValueType() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("opt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withType(Integer.class);

        Option option = OptionBuilder.create('o');

        assertEquals("opt", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getNumberOfargs());
        assertSame(Integer.class, option.getType());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }
}