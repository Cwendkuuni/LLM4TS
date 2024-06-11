package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgBoolean() {
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
        assertTrue(OptionBuilder.isRequired());

        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.getDescription());
    }

    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());

        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());

        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());

        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());

        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.getType());
    }
}