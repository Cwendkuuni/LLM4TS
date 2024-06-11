package org.apache.commons.cli2;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new OptionImpl();
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        ListIterator<String> args = List.of("arg1", "arg2").listIterator();

        option.process(commandLine, args);

        assertEquals("arg1", commandLine.getOptionValue(option));
        assertTrue(args.hasNext());
        assertEquals("arg2", args.next());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();

        option.defaults(commandLine);

        assertNull(commandLine.getOptionValue(option));
    }

    @Test
    public void testCanProcessWithStringArgument() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();

        assertTrue(option.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcessWithListIterator() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        ListIterator<String> arguments = List.of("arg1", "arg2").listIterator();

        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testGetTriggers() {
        Set triggers = option.getTriggers();

        assertNotNull(triggers);
        assertTrue(triggers.isEmpty());
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = option.getPrefixes();

        assertNotNull(prefixes);
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Set helpSettings = Set.of("help1", "help2");
        Comparator comp = Comparator.naturalOrder();

        List helpLines = option.helpLines(0, helpSettings, comp);

        assertNotNull(helpLines);
        assertTrue(helpLines.isEmpty());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Set.of("help1", "help2");
        Comparator comp = Comparator.naturalOrder();

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();

        assertNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();

        assertNull(description);
    }

    @Test
    public void testGetId() {
        int id = option.getId();

        assertEquals(0, id);
    }

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption("trigger");

        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        boolean isRequired = option.isRequired();

        assertFalse(isRequired);
    }

    @Test
    public void testGetParent() {
        Option parent = option.getParent();

        assertNull(parent);
    }

    @Test
    public void testSetParent() {
        Option parent = new OptionImpl();

        option.setParent(parent);

        assertEquals(parent, option.getParent());
    }
}