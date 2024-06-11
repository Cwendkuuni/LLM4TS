package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", OptionBuilder.longopt);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderHasArgBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderHasArgsInt() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(3, OptionBuilder.numberOfArgs);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
        OptionBuilder.reset();
    }

    @Test
    public void testCreateOption() {
        OptionBuilder.withLongOpt("longopt").hasArg().withArgName("argName")
                .isRequired().withValueSeparator(':').hasArgs(5);
        Option option = OptionBuilder.create('o');
        assertEquals("longopt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(5, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
        assertFalse(option.isOptional());
        assertNotNull(option.getValue(0));

        OptionBuilder.reset();
    }

    @Test
    public void testCreateOptionWithOptionalArg() {
        OptionBuilder.withLongOpt("longopt").hasOptionalArg();
        Option option = OptionBuilder.create('o');
        assertEquals("longopt", option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getValue(0));
        assertNull(option.getValue(1));

        OptionBuilder.reset();
    }

    @Test
    public void testCreateOptionWithNoArg() {
        Option option = OptionBuilder.create('o');
        assertEquals(null, option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getValue(0));
        assertNull(option.getValue(1));

        OptionBuilder.reset();
    }
}