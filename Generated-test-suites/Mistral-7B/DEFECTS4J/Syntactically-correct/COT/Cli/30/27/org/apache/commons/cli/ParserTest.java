package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    Parser parser;
    Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParse_NoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParse_SimpleArguments() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingOption() throws ParseException {
        options.addOption("missingOption", false, "m", "mo");
        String[] arguments = {"--missingOption"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        options.addOption("optionA", false, null, "a");
        options.addOption("optionB", false, null, "b");
        String[] arguments = {"--optionC"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_OptionsWithValues() throws ParseException {
        options.addOption("optionA", true, "a", "valA");
        options.addOption("optionB", true, "b", "valB");
        String[] arguments = {"--optionA", "valueA1", "--optionB=valueB1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("optionA"));
        assertTrue(cmd.getValue("optionA").equals("valueA1"));
        assertTrue(cmd.hasOption("optionB"));
        assertTrue(cmd.getValue("optionB").equals("valueB1"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgument() throws ParseException {
        options.addOption("optionA", false, null, "a");
        options.addOption("optionB", true, null, "b");
        String[] arguments = {"--optionA"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParse_MultipleValuesForOption() throws ParseException {
        options.addOption("optionA", true, "a", "valA");
        String[] arguments = {"--optionA=valA1", "--optionA=valA2", "--optionA=valA3"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_Properties() throws ParseException {
        options.addOption("optionA", false, "a");
        options.addOption("optionB", true, "b", "valB");
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertTrue(cmd.hasOption("optionA"));
        assertTrue(cmd.getValue("optionA").equals("valueA"));
        assertTrue(cmd.hasOption("optionB"));
        assertTrue(cmd.getValue("optionB").equals("valueB"));
    }

    @Test
    public void testParse_RequiredOptions() throws ParseException {
        options.addOption("optionA", true, null, "a");
        options.addOption("optionB", true, null, "b");
        String[] arguments = {"--optionA"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Option 'optionB' is missing", e.getMessage());
            assertEquals("Required option 'optionB' not provided", e.getLocalizedMessage());
        }
    }

    @Test
    public void testParse_OptionGroups() throws ParseException {
        OptionGroup group = new OptionGroup("group", "group");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption("optionA", true, "a");
        group.addOption("optionB", true, "b");
        String[] arguments = {"--group"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Option 'optionA' is missing", e.getMessage());
            assertEquals("Option 'optionB' is missing", e.getCause().getMessage());
        }
    }
}