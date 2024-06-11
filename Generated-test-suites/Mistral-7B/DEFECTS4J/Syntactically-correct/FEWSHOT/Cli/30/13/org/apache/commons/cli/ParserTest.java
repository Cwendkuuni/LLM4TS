package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArgs() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(0, cmd.getArgs().size());
        assertNull(cmd.getOption("non-existent"));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "an option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-o"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "an option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-o", "value"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("opt"));
        assertEquals(1, cmd.getOptionValues("opt").length);
        assertEquals("value", cmd.getOptionValues("opt")[0]);
    }

    @Test
    public void testParseWithMultipleValueOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "an option with multiple values");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("opt"));
        assertEquals(2, cmd.getOptionValues("opt").length);
        assertEquals("value1", cmd.getOptionValues("opt")[0]);
        assertEquals("value2", cmd.getOptionValues("opt")[1]);
    }

    @Test
    public void testParseWithLongOptionName() throws ParseException {
        Options options = new Options();
        Option option = new Option("longOptionName", "longOption", false, "a long option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--longOptionName"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("longOption"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "an option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-x"};
        parser.parse(options, args);
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws ParseException {
        Options options = new Options();
        Option optionShort = new Option("s", "shortOption", false, "a short option");
        Option optionLong = new Option("longOption", "longOption", false, "a long option");
        options.addOption(optionShort);
        options.addOption(optionLong);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--longOption", "-s"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("shortOption"));
        assertTrue(cmd.hasOption("longOption"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "an option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-o"};
        parser.parse(options, args);
    }

    @Test
    public void testParseWithMixedArgumentsAndOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "an option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--longOption", "arg1", "-o", "value"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("opt"));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "option 1");
        Option option2 = new Option("o2", "opt2", false, "option 2");
        Option option3 = new Option("o3", "opt3", false, "option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-o1", "-o2", "-o3"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertTrue(cmd.hasOption("opt3"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "group1", 1, "group 1");
        Option option1 = new Option("g1", "opt1", false, "option 1 in group 1");
        Option option2 = new Option("g2", "opt2", false, "option 2 in group 1");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--group1", "-g1", "-g2"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "an option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {};
        parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "an option");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("opt", "value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, null, properties);
        assertTrue(cmd.hasOption("opt"));
        assertEquals(1, cmd.getOptionValues("opt").length);
        assertEquals("value", cmd.getOptionValues("opt")[0]);
    }

    @Test
    public void testParseWithHelpOption() throws ParseException {
        Options options = new Options();
        Option helpOption = new Option("h", "help", false, "Print this help message.");
        helpOption.setRequired(false);
        options.addOption(helpOption);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, null, true);
        assertTrue(cmd.hasOption("help"));
    }
}