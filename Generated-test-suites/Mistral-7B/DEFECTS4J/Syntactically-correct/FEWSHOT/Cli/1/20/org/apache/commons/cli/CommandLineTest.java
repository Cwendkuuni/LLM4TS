package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testNewCommandLine() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("opt"));
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("opt"));

        Option opt = Option.builder("opt").build();
        cmd.addOption(opt);
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();

        Option optBoolean = Option.builder("opt").build();
        cmd.addOption(optBoolean);
        cmd.addArg("value");
        assertEquals(null, cmd.getOptionObject("opt"));
        assertEquals("value", cmd.getOptionObject("opt", "default").toString());

        Option optValue = Option.builder("opt2").longOpt("opt2").hasArg(true).build();
        cmd.addOption(optValue);
        cmd.addArg("value1 value2");
        assertEquals("value1 value2", cmd.getOptionObject("opt2").toString());
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();

        Option optBoolean = Option.builder("opt").build();
        cmd.addOption(optBoolean);
        cmd.addArg("value");
        assertNull(cmd.getOptionValue("opt"));
        assertEquals("value", cmd.getOptionValue("opt", "default"));

        Option optValue = Option.builder("opt2").longOpt("opt2").hasArg(true).build();
        cmd.addOption(optValue);
        cmd.addArg("value1 value2");
        assertEquals("value1 value2", cmd.getOptionValue("opt2"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();

        Option optValue = Option.builder("opt").longOpt("opt").hasArg(true).build();
        cmd.addOption(optValue);
        cmd.addArg("value1");
        cmd.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("opt"));

        Option optNoArg = Option.builder("opt2").build();
        cmd.addOption(optNoArg);
        cmd.addArg("value3");
        assertNull(cmd.getOptionValues("opt2"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        String[] args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);

        cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addOption(Option.builder("opt").longOpt("opt").hasArg(true).build());
        cmd.addArg("arg3");

        args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg3"}, args);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        List<String> args = cmd.getArgList();
        assertEquals(Arrays.asList("arg1", "arg2", "arg3"), args);

        cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addOption(Option.builder("opt").longOpt("opt").hasArg(true).build());
        cmd.addArg("arg3");

        args = cmd.getArgList();
        assertEquals(Arrays.asList("arg1", "arg3"), args);
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        List<String> args = cmd.getArgList();
        assertEquals(Arrays.asList("arg1", "arg2", "arg3"), args);

        cmd.addArg("arg4");
        args = cmd.getArgList();
        assertEquals(Arrays.asList("arg1", "arg2", "arg3", "arg4"), args);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();

        Option optBoolean = Option.builder("opt").build();
        cmd.addOption(optBoolean);
        assertEquals(1, cmd.getOptions().length);

        Option optValue = Option.builder("opt2").longOpt("opt2").hasArg(true).build();
        cmd.addOption(optValue);
        assertEquals(2, cmd.getOptions().length);

        Option optNoArg = Option.builder("opt3").build();
        cmd.addOption(optNoArg);
        assertEquals(3, cmd.getOptions().length);
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();

        Option optBoolean = Option.builder("opt").build();
        cmd.addOption(optBoolean);
        Option optValue = Option.builder("opt2").longOpt("opt2").hasArg(true).build();
        cmd.addOption(optValue);
        Option optNoArg = Option.builder("opt3").build();
        cmd.addOption(optNoArg);

        Iterator<Option> it = cmd.iterator();
        List<Option> options = new LinkedList<>();
        while (it.hasNext()) {
            options.add(it.next());
        }
        assertEquals(Arrays.asList(optBoolean, optValue, optNoArg), options);
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();

        Option optBoolean = Option.builder("opt").build();
        cmd.addOption(optBoolean);
        Option optValue = Option.builder("opt2").longOpt("opt2").hasArg(true).build();
        cmd.addOption(optValue);
        Option optNoArg = Option.builder("opt3").build();
        cmd.addOption(optNoArg);

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        String toString = "CommandLine:" + System.lineSeparator() + " [ options:" + System.lineSeparator()
                + " [Option: key='opt', opts=[], argName=null, arg=null, values=[], isRequired=false, isLongOpt=false]"
                + System.lineSeparator() + " [Option: key='opt2', opts=[], argName='opt2', arg=null, values=[], isRequired=true, isLongOpt=true]"
                + System.lineSeparator() + " [Option: key='opt3', opts=[], argName=null, arg=null, values=[], isRequired=false, isLongOpt=false]]"
                + System.lineSeparator() + " [ args:" + System.lineSeparator() + " ['arg1', 'arg2', 'arg3']" + System.lineSeparator() + " ] ]";
        assertEquals(toString, cmd.toString());
    }
}