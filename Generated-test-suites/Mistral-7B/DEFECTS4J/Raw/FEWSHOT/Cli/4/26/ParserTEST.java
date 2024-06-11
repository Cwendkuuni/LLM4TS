package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine result = parser.parse(null, new String[0]);
        assertNull(result.getArgs());
        assertFalse(result.hasOptions());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A help option");

        String[] arguments = new String[]{"--a"};
        CommandLine result = parser.parse(options, arguments);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        Option opt = optionsList.get(0);
        assertTrue(opt.isSet());
        assertNull(opt.getValue());
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a value");

        String[] arguments = new String[]{"--a", "value"};
        CommandLine result = parser.parse(options, arguments);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        Option opt = optionsList.get(0);
        assertTrue(opt.isSet());
        assertEquals("value", opt.getValue());
    }

    @Test
    public void testParseWithMultipleValueOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with multiple values");

        String[] arguments = new String[]{"--a", "value1", "--a", "value2"};
        CommandLine result = parser.parse(options, arguments);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        Option opt = optionsList.get(0);
        assertTrue(opt.isSet());
        List<String> values = opt.getValues();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A required option");
        options.addOption("b", true, "Another option");
        List<Option> requiredOptions = options.getRequiredOptions();

        String[] arguments = new String[]{"--b"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            List<String> missingOptions = Arrays.asList(e.getMissingOptions());
            assertEquals(1, missingOptions.size());
            assertEquals("--a", missingOptions.get(0));
        }
    }

    @Test
    public void testParseWithMultipleRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A required option");
        options.addOption("b", true, "Another required option");
        List<Option> requiredOptions = options.getRequiredOptions();

        String[] arguments = new String[]{"--b"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            List<String> missingOptions = Arrays.asList(e.getMissingOptions());
            assertEquals(2, missingOptions.size());
            assertTrue(Collections.disjoint(missingOptions, Arrays.asList("--a", "--b")));
        }

        String[] arguments2 = new String[]{"--a", "--b"};
        CommandLine result = parser.parse(options, arguments2);
        List<Option> optionsList = result.getOptions();
        assertEquals(2, optionsList.size());
        Option optA = optionsList.get(0);
        Option optB = optionsList.get(1);
        assertTrue(optA.isSet());
        assertTrue(optB.isSet());
    }

    @Test
    public void testParseWithMissingOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option");

        String[] arguments = new String[]{"--b"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: --b", e.getMessage());
        }
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option");

        String[] arguments = new String[]{"arg1", "arg2"};
        CommandLine result = parser.parse(options, arguments);
        List<String> args = result.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertFalse(result.hasOptions());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option");

        String[] arguments = new String[]{"--a", "arg1", "arg2"};
        CommandLine result = parser.parse(options, arguments);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        Option opt = optionsList.get(0);
        assertTrue(opt.isSet());
        assertNull(opt.getValue());
        List<String> args = result.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a property");

        Properties properties = new Properties();
        properties.setProperty("a", "arg1");

        String[] arguments = new String[0];
        CommandLine result = parser.parse(options, arguments, properties);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        Option opt = optionsList.get(0);
        assertTrue(opt.isSet());
        assertEquals("arg1", opt.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option");

        String[] arguments = new String[]{"--a", "arg1", "--non-option", "arg2"};
        CommandLine result = parser.parse(options, arguments, true);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        Option opt = optionsList.get(0);
        assertTrue(opt.isSet());
        assertEquals("arg1", opt.getValue());
        List<String> args = result.getArgs();
        assertEquals(1, args.size());
        assertEquals("--non-option", args.get(0));
    }

    @Test
    public void testParseWithStopAtNonOptionAndRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A required option");
        options.addOption("b", true, "Another option");
        List<Option> requiredOptions = options.getRequiredOptions();

        String[] arguments = new String[]{"--b", "arg1", "--a"};
        try {
            parser.parse(options, arguments, true);
            fail();
        } catch (MissingOptionException e) {
            List<String> missingOptions = Arrays.asList(e.getMissingOptions());
            assertEquals(1, missingOptions.size());
            assertEquals("--a", missingOptions.get(0));
        }
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "--a", "--b"};
        CommandLine result = parser.parse(options, arguments);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        OptionGroup groupResult = (OptionGroup) optionsList.get(0);
        assertEquals("group", groupResult.getLongOpt());
        assertTrue(groupResult.isSet());
        List<Option> optionsInGroup = groupResult.getOptions();
        assertEquals(2, optionsInGroup.size());
        Option optA = optionsInGroup.get(0);
        assertTrue(optA.isSet());
        assertNull(optA.getValue());
        Option optB = optionsInGroup.get(1);
        assertTrue(optB.isSet());
        assertNull(optB.getValue());
    }

    @Test
    public void testParseWithGroupAndValues() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("a", true, "Option A", "value1", "Value 1"));
        group.addOption(new Option("b", true, "Option B", "value2", "Value 2"));
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "--a", "--b", "argValue"};
        CommandLine result = parser.parse(options, arguments);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        OptionGroup groupResult = (OptionGroup) optionsList.get(0);
        assertEquals("group", groupResult.getLongOpt());
        assertTrue(groupResult.isSet());
        List<Option> optionsInGroup = groupResult.getOptions();
        assertEquals(2, optionsInGroup.size());
        Option optA = optionsInGroup.get(0);
        assertTrue(optA.isSet());
        assertEquals("value1", optA.getValue());
        Option optB = optionsInGroup.get(1);
        assertTrue(optB.isSet());
        assertEquals("value2", optB.getValue());
    }

    @Test
    public void testParseWithGroupAndValuesAndMissingOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("a", true, "Option A", "value1", "Value 1"));
        group.addOption(new Option("b", true, "Option B", "value2", "Value 2"));
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "--c"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            List<String> missingOptions = Arrays.asList(e.getMissingOptions());
            assertEquals(1, missingOptions.size());
            assertEquals("--a", missingOptions.get(0));
        }
    }

    @Test
    public void testParseWithGroupAndValuesAndExtraArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("a", true, "Option A", "value1", "Value 1"));
        group.addOption(new Option("b", true, "Option B", "value2", "Value 2"));
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group", "--a", "--b", "arg1", "arg2"};
        CommandLine result = parser.parse(options, arguments);
        List<Option> optionsList = result.getOptions();
        assertEquals(1, optionsList.size());
        OptionGroup groupResult = (OptionGroup) optionsList.get(0);
        assertEquals("group", groupResult.getLongOpt());
        assertTrue(groupResult.isSet());
        List<Option> optionsInGroup = groupResult.getOptions();
        assertEquals(2, optionsInGroup.size());
        Option optA = optionsInGroup.get(0);
        assertTrue(optA.isSet());
        assertEquals("value1", optA.getValue());
        Option optB = optionsInGroup.get(1);
        assertTrue(optB.isSet());
        assertEquals("value2", optB.getValue());
        List<String> args = result.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }
}