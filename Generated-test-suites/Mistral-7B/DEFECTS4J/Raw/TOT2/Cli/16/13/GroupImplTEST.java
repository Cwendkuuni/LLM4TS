package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        List list = new ArrayList<>();
        list.add(new Option("a", "option A", true, "a"));
        list.add(new Option("b", "option B", false, "b"));
        list.add(new Option("c", "option C", false, "c"));
        list.add(new Option("d", "option D", true, "d"));
        Group group = new Group("group", "Group description", list, 1, Integer.MAX_VALUE);
        options = new Options();
        options.addOption(group);
    }

    @Test
    public void testBasicUsage() {
        CommandLine parsed = new DefaultParser().parse(options, new String[]{"group", "a", "1", "d", "2"});
        assertTrue(parsed.hasOption("group"));
        assertTrue(parsed.hasOption("a"));
        assertTrue(parsed.hasOption("d"));
        assertEquals("1", parsed.getOptionValue("a"));
        assertEquals("2", parsed.getOptionValue("d"));
        assertFalse(parsed.hasOption("b"));
        assertFalse(parsed.hasOption("c"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test(expected = OptionException.class)
    public void testMissingOption() {
        CommandLine parsed = new DefaultParser().parse(options, new String[]{"test"});
    }

    @Test(expected = OptionException.class)
    public void testWrongNumberOfOptions() {
        CommandLine parsed = new DefaultParser().parse(options, new String[]{"group", "a", "1", "d", "a"});
    }

    @Test(expected = OptionException.class)
    public void testInvalidOption() {
        CommandLine parsed = new DefaultParser().parse(options, new String[]{"group", "e", "1"});
    }

    @Test
    public void testAnonymousArguments() {
        CommandLine parsed = new DefaultParser().parse(options, new String[]{"group", "a", "1", "d", "3", "3"});
        assertEquals("3", parsed.getOptionValue("d"));
        assertEquals("3", parsed.getOptionValue("d", new String[]{"anonymous"}));
    }

    @Test
    public void testBursting() {
        List<String> args = new ArrayList<>();
        args.add("test");
        args.add("group");
        args.add("a");
        args.add("1");
        args.add("d");
        args.add("2");
        args.add("d");
        args.add("3");

        CommandLine parsed = new DefaultParser().parse(options, args.toArray(new String[args.size()]));
        assertEquals("3", parsed.getOptionValue("d"));
    }

    @Test
    public void testNestedOptions() {
        List list = new ArrayList<>();
        list.add(new Option("g", "group", false, null));
        list.add(new Option("a", "option A", true, "a"));
        list.add(new Option("b", "option B", false, "b"));
        list.add(new Option("c", "option C", false, "c"));
        list.add(new Option("d", "option D", true, "d"));

        Group group = new GroupImpl("group", "Group description", list, 1, Integer.MAX_VALUE);
        options.addOption(group);

        CommandLine parsed = new DefaultParser().parse(options, new String[]{"group", "g", "a", "1", "d", "2"});
        assertTrue(parsed.hasOption("group"));
        assertTrue(parsed.hasOption("g"));
        assertTrue(parsed.hasOption("a"));
        assertTrue(parsed.hasOption("d"));
        assertEquals("1", parsed.getOptionValue("a"));
        assertEquals("2", parsed.getOptionValue("d"));
        assertFalse(parsed.hasOption("b"));
        assertFalse(parsed.hasOption("c"));
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }
}