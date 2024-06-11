package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCreateCommandLine() {
        CommandLine cmd = new CommandLine();
        // nothing to test
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();

        // create options
        Option optString = new Option("s", true, "A string option");
        Option optBoolean = new Option("b", false, "A boolean option");
        Option optInt = new Option("i", true, "An integer option");

        cmd.addOption(optString);
        cmd.addOption(optBoolean);
        cmd.addOption(optInt);

        // test hasOption
        assertTrue(cmd.hasOption("s"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("i"));

        // test hasOption with char
        assertTrue(cmd.hasOption('s'));
        assertTrue(cmd.hasOption('b'));
        assertTrue(cmd.hasOption('i'));

        // test hasOption with non-existent option
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption('x'));

        // test hasOption with null option
        assertFalse(cmd.hasOption(null));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();

        // create options
        Option optString = new Option("s", true, "A string option");
        Option optBoolean = new Option("b", false, "A boolean option");
        Option optInt = new Option("i", true, "An integer option");

        cmd.addOption(optString);
        cmd.addOption(optBoolean);
        cmd.addOption(optInt);

        // test getOptionObject with existent option
        assertEquals("foo", cmd.getOptionObject("s", "foo"));
        assertEquals(Boolean.TRUE, cmd.getOptionObject("b"));
        assertEquals(Integer.valueOf(123), cmd.getOptionObject("i", Integer.valueOf(123)));

        // test getOptionObject with non-existent option
        assertNull(cmd.getOptionObject("x"));
        assertNull(cmd.getOptionObject('x'));
        assertNull(cmd.getOptionObject(null));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();

        // create options
        Option optString = new Option("s", true, "A string option");
        Option optStringArg = new Option("s", true, "A string option with argument");
        Option optBoolean = new Option("b", false, "A boolean option");
        Option optInt = new Option("i", true, "An integer option");

        cmd.addOption(optString);
        cmd.addOption(optStringArg);
        cmd.addOption(optBoolean);
        cmd.addOption(optInt);

        // test getOptionValue with existent option
        assertEquals("foo", cmd.getOptionValue("s"));
        assertEquals("bar", cmd.getOptionValue("s", "bar"));
        assertEquals(false, cmd.getOptionValue("b"));
        assertEquals(Integer.valueOf(123), cmd.getOptionValue("i"));

        // test getOptionValue with non-existent option
        assertNull(cmd.getOptionValue("x"));
        assertNull(cmd.getOptionValue('x'));
        assertNull(cmd.getOptionValue(null));

        // test getOptionValue with option requiring an argument
        assertEquals("arg", cmd.getOptionValue("s", "arg"));

        // test getOptionValue with multiple options with the same long opt
        CommandLine cmd2 = new CommandLine();
        Option optString2 = new Option("s", true, "A string option in command line 2");
        Option optString3 = new Option("s", true, "Another string option in command line 2");
        cmd2.addOption(optString2);
        cmd2.addOption(optString3);
        assertEquals("arg2", cmd2.getOptionValue("s", "arg2"));
        assertEquals("arg3", cmd2.getOptionValue("s", "arg3"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();

        // create options
        Option optString = new Option("s", true, "A string option");
        Option optStringArg = new Option("s", true, "A string option with argument");
        Option optMultiple = new Option("m", true, "A multiple value option");

        cmd.addOption(optString);
        cmd.addOption(optStringArg);
        cmd.addOption(optMultiple);

        // test getOptionValues with existent option
        String[] values1 = cmd.getOptionValues("s");
        assertArrayEquals(new String[]{}, values1);

        cmd.addArg("foo");
        values1 = cmd.getOptionValues("s");
        assertArrayEquals(new String[]{"foo"}, values1);

        cmd.addArg("bar");
        values1 = cmd.getOptionValues("s");
        assertArrayEquals(new String[]{"foo", "bar"}, values1);

        cmd.addOption(optStringArg);
        cmd.addArg("arg");
        values1 = cmd.getOptionValues("s");
        assertArrayEquals(new String[]{"foo", "bar"}, values1);

        values1 = cmd.getOptionValues("s", "foo");
        assertArrayEquals(new String[]{"foo"}, values1);

        // test getOptionValues with multiple value option
        cmd.addArg("val1");
        cmd.addArg("val2");
        String[] values2 = cmd.getOptionValues("m");
        assertArrayEquals(new String[]{"val1", "val2"}, values2);

        // test getOptionValues with non-existent option
        String[] values3 = cmd.getOptionValues("x");
        assertArrayEquals(new String[]{}, values3);
        String[] values4 = cmd.getOptionValues('x');
        assertArrayEquals(new String[]{}, values4);
        String[] values5 = cmd.getOptionValues(null);
        assertArrayEquals(new String[]{}, values5);
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        // add arguments
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        // test getArgs
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);

        // test getArgList
        List argList = cmd.getArgList();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, argList.toArray(new String[argList.size()]));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();

        // create options
        Option opt1 = new Option("opt1", true, "Option 1");
        Option opt2 = new Option("opt2", true, "Option 2");
        Option opt3 = new Option("opt3", true, "Option 3");

        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);

        // test iterator
        Iterator iterator = cmd.iterator();
        List options = new LinkedList();
        while (iterator.hasNext()) {
            options.add(iterator.next());
        }
        assertEquals(3, options.size());
        assertSame(opt1, options.get(0));
        assertSame(opt2, options.get(1));
        assertSame(opt3, options.get(2));
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();

        // create options
        Option opt1 = new Option("opt1", true, "Option 1");
        Option opt2 = new Option("opt2", true, "Option 2");
        Option opt3 = new Option("opt3", true, "Option 3");

        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);

        // test getOptions
        Option[] options = cmd.getOptions();
        assertEquals(3, options.length);
        assertSame(opt1, options[0]);
        assertSame(opt2, options[1]);
        assertSame(opt3, options[2]);
    }

    @Test
    public void testToString() {
        // not implemented in this example
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();

        // test addArg
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();

        // create option
        Option opt = new Option("opt", true, "Option");

        // test addOption
        cmd.addOption(opt);
        Option[] options = cmd.getOptions();
        assertArrayEquals(new Option[]{opt}, options);
        assertTrue(cmd.hasOption("opt"));
    }
}