package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        assertFalse(cmd.hasArgs());
        assertFalse(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[]{"arg1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasArgs());
        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg1", args.get(0));
    }

    @Test
    public void testParseMultipleArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasArgs());
        List<String> args = cmd.getArgs();
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertEquals("arg3", args.get(2));
    }

    @Test
    public void testParseWithOptionsNoValue() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addOption("option1", false, null, "Short description");
        String[] arguments = new String[]{"--option1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option1"));
        Option option = cmd.getOption("option1");
        assertFalse(option.hasValue());
    }

    @Test
    public void testParseWithOptionsValue() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addOption("option1", true, null, "Short description");
        String[] arguments = new String[]{"--option1", "value1", "--option1", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option1"));
        Option option = cmd.getOption("option1");
        List<String> values = option.getValues();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseWithOptionsValueAndArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addOption("option1", true, null, "Short description");
        String[] arguments = new String[]{"--option1", "value1", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option1"));
        Option option = cmd.getOption("option1");
        List<String> values = option.getValues();
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithGroupAndRequiredOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addOption(new Option("option1", true, null, "Option 1"));
        group.addOption(new Option("option2", true, null, "Option 2"));
        options.addOptionGroup(group);
        options.addRequiredOption("require1", true, null, "Required Option 1");
        String[] arguments = new String[]{"--group1", "--option1", "--require1"};
        CommandLine cmd = parser.parse(options, arguments);
        Option option1 = cmd.getOption("option1");
        Option option2 = cmd.getOption("option2");
        Option require1 = cmd.getOption("require1");
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertTrue(cmd.hasOption("require1"));
        assertSame(group, option1.getOptGroup());
        assertSame(group, option2.getOptGroup());
        assertSame(group, require1.getOptGroup());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addRequiredOption("require1", true, null, "Required Option 1");
        String[] arguments = new String[]{"arg1"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("option1.value1", "value1");
        properties.setProperty("option1.value2", "value2");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        Option option1 = cmd.getOption("option1");
        List<String> values = option1.getValues();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
}