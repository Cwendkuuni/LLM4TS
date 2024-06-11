package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple", false, "Set apple option"));
        cmd.addOption(new Option("b", "banana", true, "Set banana option"));
        cmd.addOption(new Option("c", "cherry", null, "Set cherry option", new String[]{"cherry", "cherries"}));

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption('x'));
        assertFalse(cmd.hasOption("c")); // no short option defined
        assertFalse(cmd.hasOption("cherry")); // no name defined

        cmd.addOption(new Option("c", null, true, "Set cherry option", new String[]{"cherry", "cherries"}));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("cherry"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("i", "integer", true, "An integer option", Integer.TYPE));

        cmd.addOption(new Option("i", "integer", true, "An integer option", Integer.TYPE));
        cmd.addOption(new Option("s", "string", true, "A string option", String.class));

        cmd.parseOptions(Arrays.asList("--integer,123", "--string,hello").toArray(new String[0]));

        assertEquals(123, cmd.getOptionValue("i", Integer.class));
        assertEquals("hello", cmd.getOptionValue("s", String.class));
        assertEquals(null, cmd.getOptionValue("invalid", Integer.class));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("i", "integer", true, "An integer option", Integer.TYPE));

        cmd.addOption(new Option("i", "integer", true, "An integer option", Integer.TYPE));
        cmd.addOption(new Option("s", "string", true, "A string option", String.class));

        cmd.parseOptions(Arrays.asList("--integer,45", "--string,foobar", "--integer,123").toArray(new String[0]));

        assertEquals("45", cmd.getOptionValue("i"));
        assertEquals("foobar", cmd.getOptionValue("s"));
        assertEquals("123", cmd.getOptionValue("i", null));
        assertEquals(null, cmd.getOptionValue("invalid"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("i", "integer", true, "An integer option", Integer.TYPE));

        cmd.addOption(new Option("i", "integer", true, "An integer option", Integer.TYPE));
        cmd.addOption(new Option("s", "string", true, "A string option", String.class));

        cmd.addOption(new Option("m", "multivalue", true, "A multivalue option", new String[0]));

        cmd.parseOptions(Arrays.asList("--integer,1", "--integer,2", "--string,foo", "--string,bar", "--m,value1", "--m,value2").toArray(new String[0]));

        String[] iv = cmd.getOptionValues("i");
        assertEquals(2, iv.length);
        assertArrayEquals(new Integer[]{1, 2}, iv);

        String[] sv = cmd.getOptionValues("s");
        assertEquals(2, sv.length);
        assertArrayEquals(new String[]{"foo", "bar"}, sv);

        String[] mv = cmd.getOptionValues("m");
        assertEquals(2, mv.length);
        assertArrayEquals(new String[]{"value1", "value2"}, mv);

        assertNull(cmd.getOptionValues("invalid"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.parseOptions(Collections.emptyList().toArray(new String[0]));
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.parseOptions(Collections.emptyList().toArray(new String[0]));
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple", false, "Set apple option"));
        cmd.addOption(new Option("b", "banana", true, "Set banana option"));
        cmd.addOption(new Option("c", "cherry", null, "Set cherry option", new String[]{"cherry", "cherries"}));

        cmd.parseOptions(Arrays.asList("--apple", "--banana", "--cherry=cherries").toArray(new String[0]));

        assertEquals("[ CommandLine: [ options: {apple=null, banana=[], cherry=[cherries]} ] [ args: [] ] ]", cmd.toString());
    }
}