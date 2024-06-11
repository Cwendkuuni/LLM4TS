package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();

        assertEquals(null, OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(false, OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
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
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();

        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("myArg");

        assertEquals("myArg", OptionBuilder.argName);
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
    public void testHasArgs() {
        OptionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(2);

        assertEquals(2, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testCreateWithChar() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("myOption");
        OptionBuilder.isRequired();

        Option option = OptionBuilder.create('m');

        assertEquals("myOption", option.getLongOpt());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateWithString() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("myOption");
        OptionBuilder.isRequired();

        Option option = OptionBuilder.create("m");

        assertEquals("myOption", option.getLongOpt());
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.create();
    }
}