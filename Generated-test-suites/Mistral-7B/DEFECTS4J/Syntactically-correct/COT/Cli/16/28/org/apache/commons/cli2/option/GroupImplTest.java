package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link GroupImpl} class.
 */
public class GroupImplTest {

    private List<Option> options;
    private Option optionA;
    private Option optionB;
    private Option optionC;
    private Option arg;
    private Group group;

    @Before
    public void setUp() {
        optionA = new Option("A", true, "a");
        optionB = new Option("B", false, null);
        optionC = new Option("C", false, "c");
        arg = new Argument("arg");

        options = new ArrayList<>(Arrays.asList(optionA, optionB, optionC, arg));

        group = new GroupImpl(options, null, null, 0, Integer.MAX_VALUE);
    }

    @Test
    public void testGroupImplCanProcess() {
        // Test basic functionality
        assertTrue(group.canProcess(new WriteableCommandLine(""), ""));
        assertTrue(group.canProcess(new WriteableCommandLine(""), "--A"));
        assertFalse(group.canProcess(new WriteableCommandLine(""), "--B"));
        assertFalse(group.canProcess(new WriteableCommandLine(""), "--C"));
        assertFalse(group.canProcess(new WriteableCommandLine(""), "--arg"));

        // Test with multiple arguments
        assertTrue(group.canProcess(new WriteableCommandLine(""), "--A --arg"));
        assertFalse(group.canProcess(new WriteableCommandLine(""), "--arg --B --arg"));
        assertFalse(group.canProcess(new WriteableCommandLine(""), "--arg --arg"));
    }

    @Test
    public void testGroupImplProcess() {
        // Test basic functionality
        final WriteableCommandLine commandLine = new WriteableCommandLine("");

        group.process(commandLine, commandLine.iterator());
        assertNull(commandLine.getOption(optionA.getOpt()));
        assertNull(commandLine.getOption(optionB.getOpt()));
        assertNull(commandLine.getOption(optionC.getOpt()));
        assertNull(commandLine.getOption(arg.getLongOpt()));
        assertNull(commandLine.getOption(arg.getOpt()));

        commandLine.parse("--A");
        assertSame(optionA, commandLine.getOption(optionA.getOpt()));
        assertNull(commandLine.getOption(optionB.getOpt()));
        assertNull(commandLine.getOption(optionC.getOpt()));
        assertNull(commandLine.getOption(arg.getLongOpt()));
        assertNull(commandLine.getOption(arg.getOpt()));

        commandLine.parse("--A");
        assertSame(optionA, commandLine.getOption(optionA.getOpt()));
        assertSame(commandLine.getOption(optionA.getOpt()), commandLine.getOption(optionA.getOpt()));

        // Test with multiple arguments
        commandLine.parse("--A --arg");
        assertSame(optionA, commandLine.getOption(optionA.getOpt()));
        assertSame(arg, commandLine.getOption(arg.getOpt()));
        assertNull(commandLine.getOption(optionB.getOpt()));
        assertNull(commandLine.getOption(optionC.getOpt()));
        assertNull(commandLine.getOption(arg.getLongOpt()));

        commandLine.parse("--B --arg --C --arg");
        assertNull(commandLine.getOption(optionA.getOpt()));
        assertNull(commandLine.getOption(optionA.getOpt()));
        assertSame(optionB, commandLine.getOption(optionB.getOpt()));
        assertSame(optionC, commandLine.getOption(optionC.getOpt()));
        assertSame(arg, commandLine.getOption(arg.getOpt()));
        assertSame(arg, commandLine.getOption(arg.getOpt()));
    }

    @Test
    public void testGroupImplValidate() {
        final WriteableCommandLine commandLine = new WriteableCommandLine("");

        // Test with required options
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        commandLine.parse("--A --B");
        assertTrue(group.validate(commandLine));

        // Test with multiple arguments
        commandLine.parse("--A --arg --B --arg");
        assertTrue(group.validate(commandLine));
    }

    @Test
    public void testGroupImplGetPrefixes() {
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testGroupImplGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(4, triggers.size());
        assertTrue(triggers.contains(optionA.getOpt()));
        assertTrue(triggers.contains(optionB.getOpt()));
        assertTrue(triggers.contains(optionC.getOpt()));
        assertTrue(triggers.contains(arg.getOpt()));
    }

    @Test(expected = ClassCastException.class)
    public void testGroupImplGetTriggersWithNull() {
        Set triggers = group.getTriggers();
        triggers.add(null);
    }

    @Test
    public void testGroupImplGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGroupImplGetAnonymous() {
        assertEquals(Collections.unmodifiableList( Arrays.asList(arg) ), group.getAnonymous());
    }

    @Test
    public void testGroupImplFindOption() {
        assertNull(group.findOption("X"));
        assertSame(optionA, group.findOption(optionA.getOpt()));
        assertSame(optionB, group.findOption(optionB.getOpt()));
        assertSame(optionC, group.findOption(optionC.getOpt()));
        assertSame(arg, group.findOption(arg.getOpt()));
        assertSame(arg, group.findOption(arg.getOpt()));
    }

    @Test
    public void testGroupImplGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGroupImplGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }
}