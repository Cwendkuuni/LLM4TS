package org.apache.commons.cli;

import static org.junit.Assert.*;

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
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("newLongopt");
        assertEquals("newLongopt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_hasArgFalse() {
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        assertEquals("name", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_numArgs() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_numArgs() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longopt")
                .withDescription("description")
                .hasArg()
                .isRequired();
        Option option = OptionBuilder.create('o');
        assertEquals("longopt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() throws IllegalArgumentException {
        OptionBuilder.create('o');
    }
}