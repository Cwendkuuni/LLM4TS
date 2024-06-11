package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_boolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
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
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator_noArg() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        String className = "java.lang.String.class";
        OptionBuilder.withType(className);
        assertEquals(className, OptionBuilder.type.getName());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate_char() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.isRequired();
        OptionBuilder.withArgName("argName");
        OptionBuilder.hasArgs(5);
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.withType(String.class);
        Option BuilderOption = OptionBuilder.create('o');
        assertNotNull(BuilderOption);
        assertEquals("longOpt", BuilderOption.getLongOpt());
        assertTrue(BuilderOption.isRequired());
        assertEquals("argName", BuilderOption.getArgName());
        assertEquals(5, BuilderOption.getArgs().length);
        assertEquals(':', BuilderOption.getValueSeparator());
        assertEquals(String.class, BuilderOption.getType());
    }

    @Test
    public void testCreate_String() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.isRequired();
        OptionBuilder.withArgName("argName");
        OptionBuilder.hasArgs(5);
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.withType(String.class);
        Option BuilderOption = OptionBuilder.create("o");
        assertNotNull(BuilderOption);
        assertEquals("longOpt", BuilderOption.getLongOpt());
        assertTrue(BuilderOption.isRequired());
        assertEquals("argName", BuilderOption.getArgName());
        assertEquals(5, BuilderOption.getArgs().length);
        assertEquals(':', BuilderOption.getValueSeparator());
        assertEquals(String.class, BuilderOption.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() throws IllegalArgumentException {
        OptionBuilder.withType(String.class);
        OptionBuilder.create('x');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_nullLongOpt() throws IllegalArgumentException {
        OptionBuilder.withType(String.class);
        OptionBuilder.create((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noDescription() throws IllegalArgumentException {
        OptionBuilder.withType(String.class);
        OptionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_nullDescription() throws IllegalArgumentException {
        OptionBuilder.withType(String.class);
        OptionBuilder.create("o", (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noArgName() throws IllegalArgumentException {
        OptionBuilder.withType(String.class);
        OptionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_nullArgName() throws IllegalArgumentException {
        OptionBuilder.withType(String.class);
        OptionBuilder.create("o", (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noType() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.create('o');
    }

}