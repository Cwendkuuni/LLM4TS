package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testParseEmpty() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[0]);
        assertFalse(cmd.hasArgs());
        assertEquals(0, cmd.getArgs().size());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        options.addOption("a", true, "a");
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        options.addOption("a", true, "a");
        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), Boolean.TRUE.toString());
    }

    @Test
    public void testParseSingleOptionWithValue() throws ParseException {
        options.addOption("a", true, "a");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "1"});
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), "1");
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("b", false, "b");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseMultipleOptionsWithValues() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("b", false, "b");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "1", "-b", "2"});
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), "1");
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        options.addOption("long-a", true, "long a");
        CommandLine cmd = parser.parse(options, new String[]{"--long-a"});
        assertTrue(cmd.hasOption("long-a"));
        assertSame(cmd.getOptionValue("long-a"), Boolean.TRUE.toString());
    }

    @Test
    public void testParseWithLongOptionsAndValues() throws ParseException {
        options.addOption("long-a", true, "long a");
        CommandLine cmd = parser.parse(options, new String[]{"--long-a", "1"});
        assertTrue(cmd.hasOption("long-a"));
        assertSame(cmd.getOptionValue("long-a"), "1");
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("b", false, "b");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "--long-b", "-b"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMixedOptionsAndValues() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("b", false, "b");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "1", "--long-b", "2", "-b"});
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), "1");
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithArgs() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        assertTrue(cmd.hasArgs());
        List args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithArgsAndOptions() throws ParseException {
        options.addOption("a", true, "a");
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "-a"});
        assertTrue(cmd.hasArgs());
        List args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg1", args.get(0));
        assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        options.getRequiredOptions().add("b");
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithAllOptions() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        options.addOption("c", false, "c");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "-c"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption("a", true, "a");
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), "1");
    }

    @Test
    public void testParseWithPropertiesAndArgs() throws ParseException {
        options.addOption("a", true, "a");
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"}, properties);
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), "1");
        assertTrue(cmd.hasArgs());
        List args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption("a", true, "a");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1"}, true);
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), "arg1");
        assertTrue(cmd.hasArgs());
        List args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg1", args.get(0));
    }

    @Test
    public void testParseWithStopAtNonOptionAndProperties() throws ParseException {
        options.addOption("a", true, "a");
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1"}, true, properties);
        assertTrue(cmd.hasOption("a"));
        assertSame(cmd.getOptionValue("a"), "arg1");
        assertTrue(cmd.hasArgs());
        List args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg1", args.get(0));
        assertTrue(properties.size() == 1);
        assertEquals("1", properties.getProperty("a"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        OptionGroup group = new OptionGroup("group1");
        group.setExclusive(false);
        group.addOption(new Option("a", true, "a", "group1"));
        group.addOption(new Option("b", true, "b", "group1"));
        options.addOptionGroup(group);
        options.addOption(new Option("c", false, "c"));
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"});
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testParseWithOptionGroupAndRequiredOption() throws ParseException {
        OptionGroup group = new OptionGroup("group1");
        group.setExclusive(false);
        group.addOption(new Option("a", true, "a", "group1"));
        group.addOption(new Option("b", true, "b", "group1"));
        options.addOptionGroup(group);
        options.addOption(new Option("c", true, "c"));
        options.getRequiredOptions().add("c");
        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptionInGroup() throws ParseException {
        OptionGroup group = new OptionGroup("group1");
        group.setExclusive(false);
        group.addOption(new Option("a", true, "a", "group1"));
        group.addOption(new Option("b", true, "b", "group1"));
        options.addOptionGroup(group);
        options.addOption(new Option("c", true, "c"));
        options.getRequiredOptions().add("c");
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithOptionGroupAndExclusiveOptions() throws ParseException {
        OptionGroup group = new OptionGroup("group1");
        group.setExclusive(true);
        group.addOption(new Option("a", true, "a", "group1"));
        group.addOption(new Option("b", true, "b", "group1"));
        options.addOptionGroup(group);
        CommandLine cmd = parser.parse(options, new String[]{"-a"});
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        options.addOption("a", true, "a");
        parser.parse(options, new String[]{"invalid"});
    }
}