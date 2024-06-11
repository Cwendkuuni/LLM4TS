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
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(false, OptionBuilder.optionalArg);
        assertEquals('\u0000', OptionBuilder.valuesep);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("longOption");
        assertEquals("longOption", OptionBuilder.longopt);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testHasArgWithArgumentName() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArg().withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
        assertEquals(OptionBuilder.instance, instance);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("longOption")
                .isRequired()
                .hasArg()
                .withValueSeparator('=')
                .withType(Integer.class)
                .withDescription("description");

        Option option = OptionBuilder.create('l');
        assertEquals("longOption", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals('=', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder.create();
    }
}