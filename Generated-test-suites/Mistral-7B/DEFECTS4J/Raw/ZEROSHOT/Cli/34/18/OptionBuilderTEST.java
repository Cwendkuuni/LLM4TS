package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithBasicOptions() throws Exception
    {
        Option option = OptionBuilder.withLongOpt("opt")
                .hasArg()
                .withArgName("arg")
                .isRequired()
                .create('o');

        assertEquals("option with required argument 'arg'", option.getLongOpt(), "opt");
        assertEquals("option description not set", option.getDescription(), null);
        assertEquals("option argument name is 'arg'", option.getArgName(), "arg");
        assertEquals("option is required", option.isRequired(), true);
        assertEquals("option can have one argument", option.getArgs().length, 1);
        assertNull("option type is not set", option.getType());
        assertNull("option value separator is not set", option.getValueSeparator());

        Option optionNoArg = OptionBuilder.withLongOpt("opt2")
                .isRequired()
                .create('p');

        assertEquals("option with no argument", optionNoArg.getLongOpt(), "opt2");
        assertEquals("option description not set", optionNoArg.getDescription(), null);
        assertEquals("option argument name is not set", optionNoArg.getArgName(), null);
        assertEquals("option is required", optionNoArg.isRequired(), true);
        assertEquals("option can have no arguments", optionNoArg.getArgs().length, 0);
        assertNull("option type is not set", optionNoArg.getType());
        assertNull("option value separator is not set", optionNoArg.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() throws Exception
    {
        Option option = OptionBuilder.withLongOpt("opt")
                .hasArg()
                .withArgName("arg")
                .isRequired()
                .withValueSeparator(':')
                .create('o');

        assertEquals("option with required argument 'arg' and value separator ':'", option.getLongOpt(), "opt");
        assertEquals("option description not set", option.getDescription(), null);
        assertEquals("option argument name is 'arg'", option.getArgName(), "arg");
        assertEquals("option is required", option.isRequired(), true);
        assertEquals("option can have one argument", option.getArgs().length, 1);
        assertEquals("option value separator is ':'", option.getValueSeparator(), ':');
        assertNull("option type is not set", option.getType());

        Option optionWithValue = OptionBuilder.withLongOpt("opt2")
                .hasArg()
                .withArgName("arg")
                .withValueSeparator()
                .isRequired()
                .create('p');

        assertEquals("option with required argument 'arg' and default value separator", optionWithValue.getLongOpt(), "opt2");
        assertEquals("option description not set", optionWithValue.getDescription(), null);
        assertEquals("option argument name is 'arg'", optionWithValue.getArgName(), "arg");
        assertEquals("option is required", optionWithValue.isRequired(), true);
        assertEquals("option can have one argument", optionWithValue.getArgs().length, 1);
        assertNull("option type is not set", optionWithValue.getType());
        assertNull("option value separator is not set by default", optionWithValue.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithTypes() throws Exception
    {
        Option optionInt = OptionBuilder.withLongOpt("opt")
                .hasArg()
                .withArgName("arg")
                .isRequired()
                .withType(Integer.class)
                .create('o');

        assertEquals("option with required integer argument 'arg'", optionInt.getLongOpt(), "opt");
        assertEquals("option description not set", optionInt.getDescription(), null);
        assertEquals("option argument name is 'arg'", optionInt.getArgName(), "arg");
        assertEquals("option is required", optionInt.isRequired(), true);
        assertEquals("option can have one integer argument", optionInt.getArgs().length, 1);
        assertEquals("option type is Integer", optionInt.getType().equals(Integer.class), true);
        assertNull("option value separator is not set", optionInt.getValueSeparator());

        Option optionBoolean = OptionBuilder.withLongOpt("opt2")
                .hasArg()
                .withArgName("arg")
                .isRequired()
                .withType(Boolean.class)
                .create('p');

        assertEquals("option with required boolean argument 'arg'", optionBoolean.getLongOpt(), "opt2");
        assertEquals("option description not set", optionBoolean.getDescription(), null);
        assertEquals("option argument name is 'arg'", optionBoolean.getArgName(), "arg");
        assertEquals("option is required", optionBoolean.isRequired(), true);
        assertEquals("option can have one boolean argument", optionBoolean.getArgs().length, 1);
        assertEquals("option type is Boolean", optionBoolean.getType().equals(Boolean.class), true);
        assertNull("option value separator is not set", optionBoolean.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithOptionalArgs() throws Exception
    {
        Option optionOptionalArg = OptionBuilder.withLongOpt("opt")
                .hasOptionalArg()
                .withArgName("arg")
                .create('o');

        assertEquals("option with optional argument 'arg'", optionOptionalArg.getLongOpt(), "opt");
        assertEquals("option description not set", optionOptionalArg.getDescription(), null);
        assertEquals("option argument name is 'arg'", optionOptionalArg.getArgName(), "arg");
        assertEquals("option can have optional argument", optionOptionalArg.isRequired(), false);
        assertEquals("option can have unlimited arguments", optionOptionalArg.getArgs().length, Option.UNLIMITED_VALUES);
        assertNull("option type is not set", optionOptionalArg.getType());
        assertNull("option value separator is not set", optionOptionalArg.getValueSeparator());

        Option optionArgs = OptionBuilder.withLongOpt("opt2")
                .hasArgs()
                .withArgName("args")
                .create('a');

        assertEquals("option with unlimited arguments 'args'", optionArgs.getLongOpt(), "opt2");
        assertEquals("option description not set", optionArgs.getDescription(), null);
        assertEquals("option argument name is 'args'", optionArgs.getArgName(), "args");
        assertEquals("option can have unlimited arguments", optionArgs.isRequired(), false);
        assertEquals("option can have unlimited arguments", optionArgs.getArgs().length, Option.UNLIMITED_VALUES);
        assertNull("option type is not set", optionArgs.getType());
        assertNull("option value separator is not set", optionArgs.getValueSeparator());
    }
}