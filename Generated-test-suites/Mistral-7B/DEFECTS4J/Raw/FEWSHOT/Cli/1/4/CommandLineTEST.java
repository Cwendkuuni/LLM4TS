package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLine() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("option"));
        assertFalse(cmd.hasOption("option"));
        assertNull(cmd.getOptionValues("option"));
        assertNull(cmd.getArgs());
        assertNull(cmd.getArgList());
        assertEquals(0, cmd.size());
    }

    @Test
    public void testCommandLineHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("option", "opt", false, "option description"));
        assertTrue(cmd.hasOption("option"));
        assertTrue(cmd.hasOption('o'));
        assertFalse(cmd.hasOption("noOption"));
        assertFalse(cmd.hasOption('z'));
    }

    @Test
    public void testCommandLineGetOptionObject() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", "opt", false, "option description");
        cmd.addOption(opt);
        cmd.parseOptionValue("--opt value");
        assertEquals("value", cmd.getOptionObject("opt"));
        assertEquals("value", cmd.getOptionObject('o'));
        assertNull(cmd.getOptionObject("noOption"));
        assertNull(cmd.getOptionObject('z'));
    }

    @Test
    public void testCommandLineGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", "opt", true, "option description");
        cmd.addOption(opt);
        cmd.parseOptionValues("--opt arg1 arg2");
        String[] values = cmd.getOptionValues("opt");
        assertArrayEquals(new String[]{"arg1", "arg2"}, values);
        assertSame(cmd, cmd.getOptionObject("opt"));
        assertSame(cmd, cmd.getOptionObject('o'));
        assertNull(cmd.getOptionValues("noOption"));
        assertNull(cmd.getOptionValues('z'));
    }

    @Test
    public void testCommandLineGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
        String[] getArgs = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, getArgs);
    }

    @Test
    public void testCommandLineGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List argList = cmd.getArgList();
        assertEquals(2, argList.size());
        String arg1 = (String) argList.get(0);
        String arg2 = (String) argList.get(1);
        assertEquals("arg1", arg1);
        assertEquals("arg2", arg2);
    }

    @Test
    public void testCommandLineParseOptionValue() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", "opt", false, "option description");
        cmd.addOption(opt);
        cmd.parseOptionValue("--opt value");
        assertEquals("value", cmd.getOptionObject("opt"));
        assertEquals("value", cmd.getOptionObject('o'));
        assertNull(cmd.getOptionValues("noOption"));
        assertNull(cmd.getOptionValues('z'));

        cmd.clearParsedValues();
        cmd.parseOptionValue("--opt");
        assertFalse(cmd.hasOption("opt"));
        assertNull(cmd.getOptionObject("opt"));
    }

    @Test
    public void testCommandLineParseOptionValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", "opt", true, "option description");
        cmd.addOption(opt);
        cmd.parseOptionValues("--opt arg1 arg2");
        String[] values = cmd.getOptionValues("opt");
        assertArrayEquals(new String[]{"arg1", "arg2"}, values);
        assertSame(cmd, cmd.getOptionObject("opt"));
        assertSame(cmd, cmd.getOptionObject('o'));

        cmd.clearParsedValues();
        cmd.parseOptionValues("--opt arg1 --opt arg2");
        values = cmd.getOptionValues("opt");
        assertArrayEquals(new String[]{"arg1", "arg2"}, values);
    }

    @Test
    public void testCommandLineSize() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        cmd.addOption(new Option("option", "opt", false, "option description"));
        cmd.parseOptionValue("--opt value");
        assertEquals(5, cmd.size());
    }

    @Test
    public void testCommandLineIterator() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        cmd.addOption(new Option("option", "opt", false, "option description"));
        cmd.parseOptionValue("--opt value");

        Iterator it = cmd.iterator();
        List options = new LinkedList();
        List args = new LinkedList();

        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Option) {
                options.add(obj);
            } else {
                args.add(obj);
            }
        }

        assertEquals(1, options.size());
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertEquals("arg3", args.get(2));
        Option opt = (Option) options.get(0);
        assertEquals("value", opt.getValue());
    }

    @Test
    public void testCommandLineGetOptions() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("opt1", "o1", false, "option 1 description");
        Option opt2 = new Option("opt2", "o2", true, "option 2 description");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        Option[] options = cmd.getOptions();
        assertEquals(2, options.length);
        assertEquals(opt1, options[0]);
        assertEquals(opt2, options[1]);
    }

    @Test
    public void testCommandLineWithEmptyStringArray() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("option", "opt", false, "option description"));
        cmd.parseOptionValue("--opt");

        // Adding an empty string array as an argument
        cmd.addArg(CommandLine.EMPTY_STRING_ARRAY);
        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertArrayEquals(new String[0], args);

        // Checking if the Option is still set
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testCommandLineAddOptionWithLongOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("long-option", "lo", false, "long option description"));
        cmd.parseOptionValue("--long-option");

        assertTrue(cmd.hasOption("long-option"));
        assertTrue(cmd.hasOption('l'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandLineParseInvalidOption() {
        CommandLine cmd = new CommandLine();
        cmd.parseOptionValue("--invalid-option");
        fail("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandLineParseInvalidOptionValue() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", "opt", true, "option description");
        cmd.addOption(opt);
        cmd.parseOptionValue("--opt invalidValue");
        fail("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandLineParseInvalidOptionValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", "opt", true, "option description");
        cmd.addOption(opt);
        cmd.parseOptionValues("--opt arg1 arg2 arg3");
        fail("Should throw an IllegalArgumentException");
    }

    @Test
    public void testCommandLineClearParsedValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", "opt", true, "option description");
        cmd.addOption(opt);
        cmd.parseOptionValues("--opt arg1 arg2");
        assertEquals(2, cmd.size());
        cmd.clearParsedValues();
        assertEquals(0, cmd.size());
    }

    @Test
    public void testCommandLineToString() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addOption(new Option("option", "opt", false, "option description"));
        cmd.parseOptionValue("--opt");

        String expected = "[ CommandLine: [ options: {" +
                "option={Option{name='opt', longOpt='', mandatory=false, values=[], hidden=false, args=[], syntaxIndex=-1, optPos=-1, longoptPos=-1, isDefaultVal=false, value=null, description='option description'}}] " +
                "[ args: [arg1, arg2] ] ]]";

        String result = cmd.toString();
        assertEquals(expected, result);
    }
}