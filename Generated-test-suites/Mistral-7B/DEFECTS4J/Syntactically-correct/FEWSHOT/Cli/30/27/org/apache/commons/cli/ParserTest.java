package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), cmd);
    }

    @Test
    public void testParseWithOptions() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        options.addOption("a", "another-option", true, "Another option");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "value1", "-a", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value1", cmd.getOptionValue("o"));
        assertEquals("value2", cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("non-existent-option"));
        assertFalse(cmd.hasOption("non-existent-option"));
        assertEquals(2, cmd.getNumArgs());
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        options.addOption("a", "another-option", true, "Another option");
        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("o", "value1");
        properties.setProperty("a", "value2");

        String[] arguments = {"--option", "value3"};
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertEquals("value1", cmd.getOptionValue("o"));
        assertEquals("value2", cmd.getOptionValue("a"));
        assertEquals("value3", cmd.getOptionValue("--option"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option");
        Parser parser = new DefaultParser();

        String[] arguments = {"--other-option"};
        parser.parse(options, arguments);
        fail("Missing required option exception expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingValueForOptionalOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o"};
        try {
            parser.parse(options, arguments);
            fail("Missing value for optional option exception expected");
        } catch (MissingArgumentException e) {
            // expected
        }
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        Parser parser = new DefaultParser();

        String[] arguments = {"-x", "value"};
        parser.parse(options, arguments);
        fail("Unrecognized option exception expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidBooleanOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "false"};
        try {
            parser.parse(options, arguments);
            fail("Invalid boolean option value exception expected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidValueForOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "invalid"};
        try {
            parser.parse(options, arguments);
            fail("Invalid value for option exception expected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithMultipleValuesForOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> values = (List<String>) cmd.getOptionValues("o");
        assertArrayEquals(new String[]{"value1", "value2"}, values.toArray(new String[0]));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("g", "group-option", false, "A group option"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();

        String[] arguments = {};
        try {
            parser.parse(options, arguments);
            fail("Missing required option group exception expected");
        } catch (MissingOptionException e) {
            List<Option> requiredOptions = e.getMissingOptions();
            assertEquals(1, requiredOptions.size());
            Option option = requiredOptions.get(0);
            assertEquals("group-option", option.getKey());
            assertSame(group, option.getOptionGroup());
        }
    }

    @Test
    public void testParseWithOptionalArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional argument");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value", cmd.getOptionValue("o"));
        assertNull(cmd.getOptionValue("o.arg"));

        String[] arguments2 = {"-o", "value", "arg"};
        CommandLine cmd2 = parser.parse(options, arguments2);
        assertEquals("value", cmd2.getOptionValue("o"));
        assertEquals("arg", cmd2.getOptionValue("o.arg"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionalArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional argument");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "value", "invalid"};
        try {
            parser.parse(options, arguments);
            fail("Invalid optional argument value exception expected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithLongOptions() {
        Options options = new Options();
        options.addOption("long-option", true, "A long option");
        Parser parser = new DefaultParser();

        String[] arguments = {"--long-option"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(true, cmd.hasOption("long-option"));
        assertEquals(null, cmd.getOptionValue("long-option"));

        String[] arguments2 = {"--long-option", "value"};
        CommandLine cmd2 = parser.parse(options, arguments2);
        assertEquals(true, cmd2.hasOption("long-option"));
        assertEquals("value", cmd2.getOptionValue("long-option"));
    }

    @Test
    public void testParseWithLongOptionsAndShortOptions() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional argument");
        options.addOption("lo", "long-option", true, "A long option");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "value", "--long-option"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value", cmd.getOptionValue("o"));
        assertEquals(true, cmd.hasOption("lo"));
        assertEquals(null, cmd.getOptionValue("lo"));

        String[] arguments2 = {"-o", "value", "--long-option", "value2"};
        CommandLine cmd2 = parser.parse(options, arguments2);
        assertEquals("value", cmd2.getOptionValue("o"));
        assertEquals(true, cmd2.hasOption("lo"));
        assertEquals("value2", cmd2.getOptionValue("lo"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidLongOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional argument");
        options.addOption("il", "invalid-long-option", true, "An invalid long option");
        Parser parser = new DefaultParser();

        String[] arguments = {"--il", "value"};
        try {
            parser.parse(options, arguments);
            fail("Invalid long option exception expected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithGroups() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("g1", "group1-option", false, "A group1 option"));
        group.addOption(new Option("g2", "group2-option", false, "A group2 option"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();

        String[] arguments = {};
        try {
            parser.parse(options, new String[0]);
            fail("Missing required option group exception expected");
        } catch (MissingOptionException e) {
            List<Option> requiredOptions = e.getMissingOptions();
            assertEquals(1, requiredOptions.size());
            Option option = requiredOptions.get(0);
            assertEquals("group", option.getOptionGroup().getName());
        }

        String[] arguments2 = {"--group1-option"};
        CommandLine cmd2 = parser.parse(options, arguments2);
        assertTrue(cmd2.hasOption("g1"));

        String[] arguments3 = {"--group1-option", "--group2-option"};
        CommandLine cmd3 = parser.parse(options, arguments3);
        assertTrue(cmd3.hasOption("g1"));
        assertTrue(cmd3.hasOption("g2"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("g1", "group1-option", false, "A group1 option"));
        group.addOption(new Option("g2", "group2-option", false, "A group2 option"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();

        String[] arguments = {"--invalid-group"};
        try {
            parser.parse(options, arguments);
            fail("Invalid option group exception expected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithValuesFromProperties() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional value");
        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("o", "value");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithValuesFromArguments() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional value");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithValuesFromBoth() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional value");
        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("o", "default");
        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidValueFromProperties() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional value");
        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("o", "invalid");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        try {
            cmd = parser.parse(options, new String[0], properties);
            fail("Invalid value from properties exception expected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidValueFromArguments() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional value");
        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "invalid"};
        try {
            parser.parse(options, arguments);
            fail("Invalid value from arguments exception expected");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidValueFromBoth() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with optional value");
        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("o", "invalid");
        String[] arguments = {"-o", "invalid"};
        try {
            parser.parse(options, arguments, properties);
            fail("Invalid value from both properties and arguments exception expected");
        } catch (ParseException e) {
            // expected
        }
    }
}