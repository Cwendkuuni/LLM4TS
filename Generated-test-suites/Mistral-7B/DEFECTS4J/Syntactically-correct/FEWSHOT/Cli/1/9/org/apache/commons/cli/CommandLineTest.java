package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    public void testCommandLine() {
        CommandLine cmd = new CommandLine();
        assertTrue(cmd.args.isEmpty());
        assertTrue(cmd.options.isEmpty());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();

        cmd.addOption("a", true);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption('a'));

        cmd.addOption("b", true);
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption('b'));

        cmd.addOption("c", false);
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption('c'));

        cmd.addOption("d", null);
        assertNull(cmd.getOptionObject("d"));
        assertNull(cmd.getOptionObject('d'));

        cmd.addOption("e", 1, true, "value");
        assertEquals(Integer.valueOf(1), cmd.getOptionObject("e"));
        assertEquals(Integer.valueOf(1), cmd.getOptionObject('e'));
        assertEquals("value", cmd.getOptionValue("e"));
        assertEquals("value", cmd.getOptionValue('e'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();

        cmd.addOption("a", true);

        cmd.addOption("b", false, true, "value1,value2");
        String[] values = cmd.getOptionValues("b");
        assertArrayEquals(new String[]{"value1", "value2"}, values);

        cmd.addOption("c", null, true, "value3");
        String cValue = cmd.getOptionValue("c");
        assertNull(cValue);
        assertNull(cmd.getOptionValue('c'));
        assertSame(Values.NO_VALUE, cmd.getOptionObject("c"));
        assertSame(Values.NO_VALUE, cmd.getOptionObject('c'));

        cmd.addOption("d", false, true, "");
        String[] dValues = cmd.getOptionValues("d");
        assertArrayEquals(new String[0], dValues);

        cmd.addOption("e", false, false, "");
        String eValue = cmd.getOptionValue("e");
        assertNull(eValue);
        assertNull(cmd.getOptionValue('e'));
        assertSame(Values.NO_VALUE, cmd.getOptionObject("e"));
        assertSame(Values.NO_VALUE, cmd.getOptionObject('e'));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);

        cmd.addArg(null);
        args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", null}, args);

        cmd.addOption("a", true);
        args = cmd.getArgs();
        assertArrayEquals(new String[0], args);
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();

        cmd.addOption("a", true);
        cmd.addArg("arg1");
        cmd.addOption("b", true);
        cmd.addArg("arg2");

        String expected = "CommandLine{args=[], options={a=Option{key='a', longOpt=null, values=[], optional=true, args=[], defaultValue=null}, b=Option{key='b', longOpt='', values=[], optional=true, args=[], defaultValue=null}}}";

        String actual = cmd.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List args = cmd.getArgList();

        assertTrue(args.containsAll(Arrays.asList("arg1", "arg2")));
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();

        Option opt = new Option("a", true, "a", "help");
        cmd.addOption(opt);

        Option opt2 = new Option("b", true, "b", "help2");
        cmd.addOption(opt2);

        Iterator<Option> iterator = cmd.iterator();

        assertEquals(opt, iterator.next());
        assertEquals(opt2, iterator.next());

        Option[] options = cmd.getOptions();

        assertArrayEquals(new Option[]{opt, opt2}, options);
    }
}