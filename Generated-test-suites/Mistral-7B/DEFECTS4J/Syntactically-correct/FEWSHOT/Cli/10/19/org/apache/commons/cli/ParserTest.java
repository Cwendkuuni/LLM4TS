package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = { "-a", "--option-b=value", "arg1", "arg2", "--" };
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertEquals("arg1", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));

        List<Option> optionsList = options.getOptions();
        Option optionA = optionsList.get(0);
        Option optionB = optionsList.get(1);

        assertEquals("Option A", optionA.getLongOpt());
        assertEquals('a', optionA.getValue());
        assertEquals(Option.NO_ARGUMENT, optionA.getOptType());
        assertFalse(optionA.isRequired());

        assertEquals("Option B", optionB.getLongOpt());
        assertEquals('b', optionB.getValue());
        assertEquals(Option.VALUE_Optional, optionB.getOptType());
        assertTrue(optionB.isRequired());
        assertEquals("value", optionB.getValueDescription());
        assertEquals("Value for Option B", optionB.getDescription());

        assertEquals("value", cmd.getOptionValue("option-b"));
        assertNull(cmd.getOptionValue("unknown-option"));

        List<String> argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testParseWithMultipleRequiredOptions() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = { "--option1", "--option2" };
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.setRequiredOptions(Arrays.asList("option1", "option2"));

        String[] args = new String[0];
        assertThrows(MissingOptionException.class,
                () -> parser.parse(options, args));

        args = new String[2];
        args[0] = "--option2";
        assertThrows(MissingOptionException.class,
                () -> parser.parse(options, args));

        args = new String[2];
        args[0] = "--option1";
        assertThrows(MissingOptionException.class,
                () -> parser.parse(options, args));

        args = new String[3];
        args[0] = "--option1";
        args[1] = "--option2";
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");

        String[] arguments = { "--", "--", "--property1=value1", "--property2=value2" };
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertEquals(0, cmd.getNumArgs());

        List<Option> optionsList = options.getOptions();
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);

        assertEquals("Option 1", option1.getLongOpt());
        assertEquals('1', option1.getValue());
        assertEquals(Option.NO_ARGUMENT, option1.getOptType());
        assertFalse(option1.isRequired());
        assertEquals("Value 1 for Option 1", option1.getDescription());

        assertEquals("Option 2", option2.getLongOpt());
        assertEquals('2', option2.getValue());
        assertEquals(Option.NO_ARGUMENT, option2.getOptType());
        assertFalse(option2.isRequired());
        assertEquals("Value 2 for Option 2", option2.getDescription());

        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testParseWithValueSeparatedByEquals() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = { "--property=value1=value2" };
        CommandLine cmd = parser.parse(options, arguments, null);

        assertEquals(0, cmd.getNumArgs());

        Properties properties = cmd.getOptionProperties();
        assertEquals("value1", properties.getProperty("property"));
        assertEquals("value2", properties.getProperty("property"));
    }

    @Test
    public void testParseWithGroup() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.setRequired(true);

        Option option1 = new Option("option1", "o1", false, "o");
        option1.setLongOpt("option1");
        option1.setDescription("Option 1");
        group.addOption(option1);

        Option option2 = new Option("option2", "o2", false, "o");
        option2.setLongOpt("option2");
        option2.setDescription("Option 2");
        group.addOption(option2);

        options.addOptionGroup(group);

        String[] arguments = new String[0];
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        arguments = new String[2];
        arguments[0] = "--group1";
        assertFalse(parser.parse(options, arguments, null).hasOption("group1"));
        assertTrue(parser.parse(options, arguments, null).hasOptionGroup("group1"));
        assertSame(group, parser.parse(options, arguments, null).getOptionGroup("group1"));
    }

    @Test
    public void testParseWithValueSeparatedByComma() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        Option option = new Option("option", "o", true, "o");
        option.setLongOpt("option");
        option.setDescription("Option with comma-separated values");
        option.setValueSeparator(',');

        String[] arguments = { "--option=value1,value2" };
        CommandLine cmd = parser.parse(options, arguments, null);

        assertEquals(0, cmd.getNumArgs());
        assertEquals("value1,value2", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithValueSeparatedBySpace() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        Option option = new Option("option", "o", true, "o");
        option.setLongOpt("option");
        option.setDescription("Option with space-separated values");
        option.setValueSeparator(' ');

        String[] arguments = { "--option=value1 value2" };
        CommandLine cmd = parser.parse(options, arguments, null);

        assertEquals(0, cmd.getNumArgs());
        assertEquals("value1 value2", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithMultipleValues() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        Option option = new Option("option", "o", true, "o");
        option.setLongOpt("option");
        option.setDescription("Option with multiple values");
        option.setValueSeparator(',');

        String[] arguments = { "--option=value1,value2,value3" };
        CommandLine cmd = parser.parse(options, arguments, null);

        assertEquals(0, cmd.getNumArgs());
        assertEquals("value1,value2,value3", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        Option option = new Option("option", "o", true, "o");
        option.setLongOpt("option");
        option.setDescription("Required option");
        option.setRequired(true);

        options.addOption(option);

        String[] arguments = { "--" };
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        arguments = new String[1];
        arguments[0] = "--option";
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMissingOptionalOption() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        Option option = new Option("option", "o", false, "o");
        option.setLongOpt("option");
        option.setDescription("Optional option");

        options.addOption(option);

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertFalse(cmd.hasOption("option"));

        arguments = new String[1];
        arguments[0] = "--option";
        cmd = parser.parse(options, arguments);
        assertFalse(cmd.hasOption("option"));

        arguments = new String[2];
        arguments[0] = "--option";
        arguments[1] = "value";
        cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[1];
        arguments[0] = "--unknown";
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithExtraArguments() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[3];
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getNumArgs());
        assertEquals("arg2", cmd.getArg(0));

        List<String> extraArgs = cmd.getArgs();
        assertEquals(2, extraArgs.size());
        assertEquals("arg1", extraArgs.get(0));
        assertEquals("arg3", extraArgs.get(1));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[3];
        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(3, cmd.getNumArgs());
        assertEquals("arg1", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));
        assertEquals("arg3", cmd.getArg(2));
    }

    @Test
    public void testParseWithEmptyArguments() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.isEmpty());
    }

    @Test
    public void testParseWithEmptyOptions() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = { "arg1", "arg2" };
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertEquals("arg1", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));

        List<Option> optionsList = options.getOptions();
        assertTrue(optionsList.isEmpty());
    }
}