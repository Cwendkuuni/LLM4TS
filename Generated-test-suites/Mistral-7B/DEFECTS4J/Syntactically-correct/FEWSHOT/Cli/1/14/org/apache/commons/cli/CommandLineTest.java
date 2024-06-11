package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLineEmpty() {
        CommandLine cmd = new CommandLine();
        String[] args = cmd.getArgs();
        assertEquals(0, args.length);
        assertEquals(Collections.emptyMap(), cmd.getOptions());
    }

    @Test
    public void testCommandLineSimple() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        assertEquals(Collections.emptyMap(), cmd.getOptions());
    }

    @Test
    public void testCommandLineOptions() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("o", "opt", true, "defaultValue");
        cmd.addOption(option);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        assertEquals(option, options[0]);
        assertTrue(option.isSet());
        assertEquals("defaultValue", option.getValue());
    }

    @Test
    public void testCommandLineOptionsWithValues() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("o", "opt", true, "defaultValue");
        cmd.addOption(option);
        cmd.parse(Arrays.asList("--opt", "value1").toArray(new String[0]));
        String[] args = cmd.getArgs();
        assertEquals(0, args.length);
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        assertEquals(option, options[0]);
        assertTrue(option.isSet());
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testCommandLineMultipleOptions() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("o1", "opt1", true, "defaultValue1");
        Option option2 = new Option("o2", "opt2", true, "defaultValue2");
        cmd.addOption(option1);
        cmd.addOption(option2);
        cmd.parse(Arrays.asList("--opt1", "value1", "--opt2", "value2").toArray(new String[0]));
        Option[] options = cmd.getOptions();
        assertEquals(2, options.length);
        Option opt1 = options[0];
        Option opt2 = options[1];
        assertTrue(opt1.isSet());
        assertEquals("value1", opt1.getValue());
        assertTrue(opt2.isSet());
        assertEquals("value2", opt2.getValue());
    }

    @Test
    public void testCommandLineLongOptions() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("long-option", "opt", true, "defaultValue");
        cmd.addOption(option);
        cmd.parse(Arrays.asList("--long-option", "value1").toArray(new String[0]));
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        Option opt = options[0];
        assertTrue(opt.isSet());
        assertEquals("value1", opt.getValue());
    }

    @Test
    public void testCommandLineMixedOptionsAndArgs() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("o", "opt", true, "defaultValue");
        cmd.addOption(option);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.parse(Arrays.asList("--opt", "value1", "arg3").toArray(new String[0]));
        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg3", args[0]);
        assertEquals("arg2", args[1]);
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        Option opt = options[0];
        assertTrue(opt.isSet());
        assertEquals("value1", opt.getValue());
    }
}