package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    CommandLine cmd;

    @Before
    public void setUp() {
        cmd = new CommandLine();
    }

    @Test
    public void testNewCommandLine() {
        // nothing to test here
    }

    @Test
    public void testOptionsMapIsEmptyInitially() {
        assertFalse(cmd.options.isEmpty());
    }

    @Test
    public void testHasOptionWithShortOption() {
        cmd.addOption(new Option("a", "one", false, "one"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption('a'));
    }

    @Test
    public void testHasOptionWithLongOption() {
        cmd.addOption(new Option("a", "one", false, "one-option"));
        assertTrue(cmd.hasOption("one"));
        assertTrue(cmd.hasOption("one-option"));
        assertTrue(cmd.hasOption("one-option-value"));
        assertFalse(cmd.hasOption("another-option"));
    }

    @Test
    public void testGetOptionValueWithShortOption() {
        cmd.addOption(new Option("a", "one", false, "one"));
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue('a'));

        cmd.addOption(new Option("a", "one", true, "one"));
        cmd.parseOptionValue("a", "value");
        assertEquals("value", cmd.getOptionValue("a"));
        assertEquals("value", cmd.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValueWithLongOption() {
        cmd.addOption(new Option("a", "one", false, "one-option"));
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("one"));
        assertNull(cmd.getOptionValue("one-option"));
        assertNull(cmd.getOptionValue("one-option-value"));

        cmd.addOption(new Option("a", "one", true, "one-option"));
        cmd.parseOptionValue("--one", "value");
        assertEquals("value", cmd.getOptionValue("a"));
        assertEquals("value", cmd.getOptionValue("one"));
        assertEquals("value", cmd.getOptionValue("one-option"));
        assertNull(cmd.getOptionValue("one-option-value"));

        cmd.parseOptionValue("--one-option", "value2");
        assertEquals("value2", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("one"));
        assertEquals("value2", cmd.getOptionValue("one-option"));
        assertEquals("value2", cmd.getOptionValue("one-option-value"));
    }

    @Test
    public void testGetOptionObjectWithShortOption() {
        Option option = new Option("a", "one", false, "one");
        cmd.addOption(option);
        assertNull(cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject('a'));

        option.setValueType(Integer.TYPE);
        cmd.parseOptionValue("a", "42");
        assertEquals(Integer.valueOf(42), cmd.getOptionObject("a"));
        assertEquals(Integer.valueOf(42), cmd.getOptionObject('a'));
    }

    @Test
    public void testGetOptionObjectWithLongOption() {
        Option option = new Option("a", "one", false, "one-option");
        cmd.addOption(option);
        assertNull(cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject("one"));
        assertNull(cmd.getOptionObject("one-option"));
        assertNull(cmd.getOptionObject("one-option-value"));

        option.setValueType(String[].class);
        cmd.parseOptionValue("--one", "--value1,--value2");
        String[] values = (String[]) cmd.getOptionObject("a");
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);

        cmd.parseOptionValue("--one", "--value3");
        values = (String[]) cmd.getOptionObject("a");
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    @Test
    public void testGetOptionValuesWithShortOption() {
        cmd.addOption(new Option("a", "one", false, "one"));
        String[] values = cmd.getOptionValues("a");
        assertNull(values);

        cmd.addOption(new Option("a", "one", true, "one"));
        cmd.parseOptionValue("a", "value1");
        cmd.parseOptionValue("a", "value2");
        values = cmd.getOptionValues("a");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    public void testGetOptionValuesWithLongOption() {
        cmd.addOption(new Option("a", "one", false, "one-option"));
        String[] values = cmd.getOptionValues("a");
        assertNull(values);

        cmd.addOption(new Option("a", "one", true, "one-option"));
        cmd.parseOptionValue("--one", "--value1");
        cmd.parseOptionValue("--one", "--value2");
        values = cmd.getOptionValues("a");
        assertArrayEquals(new String[]{"--value1", "--value2"}, values);
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("a", "one", false, "one");
        cmd.addOption(option);
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        assertEquals(option, options[0]);

        cmd.addOption(new Option("b", "two", false, "two"));
        options = cmd.getOptions();
        assertEquals(2, options.length);
        assertTrue(Arrays.asList(options).contains(option));
        assertTrue(Arrays.asList(options).contains(cmd.options.get("b")));
    }

    @Test
    public void testAddArg() {
        cmd.addArg("arg1");
        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertEquals("arg1", args[0]);

        cmd.addArg("arg2");
        args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testGetArgs() {
        cmd.addArg("arg1");
        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertEquals("arg1", args[0]);

        cmd.addArg("arg2");
        args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);

        String[] expectedArgs = {"arg1", "arg2"};
        cmd.addArgs(Arrays.asList(expectedArgs));
        args = cmd.getArgs();
        assertEquals(3, args.length);
        assertArrayEquals(expectedArgs, Arrays.copyOfRange(args, 0, 3));
    }

    @Test
    public void testToString() {
        cmd.addOption(new Option("a", "one", false, "one"));
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        StringBuilder expected = new StringBuilder();
        expected.append("[ CommandLine: [ options: {a=org.apache.commons.cli.Option@6d093287} ] [ args: [arg1, arg2] ] ]");

        String actual = cmd.toString();
        assertEquals(expected.toString(), actual);
    }
}