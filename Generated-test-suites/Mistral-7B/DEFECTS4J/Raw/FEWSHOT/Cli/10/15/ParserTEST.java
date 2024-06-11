package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().length == 1);
        assertEquals("arg", cmd.getArgs()[0]);
    }

    @Test
    public void testParseMultipleArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertTrue(args.size() == 3);
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertEquals("arg3", args.get(2));
    }

    @Test
    public void testParseArgumentsWithOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Optional option");
        options.addOption(option);
        String[] arguments = {"--opt"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getOptions().size() == 1);
        Option parsedOption = cmd.getOption("opt");
        assertNotNull(parsedOption);
        assertFalse(parsedOption.hasValue());
    }

    @Test
    public void testParseArgumentsWithValueOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option with value");
        options.addOption(option);
        String[] arguments = {"--opt=value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getOptions().size() == 1);
        Option parsedOption = cmd.getOption("opt");
        assertNotNull(parsedOption);
        assertEquals("value", parsedOption.getValue());
    }

    @Test
    public void testParseArgumentsWithMixedOptionsAndValues() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", true, "Option 2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--opt1", "--opt2=value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getOptions().size() == 2);
        Option option1Parsed = cmd.getOption("opt1");
        assertNotNull(option1Parsed);
        assertFalse(option1Parsed.hasValue());
        Option option2Parsed = cmd.getOption("opt2");
        assertNotNull(option2Parsed);
        assertEquals("value", option2Parsed.getValue());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Required option");
        options.addOption(option);
        options.setRequiredOptions(Collections.singletonList(option.getKey()));
        String[] arguments = {"arg1", "arg2"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--unknown"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option with value");
        options.addOption(option);
        Properties properties = new Properties();
        properties.put("opt", "value");
        CommandLine cmd = parser.parse(options, (String[]) null, properties);
        assertTrue(cmd.getOptions().size() == 1);
        Option parsedOption = cmd.getOption("opt");
        assertNotNull(parsedOption);
        assertEquals("value", parsedOption.getValue());
    }

    @Test
    public void testParseWithPropertiesAndArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option with value");
        options.addOption(option);
        Properties properties = new Properties();
        properties.put("opt", "value");
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.getOptions().size() == 1);
        Option parsedOption = cmd.getOption("opt");
        assertNotNull(parsedOption);
        assertEquals("value", parsedOption.getValue());
        List<String> args = Arrays.asList(cmd.getArgs());
        assertTrue(args.size() == 2);
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

}