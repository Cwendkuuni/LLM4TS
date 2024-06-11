package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "option description");
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("arg", "option description")
                .isRequired(true);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testHasArg_hasArgFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Option expectedOption = new Option("arg", "option description");
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("newArgName");
        Option expectedOption = new Option("arg", "option description")
                .argName("newArgName");
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("arg", "option description")
                .isRequired(true);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("arg", "option description")
                .setValueSeparator(':');
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testWithValueSeparator_default() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option expectedOption = new Option("arg", "option description");
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testIsRequired_hasArgFalse() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Option expectedOption = new Option("arg", "option description");
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("arg", "option description")
                .numberOfArgs(Option.UNLIMITED_VALUES);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testHasArgs_numArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        Option expectedOption = new Option("arg", "option description")
                .numberOfArgs(5);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", "option description")
                .numberOfArgs(1)
                .optionalArg(true);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = new Option("arg", "option description")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .optionalArg(true);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testHasOptionalArgs_numArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option expectedOption = new Option("arg", "option description")
                .numberOfArgs(3)
                .optionalArg(true);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = new Option("arg", "option description")
                .type(Integer.class);
        assertEquals(expectedOption, optionBuilder.create());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("option description");
        Option expectedOption = new Option("arg", "option description");
        assertEquals(expectedOption, optionBuilder.create());
    }
}