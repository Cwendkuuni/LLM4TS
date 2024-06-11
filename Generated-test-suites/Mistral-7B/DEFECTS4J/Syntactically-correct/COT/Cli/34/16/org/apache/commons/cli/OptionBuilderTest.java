package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;

public class OptionBuilderTest {

    @org.junit.Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = optionBuilder.create();
        assertEquals("optionLong", option.getLongOpt());
    }

    @org.junit.Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
    }

    @org.junit.Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    @org.junit.Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @org.junit.Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @org.junit.Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());
    }

    @org.junit.Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @org.junit.Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();
        assertEquals(3, option.getArgs());
        assertTrue(option.isOptional());
    }

    @org.junit.Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @org.junit.Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        Option option = optionBuilder.create();
        assertEquals("Option description", option.getDescription());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        OptionBuilder.reset();
        throw new AssertionError("Expected IllegalArgumentException");
    }
}