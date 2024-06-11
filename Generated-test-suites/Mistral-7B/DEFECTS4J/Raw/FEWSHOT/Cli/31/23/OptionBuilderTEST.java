package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionWithLongOptAndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("A long option");
        Option option = optionBuilder.create();
        assertEquals("A long option", option.getDescription());
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create();
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionWithUnlimitedArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionWithSingleArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testOptionWithOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType("java.lang.Integer");
        Option option = optionBuilder.create();
        assertNotNull(option.getType());
        assertEquals("java.lang.Integer", option.getType().getClass().getName());
    }

    @Test
    public void testOptionWithNoLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.reset();
        Option option = optionBuilder.create();
        assertNull(option.getLongOpt());
    }
}