package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(arguments);

        assertEquals(0, cmd.getArgs().length);
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithOptions() {
        Options options = new Options();
        options.addOption(Option.builder("o").longOpt("option").hasArg(true).build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option = optionsList.get(0);
        assertEquals("value", option.getValue());
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
    }

    @Test
    public void testParseWithArguments() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments);

        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithLongOptions() {
        Options options = new Options();
        options.addOption(Option.builder("l").longOpt("long-option").hasArg(true).build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--long-option", "value"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option = optionsList.get(0);
        assertEquals("value", option.getValue());
        assertEquals("l", option.getOpt());
        assertEquals("long-option", option.getLongOpt());
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() {
        Options options = new Options();
        options.addOption(Option.builder("o").longOpt("option").hasArg(true).build());
        options.addOption(Option.builder("a").longOpt("argument").build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "value1", "--argument", "value2", "arg3"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        assertEquals("value1", option1.getValue());
        assertEquals("o", option1.getOpt());
        assertEquals("option", option1.getLongOpt());
        assertEquals(null, option1.getArg());
        assertEquals("value2", option2.getValue());
        assertEquals(null, option2.getOpt());
        assertEquals("argument", option2.getLongOpt());

        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg3", args.get(0));
    }

    @Test
    public void testParseWithOptionalArgs() {
        Options options = new Options();
        options.addOption(Option.builder("o").longOpt("option").hasArg(true).build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "value1", "--option", "value2"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option = optionsList.get(0);
        assertEquals("value1", option.getValue());
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
        assertEquals("value2", option.getArg());
    }

    @Test
    public void testParseWithMultipleOccurrences() {
        Options options = new Options();
        options.addOption(Option.builder("o").longOpt("option").hasArg(true).build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        assertEquals("value1", option1.getValue());
        assertEquals("o", option1.getOpt());
        assertEquals("option", option1.getLongOpt());
        assertEquals(null, option1.getArg());
        assertEquals("value2", option2.getValue());
        assertEquals("o", option2.getOpt());
        assertEquals("option", option2.getLongOpt());
        assertEquals(null, option2.getArg());
    }

    @Test
    public void testParseWithGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.addOption(Option.builder("g").longOpt("group-option").build());
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--group-option", "value"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option = optionsList.get(0);
        assertEquals("value", option.getValue());
        assertEquals("g", option.getOpt());
        assertEquals("group-option", option.getLongOpt());
        assertEquals("group", option.getGroupLongName());
        assertEquals(null, option.getArg());
        assertEquals("group options", option.getGroupShortName());
    }

    @Test
    public void testParseWithRequiredGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(true);
        group.addOption(Option.builder("g").longOpt("group-option").build());
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--group-option", "value"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option = optionsList.get(0);
        assertEquals("value", option.getValue());
        assertEquals("g", option.getOpt());
        assertEquals("group-option", option.getLongOpt());
        assertEquals("group", option.getGroupLongName());
        assertEquals(null, option.getArg());
        assertEquals("group options", option.getGroupShortName());

        assertFalse(getRequiredOptions(cmd).isEmpty());
    }

    @Test
    public void testParseWithRequiredOption() {
        Options options = new Options();
        Option option = Option.builder("r").longOpt("required-option").isRequired(true).build();
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {};
        assertThrows(MissingOptionException.class, () -> parser.parse(arguments));
    }

    @Test
    public void testParseWithMissingOption() {
        Options options = new Options();
        Option option = Option.builder("m").longOpt("missing-option").build();
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--missing-option2"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(arguments));
    }

    @Test
    public void testParseWithInvalidOption() {
        Options options = new Options();
        Option option = Option.builder("i").longOpt("invalid-option").build();
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--invalid-option="};
        assertThrows(MissingArgumentException.class, () -> parser.parse(arguments));
    }

    @Test
    public void testParseWithEmptyValue() {
        Options options = new Options();
        Option option = Option.builder("e").longOpt("empty-option").hasArg(true).build();
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--empty-option"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option1 = optionsList.get(0);
        assertNull(option1.getValue());
    }

    @Test
    public void testParseWithValue() {
        Options options = new Options();
        Option option = Option.builder("v").longOpt("value-option").hasArg(true).build();
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--value-option", "value"};
        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();
        Option option1 = optionsList.get(0);
        assertEquals("value", option1.getValue());
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");

        String[] arguments = {};
        CommandLine cmd = parser.parse(arguments, properties);

        List<Option> optionsList = cmd.getOptions();
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        assertEquals("value1", option1.getValue());
        assertEquals("option1", option1.getLongOpt());
        assertEquals("value2", option2.getValue());
        assertEquals("option2", option2.getLongOpt());
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        Option option = Option.builder("s").longOpt("stop").build();
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--stop", "arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments, null, true);

        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    private List<Option> getRequiredOptions(CommandLine cmd) {
        return cmd.getOptions().stream().filter(Option::isRequired).collect(Collectors.toList());
    }
}