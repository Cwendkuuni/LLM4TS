package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        Option option3 = new Option("o", "option3", true, "Option 3");
        Option option4 = new Option("o", "option4", true, "Option 4");
        Option option5 = new Option("o", "option5", true, "Option 5");
        Option option6 = new Option("o", "option6", true, "Option 6");
        Option option7 = new Option("o", "option7", true, "Option 7");
        Option option8 = new Option("o", "option8", true, "Option 8");
        Option option9 = new Option("o", "option9", true, "Option 9");
        Option option10 = new Option("o", "option10", true, "Option 10");

        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(option2);
        group.addOption(option3);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        options.addOption(option9);
        options.addOption(option10);
        options.addOptionGroup(group);

        String[] args = new String[]{"--option1", "value1", "--option3", "value3",
                "--group", "--option2", "--option4", "value4", "--option5", "value5",
                "--option6", "value6", "--option7", "value7", "--option8", "value8",
                "--option9", "value9", "--option10", "value10"};

        CommandLine cmd = parser.parse(options, args);

        assertEquals(10, cmd.getArgs().size());
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value3", cmd.getOptionValue("option3"));
        assertEquals("value4", cmd.getOptionValue("option4"));
        assertEquals("value5", cmd.getOptionValue("option5"));
        assertEquals("value6", cmd.getOptionValue("option6"));
        assertEquals("value7", cmd.getOptionValue("option7"));
        assertEquals("value8", cmd.getOptionValue("option8"));
        assertEquals("value9", cmd.getOptionValue("option9"));
        assertEquals("value10", cmd.getOptionValue("option10"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertTrue(cmd.hasOption("option3"));
        assertTrue(cmd.hasOption("option4"));
        assertTrue(cmd.hasOption("option5"));
        assertTrue(cmd.hasOption("option6"));
        assertTrue(cmd.hasOption("option7"));
        assertTrue(cmd.hasOption("option8"));
        assertTrue(cmd.hasOption("option9"));
        assertTrue(cmd.hasOption("option10"));
        assertTrue(cmd.hasOption("group"));
        assertTrue(group.isSelected());
        assertFalse(cmd.hasOption("unknown"));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test
    public void testParser_WithSingleOption() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option1 = new Option("o", "option1", true, "Option 1");

        options.addOption(option1);

        String[] args = new String[]{"--option1", "value1"};

        CommandLine cmd = parser.parse(options, args);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option1"));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test(expected = ParseException.class)
    public void testParser_MissingRequiredOption() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        Option option3 = new Option("o", "option3", true, "Option 3");

        options.addOption(option1);
        options.addOption(option2);

        String[] args = new String[]{"--option1", "value1"};

        CommandLineParser parser2 = new DefaultParser();
        CommandLine cmd = parser2.parse(options, args);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("option2"));
        assertNull(cmd.getOptionValue("option2"));

        // Parser should throw an exception when required options are missing
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParser_MissingRequiredOptionGroup() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        Option option3 = new Option("o", "option3", true, "Option 3");

        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(option2);
        group.addOption(option3);

        options.addOption(option1);
        options.addOptionGroup(group);

        String[] args = new String[]{"--option1", "value1"};

        CommandLineParser parser2 = new DefaultParser();
        CommandLine cmd = parser2.parse(options, args);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("option2"));
        assertFalse(cmd.hasOption("option3"));

        // Parser should throw an exception when required option group is missing
        parser.parse(options, args, true, null);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParser_MissingArgument() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        String[] args = new String[]{"--option1"};

        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(null, cmd.getOptionValue("option2"));
        assertTrue(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("option2"));
        assertTrue(cmd.hasOption("option2.arg"));
        assertNull(cmd.getOptionValue("option2.arg"));

        // Parser should throw an exception when required argument is missing
        parser.parse(options, args, true, new String[]{"option2"});
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParser_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option1 = new Option("o", "option1", true, "Option 1");

        options.addOption(option1);

        String[] args = new String[]{"--unknown", "value1"};

        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("unknown"));
        assertNull(cmd.getOptionValue("unknown"));

        // Parser should throw an exception when unrecognized option is provided
        parser.parse(options, args, true, new String[]{"unknown"});
    }

    @Test
    public void testParser_HelpFormatting() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");

        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(option2);
        group.addOption(option2);

        options.addOption(option1);
        options.addOptionGroup(group);

        String[] args = new String[]{"--help"};

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test command line parser", options);
        System.out.println(formatter.getHelpTemplate());
    }
}