
package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption(""));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("-"));
        assertFalse(cmd.hasOption("--"));
        assertFalse(cmd.hasOption("-abc"));
        assertFalse(cmd.hasOption("--abc"));

        cmd.addOption(new Option("a", false, ""));
        assertTrue(cmd.hasOption("a"));

        cmd.addOption(new Option("b", true, ""));
        assertTrue(cmd.hasOption("b"));

        cmd.addOption(new Option("c", false, ""));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testHasOption_Char() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption('a'));
        assertFalse(cmd.hasOption('-'));

        cmd.addOption(new Option("a", false, ""));
        assertTrue(cmd.hasOption('a'));

        cmd.addOption(new Option("b", true, ""));
        assertFalse(cmd.hasOption('b'));

        cmd.addOption(new Option("c", false, ""));
        assertTrue(cmd.hasOption('c'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject("b"));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionObject("a"));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionObject("b"));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionObject("c"));
    }

    @Test
    public void testGetOptionObject_Char() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject('a'));
        assertNull(cmd.getOptionObject('b'));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionObject('a'));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionObject('b'));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionObject('c'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionValue("a"));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionValue("b"));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testGetOptionValue_Char() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue('a'));
        assertNull(cmd.getOptionValue('b'));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionValue('a'));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionValue('b'));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionValue('c'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionValues("a"));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionValues("b"));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionValues("c"));
    }

    @Test
    public void testGetOptionValues_Char() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues('a'));
        assertNull(cmd.getOptionValues('b'));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionValues('a'));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionValues('b'));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionValues('c'));
    }

    @Test
    public void testGetOptionValue_Default() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("a", "default"));
        assertNull(cmd.getOptionValue("b", "default"));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionValue("a", "default"));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionValue("b", "default"));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionValue("c", "default"));
    }

    @Test
    public void testGetOptionValue_Char_DefaultValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue('a', "default"));
        assertNull(cmd.getOptionValue('b', "default"));

        cmd.addOption(new Option("a", false, ""));
        assertNull(cmd.getOptionValue('a', "default"));

        cmd.addOption(new Option("b", true, ""));
        assertNull(cmd.getOptionValue('b', "default"));

        cmd.addOption(new Option("c", false, ""));
        assertNull(cmd.getOptionValue('c', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgs().length);

        cmd.addArg("arg1");
        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);

        cmd.addArg("arg2");
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgList().size());

        cmd.addArg("arg1");
        assertEquals(1, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));

        cmd.addArg("arg2");
        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.iterator().hasNext());

        cmd.addOption(new Option("a", false, ""));
        assertTrue(cmd.iterator().hasNext());
        assertEquals("a", cmd.iterator().next().getOpt());

        cmd.addOption(new Option("b", true, ""));
        assertTrue(cmd.iterator().hasNext());
        assertEquals("a", cmd.iterator().next().getOpt());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getOptions().length);

        cmd.addOption(new Option("a", false, ""));
        assertEquals(1, cmd.getOptions().length);
        assertEquals("a", cmd.getOptions()[0].getOpt());

        cmd.addOption(new Option("b", true, ""));
        assertEquals(2, cmd.getOptions().length);
        assertEquals("a", cmd.getOptions()[0].getOpt());
        assertEquals("b", cmd.getOptions()[1].getOpt());
    }
}
