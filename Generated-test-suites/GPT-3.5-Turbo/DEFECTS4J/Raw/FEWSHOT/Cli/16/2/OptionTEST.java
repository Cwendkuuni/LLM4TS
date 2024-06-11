package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {
    private static final String TRIGGER = "t";
    private Option option;

    @Before
    public void setUp() {
        option = new OptionImpl(TRIGGER);
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        ListIterator args = List.of("arg1", "arg2").listIterator();
        option.process(commandLine, args);

        assertTrue(commandLine.hasOption(TRIGGER));
        assertEquals("arg1", commandLine.getValue(TRIGGER));
        assertTrue(args.hasNext());
        assertEquals("arg2", args.next());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        option.defaults(commandLine);

        assertFalse(commandLine.hasOption(TRIGGER));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        assertTrue(option.canProcess(commandLine, TRIGGER));
        assertFalse(option.canProcess(commandLine, "other_trigger"));
    }

    @Test
    public void testCanProcessWithArguments() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        ListIterator arguments = List.of("arg1", "arg2").listIterator();
        assertTrue(option.canProcess(commandLine, arguments));
        assertFalse(option.canProcess(commandLine, List.of("other_trigger").listIterator()));
        assertTrue(arguments.hasNext());
        assertEquals("arg1", arguments.next());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertNotNull(triggers);
        assertTrue(triggers.contains(TRIGGER));
        assertEquals(1, triggers.size());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertNotNull(prefixes);
        assertTrue(prefixes.contains("-"));
        assertEquals(1, prefixes.size());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        commandLine.addValue(TRIGGER, "value");

        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateMissingOption() throws OptionException {
        option.validate(new DefaultWriteableCommandLine());
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set helpSettings = Set.of("help1", "help2");
        Comparator comp = null;

        List<HelpLineImpl> helpLines = option.helpLines(depth, helpSettings, comp);

        assertNotNull(helpLines);
        assertEquals(0, helpLines.size());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Set.of("help1", "help2");
        Comparator comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("Usage: -t", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(TRIGGER, option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("", option.getDescription());
    }

    @Test
    public void testGetId() {
        assertTrue(option.getId() > 0);
    }

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption(TRIGGER);

        assertEquals(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        assertNull(option.getParent());
    }

    @Test
    public void testSetParent() {
        Option parentOption = new OptionImpl("parent");
        option.setParent(parentOption);

        assertEquals(parentOption, option.getParent());
    }
}