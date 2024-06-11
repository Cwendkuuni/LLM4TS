package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.util.Arrays;

public class OptionBuilderTest {

    @org.junit.Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create('l');
        assertEquals("longOpt", option.getLongOpt());
    }

    @org.junit.Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        assertEquals(1, option.getArgs().length);
    }

    @org.junit.Test
    public void testHasArgWithName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create('a');
        assertEquals("argName", option.getArgName());
    }

    @org.junit.Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('r');
        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('o');
        assertTrue(option.isOptional());
    }

    @org.junit.Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('h');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @org.junit.Test
    public void testHasArgsWithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create('n');
        assertEquals(3, option.getArgs().length);
    }

    @org.junit.Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('s');
        assertEquals(':', option.getValueSeparator());
    }

    @org.junit.Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option option = optionBuilder.create('s');
        assertEquals('=', option.getValueSeparator());
    }

    @org.junit.Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create('s');
        assertEquals(String.class, option.getType());
    }

    @org.junit.Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option option = optionBuilder.create('d');
        assertEquals("description", option.getLongOpt());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.create('x');
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("x");
        Option option = optionBuilder.create('x');
    }

    @org.junit.Test
    public void testCreateWithOptionalArgAndValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().withValueSeparator(':');
        Option option = optionBuilder.create('p');
        String[] args = {"-p", "value1:value2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(args);
        Option opt = cmd.getOption("p");
        assertEquals("value1", opt.getValue());
        assertEquals("value2", opt.getValues()[0]);
    }

    @org.junit.Test
    public void testCreateWithMultipleArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('m');
        String[] args = {"-m", "arg1", "arg2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(args);
        Option opt = cmd.getOption("m");
        assertEquals(Arrays.asList(new String[]{"arg1", "arg2"}), opt.getValues());
    }
}