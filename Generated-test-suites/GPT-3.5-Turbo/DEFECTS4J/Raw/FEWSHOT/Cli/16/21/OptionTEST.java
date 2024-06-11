package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class OptionTest {

    private static Option option;

    @BeforeClass
    public static void setUp() {
        option = new OptionImpl();
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = List.of("arg1", "arg2", "arg3").listIterator();
        
        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            e.printStackTrace();
        }

        assertTrue(commandLine.hasOption(option));
        assertEquals("arg2", args.next());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new CommandLineImpl();

        option.defaults(commandLine);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option.getDefaultValue(), commandLine.getValue(option));
    }

    @Test
    public void testCanProcess_String() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "-f";

        assertTrue(option.canProcess(commandLine, argument));
    }

    @Test
    public void testCanProcess_ListIterator() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> arguments = List.of("-f", "value").listIterator();

        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("-f"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertEquals(2, prefixes.size());
        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new CommandLineImpl();

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            e.printStackTrace();
        }

        // Validation should not throw an exception
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set<HelpSettings> helpSettings = Set.of(HelpSettings.SHORT_USAGE);
        Comparator comp = Comparator.naturalOrder();

        List<HelpLineImpl> helpLines = option.helpLines(depth, helpSettings, comp);

        assertEquals(1, helpLines.size());
        assertEquals(option.getDescription(), helpLines.get(0).getLine());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<HelpSettings> helpSettings = Set.of(HelpSettings.SHORT_USAGE);
        Comparator comp = Comparator.naturalOrder();

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals(option.getPreferredName(), buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();

        assertEquals("-f", preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();

        assertEquals("Description", description);
    }

    @Test
    public void testGetId() {
        int id = option.getId();

        assertEquals(1, id);
    }

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption("-f");

        assertEquals(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        boolean required = option.isRequired();

        assertFalse(required);
    }
    
}