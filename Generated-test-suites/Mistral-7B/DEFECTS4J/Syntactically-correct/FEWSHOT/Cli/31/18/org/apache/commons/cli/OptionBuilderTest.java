package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
        OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_false() {
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
    public void testWithValueSeparator_char() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_false() {
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
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
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(2);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType_Integer() {
        OptionBuilder.withType(Integer.class);
        assertSame(Integer.class, OptionBuilder.type);
    }

    @Test
    public void testWithType_String() {
        OptionBuilder.withType(String.class);
        assertSame(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate_char() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longopt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.withValueSeparator(':');

        Option option = OptionBuilder.create('o');
        assertEquals("longopt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals("argName", option.getArgName());
        assertEquals(':', option.getValueSeparator());

        OptionBuilder.reset();
    }

    @Test
    public void testCreate_String() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longopt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.withValueSeparator(':');

        Option option = OptionBuilder.create("o");
        assertEquals("longopt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals("argName", option.getArgName());
        assertEquals(':', option.getValueSeparator());

        OptionBuilder.reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() throws IllegalArgumentException {
        OptionBuilder.hasArg();
        OptionBuilder.create('o');
    }
}