package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] args = new String[] { "arg1", "--option1Value", "--option2", "-o2", "--option1" };
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertArrayEquals(args, cmd.getArgs().toArray(new String[0]));
        assertSame(options, cmd.getOptions());
        assertSame(options.getOption("option1"), cmd.getOption("option1"));
        assertSame(options.getOption("option2"), cmd.getOption("option2"));
        assertSame(options.getOption("o2"), cmd.getOption("o2"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertFalse(cmd.hasOption("nonExistentOption"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        String[] args = new String[0];
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        List<Option> optionsList = cmd.getOptions();
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);

        assertSame(options.getOption("option1"), option1);
        assertSame(options.getOption("option2"), option2);
        assertSame(option1.getValue(), "value1");
        assertSame(option2.getValue(), "value2");
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] args = new String[] { "arg1", "--option1", "-x", "--option2" };
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args, true);

        List<String> argsList = cmd.getArgs();
        List<Option> optionsList = cmd.getOptions();

        assertArrayEquals(new String[] { "arg1", "-x" }, argsList.toArray(new String[0][]));
        assertEquals(2, optionsList.size());
        assertSame(options.getOption("option1"), optionsList.get(0));
        assertSame(options.getOption("option2"), optionsList.get(1));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException, MissingOptionException {
        Options options = new Options();
        String[] args = new String[0];
        options.addRequiredOption("requiredOption");

        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithMultipleMissingRequiredOptions() throws ParseException, MissingOptionException {
        Options options = new Options();
        String[] args = new String[0];
        options.addRequiredOption("requiredOption1");
        options.addRequiredOption("requiredOption2");

        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException, UnrecognizedOptionException {
        Options options = new Options();
        String[] args = new String[] { "--unknownOption" };

        CommandLineParser parser = new DefaultParser();

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        String[] args = new String[] { "--group", "opt1", "--group", "opt2" };
        OptionGroup group = new OptionGroup("group");
        group.addOption(new Option("opt1", "Option 1"));
        group.addOption(new Option("opt2", "Option 2"));
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Option opt1 = cmd.getOption("opt1");
        Option opt2 = cmd.getOption("opt2");

        assertSame(group, opt1.getOptGroup());
        assertSame(group, opt2.getOptGroup());
    }
}