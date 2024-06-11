package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test cases for the Parser class.
 */
public class ParserTest {

    /**
     * Test constructor.
     */
    @Test
    public void testParser() {
        new Parser();
    }

    /**
     * Test parse method with no arguments.
     */
    @Test
    public void testParseNoArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        assertNull(cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    /**
     * Test parse method with single argument.
     */
    @Test
    public void testParseSingleArgument() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option option = optionsList.get(0);
        assertNull(option.getValue());
        assertEquals("arg1", option.getOpt());
    }

    /**
     * Test parse method with single option.
     */
    @Test
    public void testParseSingleOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with single letter");
        options.addOption(option);
        String[] arguments = {"-o"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt = optionsList.get(0);
        assertTrue(opt.getValue().isEmpty());
        assertEquals("opt", opt.getOpt());
    }

    /**
     * Test parse method with single option and value.
     */
    @Test
    public void testParseSingleOptionWithValue() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option with single letter and value");
        options.addOption(option);
        String[] arguments = {"-o", "test"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt = optionsList.get(0);
        List<String> valueList = opt.getValues();
        assertEquals(1, valueList.size());
        assertEquals("test", valueList.get(0));
    }

    /**
     * Test parse method with multiple options.
     */
    @Test
    public void testParseMultipleOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "-o2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt1 = optionsList.get(0);
        assertTrue(opt1.getValue().isEmpty());
        assertEquals("opt1", opt1.getOpt());
        Option opt2 = optionsList.get(1);
        assertTrue(opt2.getValue().isEmpty());
        assertEquals("opt2", opt2.getOpt());
    }

    /**
     * Test parse method with multiple options and values.
     */
    @Test
    public void testParseMultipleOptionsWithValues() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option 1 with value");
        Option option2 = new Option("o2", "opt2", true, "Option 2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "value1", "-o2", "value2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt1 = optionsList.get(0);
        List<String> opt1Values = opt1.getValues();
        assertEquals(1, opt1Values.size());
        assertEquals("value1", opt1Values.get(0));
        Option opt2 = optionsList.get(1);
        List<String> opt2Values = opt2.getValues();
        assertEquals(1, opt2Values.size());
        assertEquals("value2", opt2Values.get(0));
    }

    /**
     * Test parse method with required option.
     */
    @Test
    public void testParseRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Required option");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = {};
        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments, false);
        });

        String[] argumentsWithOption = {"--opt"};
        CommandLine cmd = parser.parse(options, argumentsWithOption, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt = optionsList.get(0);
        List<String> optValues = opt.getValues();
        assertEquals(1, optValues.size());
        assertEquals("", optValues.get(0));
    }

    /**
     * Test parse method with multiple required options.
     */
    @Test
    public void testParseMultipleRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Required option 1");
        Option option2 = new Option("o2", "opt2", true, "Required option 2");
        option1.setRequired(true);
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {};

        assertThrows(MissingOptionException.class, () -> {
            new DefaultParser().parse(options, arguments, false);
        });

        String[] argumentsWithOptions = {"--opt1", "--opt2"};
        CommandLine cmd = new DefaultParser().parse(options, argumentsWithOptions, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt1 = optionsList.get(0);
        List<String> opt1Values = opt1.getValues();
        assertEquals(0, opt1Values.size());
        Option opt2 = optionsList.get(1);
        List<String> opt2Values = opt2.getValues();
        assertEquals(0, opt2Values.size());
    }

    /**
     * Test parse method with multiple arguments.
     */
    @Test
    public void testParseMultipleArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "An argument");
        option.setArgName("value");
        options.addOption(option);
        String[] arguments = {"--arg", "value1", "--arg", "value2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt = optionsList.get(0);
        List<String> values = opt.getValues();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    /**
     * Test parse method with unrecognized option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with single letter");
        options.addOption(option);
        String[] arguments = {"--unrecognized"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    /**
     * Test parse method with missing argument.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "An argument");
        option.setArgName("value");
        options.addOption(option);
        String[] arguments = {"--arg"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    /**
     * Test parse method with properties.
     */
    @Test
    public void testParseProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("p", "prop", true, "Property option");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("prop.key", "prop.value");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, properties, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt = optionsList.get(0);
        List<String> optValues = opt.getValues();
        assertEquals(1, optValues.size());
        assertEquals("prop.value", optValues.get(0));
    }

    /**
     * Test parse method with stopAtNonOption.
     */
    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "An argument");
        option.setArgName("value");
        options.addOption(option);
        String[] arguments = {"--arg", "value1", "--", "nonOption"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt = optionsList.get(0);
        List<String> values = opt.getValues();
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        String[] cmdArgs = cmd.getArgs();
        assertEquals(1, cmdArgs.length);
        assertEquals("nonOption", cmdArgs[0]);
    }

    /**
     * Test parse method with option group.
     */
    @Test
    public void testParseOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group1", "Group 1");
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        String[] arguments = {"--group1", "--opt1"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        List<Option> optionsList = cmd.getOptions();
        assertFalse(optionsList.isEmpty());
        Option opt1 = optionsList.get(0);
        assertTrue(opt1.getValues().isEmpty());
        assertEquals("opt1", opt1.getOpt());

        Option optGroup = cmd.findOption("group1");
        assertTrue(optGroup instanceof OptionGroup);
        Option selectedOption = optGroup.getSelectedOption();
        assertEquals(option1, selectedOption);
    }
}