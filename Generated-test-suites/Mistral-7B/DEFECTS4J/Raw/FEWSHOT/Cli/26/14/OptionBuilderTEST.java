package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create('L');
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('H');
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('O');
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create('A');
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('R');
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create('V');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('G');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create('I');
        assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('U');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        Option option = optionBuilder.create('J');
        assertEquals(5, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('N');
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option option = optionBuilder.create('D');
        assertEquals("description", option.getDescription());
    }
}