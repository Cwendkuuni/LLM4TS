package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLine() {
        CommandLine cmd = new CommandLine();
        assert Null(cmd.getOptions());
        assert Null(cmd.getArgs());
        assert Null(cmd.getArgList());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption('o'));

        Option opt = new Option("opt", "option", true);
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();

        Option opt = new Option("opt", "option", false);
        cmd.addOption(opt);
        cmd.setOptionValue("opt", "true");

        assertNull(cmd.getOptionObject("opt"));
        assertEquals(Boolean.TYPE, cmd.getOptionObject("opt").getClass());
        assertSame(Boolean.valueOf(true), cmd.getOptionObject("opt"));

        Option opt2 = new Option("opt2", "option2", true);
        cmd.addOption(opt2);
        cmd.setOptionValue("opt2", "value");

        assertNull(cmd.getOptionObject("opt2"));
        assertEquals(String.class, cmd.getOptionObject("opt2").getClass());
        assertSame("value", cmd.getOptionObject("opt2"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("opt"));
        assertNull(cmd.getOptionValue('o'));

        Option opt = new Option("opt", "option", true);
        cmd.addOption(opt);
        cmd.setOptionValue("opt", "true");

        assertNull(cmd.getOptionValue("opt2"));
        assertNull(cmd.getOptionValue('o2'));
        assertSame("true", cmd.getOptionValue("opt"));
        assertSame("true", cmd.getOptionValue('o'));

        Option opt2 = new Option("opt2", "option2", true);
        cmd.addOption(opt2);
        cmd.setOptionValue("opt2", "value");

        assertNull(cmd.getOptionValue("opt3"));
        assertNull(cmd.getOptionValue('o3'));
        assertSame("value", cmd.getOptionValue("opt2"));
        assertSame("value", cmd.getOptionValue('o2'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("opt"));
        assertNull(cmd.getOptionValues('o'));

        Option opt = new Option("opt", "option", true);
        cmd.addOption(opt);
        cmd.setOptionValue("opt", "value1,value2");

        String[] expected = new String[] {"value1", "value2"};
        assertArrayEquals(expected, cmd.getOptionValues("opt"));
        assertArrayEquals(expected, cmd.getOptionValues('o'));

        Option opt2 = new Option("opt2", "option2", true);
        cmd.addOption(opt2);
        cmd.setOptionValue("opt2", "value3");
        cmd.setOptionValue("opt2", "value4,value5");

        String[] expected2 = new String[] {"value4", "value5"};
        assertArrayEquals(expected2, cmd.getOptionValues("opt2"));
        assertArrayEquals(expected2, cmd.getOptionValues('o2'));

        String[] expected3 = new String[] {"value3"};
        assertArrayEquals(expected3, cmd.getOptionValues("opt2"));
        assertArrayEquals(expected3, cmd.getOptionValues('o2'));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        String[] args = new String[] {"arg1", "arg2"};
        cmd.addArg(args[0]);
        cmd.addArg(args[1]);

        assertArrayEquals(args, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        String[] args = new String[] {"arg1", "arg2"};
        cmd.addArg(args[0]);
        cmd.addArg(args[1]);

        List expected = new LinkedList();
        Collections.addAll(expected, args);
        assertSame(expected, cmd.getArgList());
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("opt1", "option1");
        Option opt2 = new Option("opt2", "option2");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        List expected = new ArrayList();
        Collections.addAll(expected, opt1, opt2);

        Iterator it = cmd.iterator();
        List actual = new ArrayList();

        while (it.hasNext()) {
            actual.add(it.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("opt1", "option1");
        Option opt2 = new Option("opt2", "option2");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Option[] expected = new Option[] {opt1, opt2};
        Option[] actual = cmd.getOptions();

        assertArrayEquals(expected, actual);
    }
}