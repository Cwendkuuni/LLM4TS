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
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\u0000', OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("myOption");
        assertEquals("myOption", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithHasArgFalse() {
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
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
        char separator = ':';
        OptionBuilder.withValueSeparator(separator);
        assertEquals(separator, OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
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
    public void testHasArgsWithNumber() {
        int numArgs = 3;
        OptionBuilder.hasArgs(numArgs);
        assertEquals(numArgs, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        int numArgs = 4;
        OptionBuilder.hasOptionalArgs(numArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(numArgs, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        Class<?> type = String.class;
        OptionBuilder.withType(type);
        assertSame(type, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        String description = "Option description";
        OptionBuilder.withDescription(description);
        assertEquals(description, OptionBuilder.description);
    }

    @Test
    public void testCreateWithChar() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("myOption");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.isRequired();
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('o');

        assertEquals("myOption", option.getLongOpt());
        assertEquals(Option.REQUIRED_ARG, option.getArgPosition());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateWithString() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("myOption");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.isRequired();
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create("o");

        assertEquals("myOption", option.getLongOpt());
        assertEquals(Option.REQUIRED_ARG, option.getArgPosition());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.create();
    }
}