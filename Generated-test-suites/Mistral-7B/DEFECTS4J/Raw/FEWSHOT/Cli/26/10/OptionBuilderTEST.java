package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption");
        assertEquals("longOption", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_boolean() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator_noArg() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withType(Integer.class);
        assertNotNull(OptionBuilder.type);
        assertEquals(Integer.class, OptionBuilder.type.getClass());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("description")
                .isRequired()
                .hasArg()
                .withValueSeparator(':')
                .withType(Integer.class);
        Option option = builder.create('o');
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
        assertEquals(Integer.class, option.getType().getClass());
        assertEquals("arg", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg();
        try {
            Option option = builder.create((char) 0);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
        fail("Exception expected: must specify long opt");
    }
}