package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @Test
    public void testParseWithSingleOption() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "test.txt"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertEquals(option, optionsList.get(0));
        assertEquals("test.txt", cmd.getOptionValue("input"));
        assertEquals(Collections.emptyList(), cmd.getArgs());
    }

    @Test
    public void testParseWithSingleArg() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg"});
        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg", cmd.getArgs()[0]);
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @Test
    public void testParseWithSingleOptionAndArg() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "test.txt", "arg"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertEquals(option, optionsList.get(0));
        assertEquals("test.txt", cmd.getOptionValue("input"));
        assertEquals("arg", cmd.getArgs()[0]);
        assertEquals(Collections.emptyList(), cmd.getOptions().subList(1, cmd.getOptions().size()));
    }

    @Test
    public void testParseWithRequiredOption() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
        CommandLine cmd = parser.parse(options, new String[]{"-i", "test.txt"});
        assertEquals(1, cmd.getArgs().length);
        assertEquals("test.txt", cmd.getArgs()[0]);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
    }

    @Test
    public void testParseWithOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        Option option = new Option("i", "input", true, "input file");
        group.addOption(option);
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-g", "-i", "test.txt"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertEquals(group, optionsList.get(0).getParent());
        assertEquals("test.txt", cmd.getOptionValue("input"));
        assertEquals(Collections.emptyList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndArg() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        Option option = new Option("i", "input", true, "input file");
        group.addOption(option);
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-g", "-i", "test.txt", "arg"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertEquals(group, optionsList.get(0).getParent());
        assertEquals("test.txt", cmd.getOptionValue("input"));
        assertEquals("arg", cmd.getArgs()[0]);
        assertEquals(Collections.emptyList(), cmd.getOptions().subList(1, cmd.getOptions().size()));
    }

    @Test
    public void testParseWithMixedOptionsAndArgs() {
        Options options = new Options();
        Option option1 = new Option("i", "input1", true, "input file 1");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("h", "help", false, "Print this help message.");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "test1.txt", "-o", "test2.txt"});
        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());
        assertEquals(option1, optionsList.get(0));
        assertEquals(option2, optionsList.get(1));
        assertEquals(option3, optionsList.get(2));
        assertEquals("test1.txt", cmd.getOptionValue("input1"));
        assertEquals("test2.txt", cmd.getOptionValue("output"));
        assertEquals(Collections.emptyList(), cmd.getArgs());
    }

    @Test
    public void testParseWithExtraArg() {
        Options options = new Options();
        Option option1 = new Option("i", "input", true, "input file");
        options.addOption(option1);
        Parser parser = new DefaultParser();
        assertThrows(ParseException.class, () -> parser.parse(options, new String[]{"-i", "test.txt", "arg"}));
        CommandLine cmd = parser.parse(options, new String[]{"-i", "test.txt"});
        assertEquals(1, cmd.getArgs().length);
        assertEquals("test.txt", cmd.getArgs()[0]);
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @Test
    public void testParseWithMissingArgForOptionRequiringArg() {
        Options options = new Options();
        Option option = new Option("i", "input", false, "input file");
        option.setRequiredArg(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[]{"-i"}));
    }

    @Test
    public void testParseWithExtraArgsForOptionRequiringArg() {
        Options options = new Options();
        Option option = new Option("i", "input", false, "input file");
        option.setRequiredArg(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        assertThrows(ParseException.class, () -> parser.parse(options, new String[]{"-i", "test.txt", "arg1", "arg2"}));
    }

    @Test
    public void testParseWithEmptyProperties() {
        Options options = new Options();
        Properties properties = new Properties();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @Test
    public void testParseWithPropertiesContainingSingleOption() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("input", "test.txt");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertEquals(option, optionsList.get(0));
        assertEquals("test.txt", cmd.getOptionValue("input"));
        assertEquals(Collections.emptyList(), cmd.getArgs());
    }

    @Test
    public void testParseWithPropertiesContainingOptionAndArg() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("input", "test.txt");
        properties.setProperty("input2", "test2.txt");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertEquals(option, optionsList.get(0));
        assertEquals("test.txt", cmd.getOptionValue("input"));
        assertNull(cmd.getOptionValue("input2"));
        assertEquals(Arrays.asList("test2.txt"), cmd.getArgs());
    }

    @Test
    public void testParseWithPropertiesContainingMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("i", "input1", true, "input file 1");
        Option option2 = new Option("o", "output", true, "output file");
        options.addOption(option1);
        options.addOption(option2);
        Properties properties = new Properties();
        properties.setProperty("input1", "test1.txt");
        properties.setProperty("output", "test2.txt");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        assertEquals(option1, optionsList.get(0));
        assertEquals("test1.txt", cmd.getOptionValue("input1"));
        assertEquals(option2, optionsList.get(1));
        assertEquals("test2.txt", cmd.getOptionValue("output"));
        assertEquals(Collections.emptyList(), cmd.getArgs());
    }

    @Test
    public void testParseWithPropertiesContainingMissingOption() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("output", "test.txt");
        Parser parser = new DefaultParser();
        assertThrows(ParseException.class, () -> parser.parse(options, new String[0], properties));
    }

    @Test
    public void testParseWithPropertiesContainingInvalidOption() {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("invalid", "test.txt");
        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[0], properties));
    }
}