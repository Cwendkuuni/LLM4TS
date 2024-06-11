package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithNoOptionsAndNoArguments() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndNoArguments() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);

        assertTrue(cmd.getOptions().containsAll(Arrays.asList(option1, option2)));
        assertFalse(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0).isSet());
        assertTrue(optionsList.get(1).isSet());
        assertEquals("arg1", optionsList.get(0).getValue());
        assertEquals("arg2", optionsList.get(1).getValue());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        options.addOption(option1);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"arg1", "--missing-option"});
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        options.addOption(option1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1"});

        option1.setValue(null);
        parser.parse(options, new String[]{"arg1", "arg2"});
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        option1.setRequired(true);
        options.addOption(option1);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("u", "opt2", false, "Unrecognized Option 2");
        options.addOption(option1);
        options.addOption(option2);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"--opt2"});
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.setRequired(false);

        Option option1 = new Option("o", "opt1", true, "Option 1", group);
        Option option2 = new Option("o", "opt2", true, "Option 2", group);

        options.addOption(group);
        options.addOption(option1);
        options.addOption(option2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--group", "arg1", "arg2"});

        OptionGroup groupObj = cmd.getOptions().getOptionGroup("group");
        assertNotNull(groupObj);
        assertTrue(groupObj.isSelected());
        assertEquals(2, cmd.getArgs().length);
        assertTrue(cmd.getOptions().get(0).isSet());
        assertTrue(cmd.getOptions().get(1).isSet());
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseWithNoSuchOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        options.addOption(option1);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"--no-such-option"});
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithHelp() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--help"});

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }
}