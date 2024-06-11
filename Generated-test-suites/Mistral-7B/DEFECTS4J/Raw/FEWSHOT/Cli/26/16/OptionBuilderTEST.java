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
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertSame(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertSame(Option.UNINITIALIZED, OptionBuilder.optionalArg);
        assertSame((char) 0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals(instance, OptionBuilder.instance);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharInvalid() {
        OptionBuilder.reset();
        OptionBuilder.create('%');
    }

    @Test
    public void testCreate() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("longOpt")
            .withDescription("description")
            .withArgName("argName")
            .hasArg(true)
            .isRequired(true)
            .withValueSeparator(';')
            .withType(Integer.class);
        Option option = OptionBuilder.create('o');
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(true, option.isOptionalArg());
        assertEquals(';', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());
    }
}