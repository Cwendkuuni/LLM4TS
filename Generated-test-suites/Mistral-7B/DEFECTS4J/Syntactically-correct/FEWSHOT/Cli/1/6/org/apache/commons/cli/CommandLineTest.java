package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    CommandLine cmd;

    @Before
    public void initialize() {
        cmd = new CommandLine();
    }

    @Test
    public void testHasOptionEmptyCommandLine() {
        assertFalse(cmd.hasOption("option"));
    }

    @Test
    public void testHasOptionSetOption() {
        cmd.addOption(new Option("o", "option", false, "option description"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption('o'));
    }

    @Test
    public void testHasOptionUnexpectedOption() {
        cmd.addOption(new Option("o", "option", false, "option description"));
        assertFalse(cmd.hasOption("unexpectedOption"));
    }

    @Test
    public void testGetOptionValueEmptyCommandLine() {
        assertNull(cmd.getOptionValue("option"));
    }

    @Test
    public void testGetOptionValueNoValue() {
        cmd.addOption(new Option("o", "option", false, "option description"));
        assertNull(cmd.getOptionValue("o"));
    }

    @Test
    public void testGetOptionValueSingleValue() {
        cmd.addOption(new Option("o", "option", true, "option description"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("o"));
        assertEquals("value", cmd.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValueMultipleValues() {
        cmd.addOption(new Option("o", "option", true, "option description"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        String[] values = cmd.getOptionValues("o");
        assertArrayEquals(new String[] {"value1", "value2"}, values);
    }

    @Test
    public void testGetOptionValueMissingOption() {
        cmd.addOption(new Option("o", "option", false, "option description"));
        assertNull(cmd.getOptionValue("missingOption"));
    }

    @Test
    public void testGetOptionValueMissingArgument() {
        cmd.addOption(new Option("o", "option", true, "option description"));
        assertNull(cmd.getOptionValue("o"));
    }

    @Test
    public void testGetOptionValuesEmptyCommandLine() {
        assertNull(cmd.getOptionValues("option"));
    }

    @Test
    public void testGetOptionValuesNoValue() {
        cmd.addOption(new Option("o", "option", false, "option description"));
        assertNull(cmd.getOptionValues("o"));
    }

    @Test
    public void testGetOptionValuesSingleValue() {
        cmd.addOption(new Option("o", "option", true, "option description"));
        cmd.addArg("value");
        assertEquals(new String[] {"value"}, cmd.getOptionValues("o"));
        assertEquals(new String[] {"value"}, cmd.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValuesMultipleValues() {
        cmd.addOption(new Option("o", "option", true, "option description"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        String[] values = cmd.getOptionValues("o");
        assertArrayEquals(new String[] {"value1", "value2"}, values);
    }

    @Test
    public void testGetOptionValuesMissingOption() {
        cmd.addOption(new Option("o", "option", false, "option description"));
        assertNull(cmd.getOptionValues("missingOption"));
    }

    @Test
    public void testGetOptionValuesMissingArgument() {
        cmd.addOption(new Option("o", "option", true, "option description"));
        assertNull(cmd.getOptionValues("o"));
    }

    @Test
    public void testGetOptionObjectEmptyCommandLine() {
        assertNull(cmd.getOptionObject("option"));
    }

    @Test
    public void testGetOptionObjectSetOption() {
        Option opt = new Option("o", "option", true, "option description");
        opt.setType(String.class);
        cmd.addOption(opt);
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionObject("o"));
        assertEquals("value", cmd.getOptionObject('o'));
    }

    @Test
    public void testGetOptionObjectMissingOption() {
        Option opt = new Option("o", "option", true, "option description");
        opt.setType(String.class);
        cmd.addOption(opt);
        assertNull(cmd.getOptionObject("missingOption"));
    }

    @Test
    public void testGetOptionObjectMissingArgument() {
        Option opt = new Option("o", "option", true, "option description");
        opt.setType(String.class);
        cmd.addOption(opt);
        assertNull(cmd.getOptionObject("o"));
    }

    @Test
    public void testGetOptionObjectNullOption() {
        Option opt = new Option("o", "option", true, "option description");
        opt.setType(String.class);
        cmd.addOption(opt);
        assertNull(cmd.getOptionObject(null));
    }

    @Test
    public void testGetArgListEmptyCommandLine() {
        assertEquals(Collections.emptyList(), cmd.getArgList());
    }

    @Test
    public void testGetArgListWithArgs() {
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List expected = new ArrayList();
        expected.add("arg1");
        expected.add("arg2");
        assertEquals(expected, cmd.getArgList());
    }

    @Test
    public void testGetArgsEmptyCommandLine() {
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testGetArgsWithArgs() {
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] expected = new String[] {"arg1", "arg2"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testAddArg() {
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List args = cmd.getArgList();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));

        String[] argsArr = cmd.getArgs();
        assertArrayEquals(argsArr, new String[] {"arg1", "arg2"});
    }

    @Test
    public void testIterator() {
        Option opt1 = new Option("o1", "option1", false, "option1 description");
        Option opt2 = new Option("o2", "option2", true, "option2 description");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Map<Integer, Option> hashcodeMap = cmd.hashcodeMap;

        List<Option> expected = new ArrayList<>();
        expected.add(opt1);
        expected.add(opt2);

        Iterator<Option> it = cmd.iterator();
        List<Option> actual = new ArrayList<>();
        while (it.hasNext()) {
            actual.add(it.next());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        Option opt1 = new Option("o1", "option1", false, "option1 description");
        Option opt2 = new Option("o2", "option2", true, "option2 description");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Option[] expected = new Option[] {opt1, opt2};
        Option[] actual = cmd.getOptions();
        assertArrayEquals(expected, actual);
    }
}