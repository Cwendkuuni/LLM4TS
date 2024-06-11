package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
        OptionBuilder.instance = null;
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.longopt);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgWithName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgsWithValue() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithValue() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder.withType("String.class");
        assertEquals("String.class", OptionBuilder.type.toString());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder.withDescription("Option Description");
        assertEquals("Option Description", OptionBuilder.description);
    }

    @Test
    public void testOptionCreation() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longOpt")
            .hasArg()
            .isRequired()
            .withValueSeparator(':')
            .hasArgs()
            .withType(Integer.class)
            .withDescription("Option Description");

        Option option = OptionBuilder.create('o');
        assertNotNull(option);
        assertEquals("Option Description", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionCreationWithNoLongOpt() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder.hasArg()
            .withValueSeparator(':')
            .hasArgs()
            .withType(Integer.class)
            .withDescription("Option Description");

        try {
            OptionBuilder.create(' ');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }
}