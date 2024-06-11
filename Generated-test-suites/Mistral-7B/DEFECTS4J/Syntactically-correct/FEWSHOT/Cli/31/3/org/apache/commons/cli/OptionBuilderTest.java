package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("newLongopt");
        assertEquals("newLongopt", OptionBuilder.longopt);
        assertSame(OptionBuilder.instance, builder);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg();
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasArgWithName() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg().withArgName("newName");
        assertNull(OptionBuilder.longopt);
        assertEquals("newName", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.isRequired();
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasArgsWithNumArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArgs(5);
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(5);
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertTrue(OptionBuilder.required);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withType("java.lang.Integer");
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);

        assertEquals("java.lang.Integer", OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withDescription("New option description");
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertSame(OptionBuilder.instance, builder);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);

        assertEquals("New option description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("newLongopt")
                .isRequired()
                .hasArgWithName("newName")
                .withDescription("New option description")
                .withType(Integer.class);

        Option option = builder.create();

        assertEquals("newLongopt", option.getLongOpt());
        assertEquals("New option description", option.getDescription());
        assertEquals("newName", option.getArgName());
        assertEquals(true, option.isRequired());
        assertEquals(true, option.hasArg());
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.isRequired()
                .hasArgWithName("newName")
                .withDescription("New option description")
                .withType(Integer.class);

        OptionBuilder.create();
    }
}