package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithOptionsAndArgs() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", 2, "B option with an integer value");
        options.addOption("c", 3, "C option with an integer value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a", "--b=2", "3", "--c=3"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals(Arrays.asList("-a", "--b=2", "3", "--c=3"), cmd.getArgs());
        assertEquals(3, cmd.getOptions().size());
        Option aOpt = cmd.getOption("a");
        assertTrue(aOpt.isSet());
        Option bOpt = cmd.getOption("b");
        assertEquals(Integer.valueOf(2), bOpt.getValue());
        Option cOpt = cmd.getOption("c");
        assertEquals(Integer.valueOf(3), cOpt.getValue());
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addRequiredOption("b", true, "B option");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--x"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithUnrecognizedGroupOption() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(true);
        options.addOptionGroup(group);
        options.addOption("a", true, "A option");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--group"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithUnrecognizedOptionInGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(true);
        options.addOptionGroup(group);
        options.addOption("a", true, "A option");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--group", "--x"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithInvalidNumberOfValuesForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", 2, "A option with 2 values");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a", "v1", "v2", "v3"};
        assertThrows(InvalidNumberOfValuesForOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithInvalidBooleanValueForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a", "false"};
        assertThrows(InvalidBooleanValueForOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithMissingValueForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option with a value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option with multiple values");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("a", "v1 v2 v3");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        List<String> args = cmd.getArgs();
        List<Option> optionsList = cmd.getOptions();
        assertEquals(Arrays.asList("v1", "v2", "v3"), args);
        Option aOpt = optionsList.get(0);
        List<String> values = (List<String>) aOpt.getValue();
        assertEquals(Arrays.asList("v1", "v2", "v3"), values);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("a", "v1");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        List<String> args = cmd.getArgs();
        List<Option> optionsList = cmd.getOptions();
        assertEquals(Arrays.asList("v1"), args);
        Option aOpt = optionsList.get(0);
        List<String> values = (List<String>) aOpt.getValue();
        assertEquals(Arrays.asList("v1"), values);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true);
        List<String> argsList = cmd.getArgs();
        List<Option> optionsList = cmd.getOptions();
        assertEquals(Arrays.asList("-a"), argsList);
        assertEquals(1, optionsList.size());
        Option aOpt = optionsList.get(0);
        List<String> values = (List<String>) aOpt.getValue();
        assertEquals(Arrays.asList(""), values);
    }

    @Test
    public void testParseWithStopAtNonOptionAndOptionalValueForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option with an optional value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true);
        List<String> argsList = cmd.getArgs();
        List<Option> optionsList = cmd.getOptions();
        assertEquals(Arrays.asList("-a"), argsList);
        assertEquals(1, optionsList.size());
        Option aOpt = optionsList.get(0);
        List<String> values = (List<String>) aOpt.getValue();
        assertEquals(Arrays.asList(""), values);
    }
}