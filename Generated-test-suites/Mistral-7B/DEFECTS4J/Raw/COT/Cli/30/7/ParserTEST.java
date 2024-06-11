package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser.
 */
public class ParserTest {

    

    @Test
    public void testSetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = new ArrayList();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        assertEquals(requiredOptions, parser.getRequiredOptions());
    }

    @Test
    public void testFlattenEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        String[] result = parser.flatten(null, new String[0], false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArg() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"arg"};
        String[] result = parser.flatten(null, arguments, false);
        assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option");
        options.addOption(option);
        String[] arguments = {"-o"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenSingleOptionWithArg() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        options.addOption(option);
        String[] arguments = {"-o", "arg"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "arg"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"arg"};
        parser.flatten(options, arguments, false);
        fail("ParseException expected");
    }

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(null, new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSingleArg() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option");
        options.addOption(option);
        String[] arguments = {"-o"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testParseSingleOptionWithArg() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        options.addOption(option);
        String[] arguments = {"-o", "arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals("arg", cmd.getOptionValue("opt"));
    }

    @Test(expected = ParseException.class)
    public void testParseUnknownOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--unknown"};
        parser.parse(options, arguments);
        fail("ParseException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseUnknownOptionWithArg() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--unknown", "arg"};
        parser.parse(options, arguments);
        fail("ParseException expected");
    }

    @Test
    public void testParseMultipleOptions()
            throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "-o2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
    }

    @Test
    public void testParseMultipleOptionsWithArgs()
            throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option 1");
        Option option2 = new Option("o2", "opt2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "arg1", "-o2", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals("arg1", cmd.getOptionValue("opt1"));
        assertEquals("arg2", cmd.getOptionValue("opt2"));
    }

    @Test
    public void testParseMultipleOptionsMixed()
            throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "-o2", "arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertTrue(cmd.hasOption("opt1"));
        assertEquals("arg", cmd.getOptionValue("opt2"));
    }

    @Test
    public void testParseMultipleOptionsMixedWithNoArg()
            throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "-o2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        option.setLongOpt("long-opt");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("opt", "arg");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertTrue(cmd.hasOption("opt"));
        assertEquals("arg", cmd.getOptionValue("opt"));
        assertEquals("arg", cmd.getOptionValue("long-opt"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option");
        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add("opt");
        parser.setRequiredOptions(requiredOptions);
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("opt"));
        // NPE expected since no args are passed
        assertThrows(MissingOptionException.class, () -> parser.checkRequiredOptions());
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        Option opt1 = new Option("o1", "opt1", true, "Option 1");
        Option opt2 = new Option("o2", "opt2", true, "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);
        options.addOptionGroup(group);
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.isSetGroup("group"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithRequiredGroup() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        Option opt1 = new Option("o1", "opt1", true, "Option 1");
        Option opt2 = new Option("o2", "opt2", true, "Option 2");
        group.addOption(opt1);
        group.setRequired(true);
        options.addOptionGroup(group);
        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals(Arrays.asList("opt1", "opt2"), e.getMissingOptions());
        }
    }

    @Test
    public void testParseWithMixedGroups() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        Option opt1 = new Option("o1", "opt1", true, "Option 1");
        group1.addOption(opt1);
        group1.setRequired(true);
        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        Option opt2 = new Option("o2", "opt2", true, "Option 2");
        group2.addOption(opt2);
        group2.setRequired(false);
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException expected for group1");
        } catch (MissingOptionException e) {
            assertEquals(Arrays.asList("opt1"), e.getMissingOptions());
        }
    }

    
}