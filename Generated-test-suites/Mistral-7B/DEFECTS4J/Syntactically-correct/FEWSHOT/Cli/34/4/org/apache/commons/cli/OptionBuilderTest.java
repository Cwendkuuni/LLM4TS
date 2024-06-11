package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
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
    }

    @Test
    public void testLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.longopt);
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
        OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.argName);
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
    public void testWithValueSeparatorEmpty() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
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
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertNotNull(OptionBuilder.type);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("testOpt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.withType(Integer.class);

        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals("testOpt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgCount());
        assertEquals(':', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }
}