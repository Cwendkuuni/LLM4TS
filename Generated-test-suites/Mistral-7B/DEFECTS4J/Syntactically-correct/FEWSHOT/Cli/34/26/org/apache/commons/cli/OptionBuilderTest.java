package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder.withLongOpt("optionLongopt");
        assertEquals("optionLongopt", OptionBuilder.longopt);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder.hasArg();
        assertEquals(Option.UNINITIALIZED, OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgWithArgName() {
        OptionBuilder.hasArg().withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgsWithNumArgs() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
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
    public void testOptionBuilderHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder.withDescription("Option description");
        assertEquals("Option description", OptionBuilder.description);
    }

    @Test
    public void testCreateOptionWithDefaultSettings() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateOptionWithLongOpt() {
        OptionBuilder.withLongOpt("optionLongopt");
        Option option = OptionBuilder.create('o');
        assertEquals("optionLongopt", option.getLongOpt());
    }

    @Test
    public void testCreateOptionWithArgName() {
        OptionBuilder.hasArg().withArgName("argName");
        Option option = OptionBuilder.create('o');
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testCreateOptionWithRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create('o');
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateOptionWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create('o');
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreateOptionWithArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create('o');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testCreateOptionWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create('i');
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreateOptionWithDescription() {
        OptionBuilder.withDescription("Option description");
        Option option = OptionBuilder.create('o');
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testCreateOptionWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('D');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithArgNameAndValueSeparator() {
        OptionBuilder.hasArg().withArgName("argName").withValueSeparator(':');
        Option option = OptionBuilder.create('D');
        assertEquals("argName", option.getArgName());
        assertEquals(':', option.getValueSeparator());
    }
}