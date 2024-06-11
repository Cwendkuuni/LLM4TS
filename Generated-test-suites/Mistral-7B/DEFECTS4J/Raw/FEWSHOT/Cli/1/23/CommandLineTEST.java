package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLineCreation() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptions());
        assertEquals(0, cmd.args.size());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();

        // Test for non-existent option
        assertFalse(cmd.hasOption("option1"));

        // Test for existing option
        cmd.addOption(new Option("o", "option", true, "Description"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption('o'));
        assertTrue(cmd.hasOption("option"));
        assertTrue(cmd.hasOption("option", "Description"));
        assertTrue(cmd.hasOption("option", true));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();

        // Test for non-existent option
        assertNull(cmd.getOptionValue("option1"));
        assertNull(cmd.getOptionValue('o'));

        // Test for option with no value
        cmd.addOption(new Option("o", "option", false, "Description"));
        assertNull(cmd.getOptionValue("option"));
        assertNull(cmd.getOptionValue("option", "default"));

        // Test for option with value
        cmd.addOption(new Option("i", "input", true, "Input file"));
        cmd.addArg("inputFile.txt");
        assertEquals("inputFile.txt", cmd.getOptionValue("i"));
        assertEquals("inputFile.txt", cmd.getOptionValue("i", "default"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();

        // Test for non-existent option
        assertNull(cmd.getOptionValues("option1"));
        assertNull(cmd.getOptionValues('o'));

        // Test for single value option
        cmd.addOption(new Option("i", "input", true, "Input file"));
        cmd.addArg("inputFile.txt");
        String[] values = cmd.getOptionValues("i");
        assertEquals(1, values.length);
        assertEquals("inputFile.txt", values[0]);

        // Test for multiple value option
        cmd = new CommandLine();
        cmd.addOption(new Option("a", "arg", true, "Argument"));
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertEquals("arg1", values[0]);
        assertEquals("arg2", values[1]);

        // Test for non-existent option with default value
        assertNull(cmd.getOptionValues("option1", "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        // Test for empty command line
        String[] args = cmd.getArgs();
        assertEquals(0, args.length);

        // Test for command line with arguments
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);

        // Test for command line with options
        cmd = new CommandLine();
        cmd.addOption(new Option("i", "input", true, "Input file"));
        cmd.addArg("inputFile.txt");
        args = cmd.getArgs();
        assertEquals(0, args.length);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();

        // Test for empty command line
        List args = cmd.getArgList();
        assertEquals(0, args.size());

        // Test for command line with arguments
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        args = cmd.getArgList();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));

        // Test for command line with options
        cmd = new CommandLine();
        cmd.addOption(new Option("i", "input", true, "Input file"));
        cmd.addArg("inputFile.txt");
        args = cmd.getArgList();
        assertEquals(1, args.size());
        assertEquals("inputFile.txt", args.get(0));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();

        // Test for empty command line
        cmd.addArg("arg1");
        List args = cmd.getArgList();
        assertEquals(1, args.size());
        assertEquals("arg1", args.get(0));

        // Test for command line with existing arguments
        cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        args = cmd.getArgList();
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertEquals("arg3", args.get(2));
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();

        // Test for empty command line
        cmd.addOption(new Option("o", "option", true, "Description"));
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        assertEquals("option", options[0].getLongOpt());
        assertEquals("option", options[0].getKey());
        assertEquals("Description", options[0].getDescription());
        assertEquals(true, options[0].isRequired());

        // Test for command line with existing options
        cmd = new CommandLine();
        cmd.addOption(new Option("o", "option1", true, "Description1"));
        cmd.addOption(new Option("o", "option2", true, "Description2"));
        options = cmd.getOptions();
        assertEquals(2, options.length);
        assertEquals("option1", options[0].getLongOpt());
        assertEquals("option1", options[0].getKey());
        assertEquals("Description1", options[0].getDescription());
        assertEquals(true, options[0].isRequired());
        assertEquals("option2", options[1].getLongOpt());
        assertEquals("option2", options[1].getKey());
        assertEquals("Description2", options[1].getDescription());
        assertEquals(true, options[1].isRequired());
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();

        // Test for empty command line
        Iterator it = cmd.iterator();
        assertFalse(it.hasNext());

        // Test for command line with options
        cmd.addOption(new Option("o", "option1", true, "Description1"));
        cmd.addOption(new Option("o", "option2", true, "Description2"));
        it = cmd.iterator();
        assertTrue(it.hasNext());
        Option option1 = it.next();
        assertEquals("option1", option1.getLongOpt());
        assertEquals("option1", option1.getKey());
        assertEquals("Description1", option1.getDescription());
        assertEquals(true, option1.isRequired());
        assertTrue(it.hasNext());
        Option option2 = it.next();
        assertEquals("option2", option2.getLongOpt());
        assertEquals("option2", option2.getKey());
        assertEquals("Description2", option2.getDescription());
        assertEquals(true, option2.isRequired());
        assertFalse(it.hasNext());
    }
}