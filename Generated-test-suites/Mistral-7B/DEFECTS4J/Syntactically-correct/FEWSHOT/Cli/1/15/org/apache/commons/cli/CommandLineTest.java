package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testNewCommandLine() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getArgList());
        assertNull(cmd.getOptions());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("opt"));
        cmd.addOption(new Option("opt", "opt", true));
        assertTrue(cmd.hasOption("opt"));
        cmd.addOption(new Option("opt2", "opt2", false));
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("opt2"));
    }

    @Test
    public void testHasOptionString() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption('o'));
        cmd.addOption(new Option('o', "opt", true));
        assertTrue(cmd.hasOption('o'));
        cmd.addOption(new Option('o', "opt2", false));
        assertTrue(cmd.hasOption('o'));
        assertFalse(cmd.hasOption('o', "opt2"));
    }

    @Test
    public void testHasOptionLong() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption(1));
        cmd.addOption(new Option("opt", "opt", true));
        assertTrue(cmd.hasOption("opt"));
        cmd.addOption(new Option("opt2", "opt2", false));
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption(1, "opt2"));
    }

    @Test
    public void testHasOptionLongWithAlias() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "o", true);
        cmd.addOption(opt);
        cmd.addOption(new Option("opt2", "o2", false));
        assertFalse(cmd.hasOption(1, "o2"));
        assertTrue(cmd.hasOption('o'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt", "opt", true, "value"));
        assertEquals("value", cmd.getOptionValue("opt"));
        assertNull(cmd.getOptionValue("opt2"));
        cmd.addOption(new Option("opt", "opt2", true, "value2"));
        assertEquals("value2", cmd.getOptionValue("opt"));
        assertEquals(null, cmd.getOptionValue("opt2"));
    }

    @Test
    public void testGetOptionValueString() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt", "opt", true, "value"));
        assertEquals("value", cmd.getOptionValue('o'));
        cmd.addOption(new Option("opt2", "opt2", true, "value2"));
        assertEquals("value", cmd.getOptionValue('o'));
        assertEquals("value2", cmd.getOptionValue('o', "opt2"));
    }

    @Test
    public void testGetOptionValueLong() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", 1, true, "value");
        cmd.addOption(opt);
        assertEquals("value", cmd.getOptionValue(1));
        cmd.addOption(new Option("opt2", 1, true, "value2"));
        assertEquals("value", cmd.getOptionValue(1));
        assertEquals("value2", cmd.getOptionValue(1, "opt2"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "opt", true, "value");
        cmd.addOption(opt);
        cmd.addOption(new Option("opt", "opt2", true, "value2"));
        String[] values = cmd.getOptionValues("opt");
        assertArrayEquals(new String[]{"value", "value2"}, values);
        String[] values2 = cmd.getOptionValues("opt2");
        assertArrayEquals(new String[]{"value2"}, values2);
        String[] values3 = cmd.getOptionValues("opt3");
        assertNull(values3);
    }

    @Test
    public void testGetOptionValuesLong() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", 1, true, "value");
        cmd.addOption(opt);
        cmd.addOption(new Option("opt", 2, true, "value2"));
        String[] values = cmd.getOptionValues(1);
        assertArrayEquals(new String[]{"value", "value2"}, values);
        String[] values2 = cmd.getOptionValues(2);
        assertArrayEquals(new String[]{"value2"}, values2);
        String[] values3 = cmd.getOptionValues(3);
        assertNull(values3);
    }

    @Test
    public void testGetOptionValuesLongWithAlias() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "o", true, "value");
        cmd.addOption(opt);
        cmd.addOption(new Option("opt2", "o2", true, "value2"));
        String[] values = cmd.getOptionValues('o');
        assertArrayEquals(new String[]{"value", "value2"}, values);
        String[] values2 = cmd.getOptionValues('o', "opt2");
        assertArrayEquals(new String[]{"value2"}, values2);
        String[] values3 = cmd.getOptionValues('o', "opt3");
        assertNull(values3);
    }

    @Test
    public void testGetOptionValuesWithMultipleValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "opt", true, "value");
        cmd.addOption(opt);
        cmd.addOption(new Option("opt", 1, true, "value1"));
        cmd.addOption(new Option("opt", 2, true, "value2"));
        String[] values = cmd.getOptionValues("opt");
        assertArrayEquals(new String[]{"value", "value1", "value2"}, values);
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "opt", true, Integer.class, "value");
        cmd.addOption(opt);
        assertEquals(Integer.class, cmd.getOptionObject("opt").getClass());
        assertNull(cmd.getOptionObject("opt2"));
        cmd.addOption(new Option("opt", "opt2", true, Long.class, "value2"));
        assertEquals(Integer.class, cmd.getOptionObject("opt").getClass());
        assertEquals(Long.class, cmd.getOptionObject("opt2").getClass());
    }

    @Test
    public void testGetOptionObjectWithValue() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "opt", true, Integer.class, "value");
        cmd.addOption(opt);
        cmd.addOption(new Option("opt", "opt2", true, Integer.class, "value2"));
        assertEquals(Integer.valueOf(1), cmd.getOptionObject("opt", 1));
        assertEquals(Integer.valueOf(2), cmd.getOptionObject("opt", 2));
    }

    @Test
    public void testGetOptionObjectWithValueString() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "opt", true, Integer.class, "value");
        cmd.addOption(opt);
        cmd.addOption(new Option("opt", "opt2", true, Integer.class, "value2"));
        assertEquals(Integer.valueOf(1), cmd.getOptionObject('o', 1));
        assertEquals(Integer.valueOf(2), cmd.getOptionObject('o', 2));
    }

    @Test
    public void testGetOptionObjectWithValueLong() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", 1, true, Integer.class, "value");
        cmd.addOption(opt);
        cmd.addOption(new Option("opt", 2, true, Integer.class, "value2"));
        assertEquals(Integer.valueOf(1), cmd.getOptionObject(1, 1));
        assertEquals(Integer.valueOf(2), cmd.getOptionObject(1, 2));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List args = cmd.getArgList();
        assertEquals(new ArrayList(Arrays.asList("arg1", "arg2")), args);
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg");
        List args = cmd.getArgList();
        assertEquals(new ArrayList(Arrays.asList("arg")), args);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("opt", "opt", true, "value");
        cmd.addOption(opt);
        Map options = cmd.getOptions();
        assertEquals(1, options.size());
        Option opt2 = new Option("opt2", "opt2", true, "value2");
        cmd.addOption(opt2);
        assertEquals(2, options.size());
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addOption(new Option("opt", "opt", true, "value"));
        assertEquals("[ CommandLine: [ options: {opt=Option@1542401419[opt=opt, values=[], optional=true, longOpt=opt, shortOpt=o, args=[]]} ] [ args: [arg1, arg2] ] ]", cmd.toString());
    }

    @Test
    public void testGetArgListIterable() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        Iterator it = cmd.iterator();
        List<String> args = new ArrayList<>();
        while (it.hasNext()) {
            args.add((String) it.next());
        }
        assertEquals(Arrays.asList("arg1", "arg2"), args);
    }

    @Test
    public void testGetOptionsIterable() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addOption(new Option("opt", "opt", true, "value"));
        Iterator it = cmd.iterator();
        List<Option> options = new ArrayList<>();
        while (it.hasNext()) {
            options.add((Option) it.next());
        }
        assertEquals(Arrays.asList(new Option("opt", "opt", true, "value")), options);
    }
}