package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOpt");
        assertEquals("testOpt", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());

        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.getRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.getRequired());

        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.getRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilder.getOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilder.getOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilder.getOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        assertEquals("test description", OptionBuilder.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithChar_InvalidChar() {
        OptionBuilder.create(' ');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_NullLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(null, option.getType());
        assertEquals('\u0000', option.getValueSeparator());
        assertEquals(null, option.getArgName());
    }

    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("testOpt");
        assertEquals("testOpt", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(null, option.getType());
        assertEquals('\u0000', option.getValueSeparator());
        assertEquals(null, option.getArgName());
    }

    @Test
    public void testCreateWithOptAndDescription() {
        Option option = OptionBuilder.create("testOpt", "test description");
        assertEquals("testOpt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("test description", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(null, option.getType());
        assertEquals('\u0000', option.getValueSeparator());
        assertEquals(null, option.getArgName());
    }
}