package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLineCreation() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getArgs());
        assertNotNull(cmd.getOptions());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("option"));
        assertFalse(cmd.hasOption('o'));

        Option option = new Option("option", true, false, "opt");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("option"));
        assertTrue(cmd.hasOption('o'));
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("option"));
        assertNull(cmd.getOptionValue('o'));

        Option option = new Option("option", true, false, "opt");
        cmd.addOption(option);
        cmd.addArg("arg");
        assertEquals("arg", cmd.getOptionValue("option"));
        assertEquals("arg", cmd.getOptionValue('o'));
        assertEquals("arg", cmd.getOptionValue("opt"));
        assertNull(cmd.getOptionValue("nonExistentOption"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        String[] values = cmd.getOptionValues("option");
        assertNull(values);

        Option option = new Option("option", true, false, "opt");
        cmd.addOption(option);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        values = cmd.getOptionValues("option");
        assertArrayEquals(new String[]{"arg1", "arg2"}, values);

        String[] emptyValues = cmd.getOptionValues("nonExistentOption");
        assertNull(emptyValues);
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        String[] args = cmd.getArgs();
        assertNull(args);

        Option option = new Option("option", true, false, "opt");
        cmd.addOption(option);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        List<String> argList = cmd.getArgList();
        assertTrue(argList.isEmpty());

        Option option = new Option("option", true, false, "opt");
        cmd.addOption(option);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testToString() {
        // The toString() method is commented out in the original code.
        CommandLine cmd = new CommandLine();
        Option option = new Option("option", true, false, "opt");
        cmd.addOption(option);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String result = cmd.toString();
        // This is just a placeholder for the actual expected result.
        assertEquals("[ CommandLine: [ options: [Option{key='option', longOpt='opt', values=[], required=false, hidden=false, args=-, index=-1, multiple=false}] ] [ args: [arg1, arg2] ] ]", result);
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg");
        List<String> args = cmd.getArgList();
        assertEquals(1, args.size());
        assertEquals("arg", args.get(0));
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("option", true, false, "opt");
        cmd.addOption(option);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("option1", true, false, "opt1");
        Option option2 = new Option("option2", true, false, "opt2");
        cmd.addOption(option1);
        cmd.addOption(option2);
        Iterator<Option> it = cmd.iterator();
        assertTrue(it.hasNext());
        assertEquals(option1, it.next());
        assertTrue(it.hasNext());
        assertEquals(option2, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("option1", true, false, "opt1");
        Option option2 = new Option("option2", true, false, "opt2");
        cmd.addOption(option1);
        cmd.addOption(option2);
        Option[] options = cmd.getOptions();
        assertEquals(2, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
    }
}