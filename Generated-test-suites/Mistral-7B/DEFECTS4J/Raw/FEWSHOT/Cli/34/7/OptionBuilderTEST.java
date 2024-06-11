package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertNull(OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", OptionBuilder.longopt);
        assertNotNull(OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertNotNull(OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertNotNull(OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
        assertNotNull(OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
        assertNotNull(OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
        assertNotNull(OptionBuilder.instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longopt");
        OptionBuilder.hasArg();
        OptionBuilder.create('o');
    }
}