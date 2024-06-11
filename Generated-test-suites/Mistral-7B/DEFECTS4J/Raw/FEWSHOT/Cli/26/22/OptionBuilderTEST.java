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
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArg();
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArg().withArgName("name");
        assertEquals("name", OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.isRequired();
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArg();
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withType(String.class);
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(String.class, OptionBuilder.type);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withDescription("test description");
        assertEquals("test description", OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("test").withDescription("test description")
                .hasArg().withArgName("arg");
        Option option = OptionBuilder.create('t');
        assertEquals("test description", option.getDescription());
        assertEquals("arg", option.getArgumentName());
        assertEquals("test", option.getLongOpt());
        assertEquals(Option.VALUE_REQUIRED, option.getArgType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithValueSeparator() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("test").withDescription("test description")
                .hasArg().withArgName("arg").withValueSeparator(':');
        Option option = OptionBuilder.create('t');
        assertEquals("test description", option.getDescription());
        assertEquals("arg", option.getArgumentName());
        assertEquals("test", option.getLongOpt());
        assertEquals(Option.VALUE_REQUIRED, option.getArgType());
        assertEquals(Option.TYPE_VALUE_SEPARATED, option.getValueSeparator());
    }
}