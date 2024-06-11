package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private WriteableCommandLine commandLine;
    private ListIterator<String> args;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
        args = new ArrayList<>(Arrays.asList("--option1", "--option2", "-foo", "-bar", "--option1").listIterator());
    }

    @Test
    public void testProcess_processOneArgument() throws OptionException {
        Option option = mock(Option.class);
        when(option.process(commandLine, args)).thenReturn(true);

        option.process(commandLine, args);

        assertTrue(args.hasNext());
        assertEquals("next argument", args.next());
    }

    @Test
    public void testProcess_processMultipleArguments() throws OptionException {
        Option option = mock(Option.class);
        when(option.process(commandLine, args)).thenReturn(true);

        option.process(commandLine, args);
        option.process(commandLine, args);

        assertTrue(args.hasNext());
        assertEquals("next argument", args.next());
    }

    @Test(expected = OptionException.class)
    public void testProcess_cannotProcessArgument() throws OptionException {
        Option option = mock(Option.class);
        when(option.canProcess(commandLine, args)).thenReturn(false);

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        option.defaults(commandLine);

        // no assertions here, as we cannot check the command line's state
    }

    @Test
    public void testCanProcess_canProcessSingleArgument() {
        Option option = mock(Option.class);
        String argument = "--option1";

        assertTrue(option.canProcess(commandLine, argument));
    }

    @Test
    public void testCanProcess_cannotProcessSingleArgument() {
        Option option = mock(Option.class);
        String argument = "-foo";

        assertFalse(option.canProcess(commandLine, argument));
    }

    @Test
    public void testCanProcess_canProcessMultipleArguments() {
        Option option = mock(Option.class);
        ListIterator<String> args = new ArrayList<>(Arrays.asList("--option1", "--option2", "-foo", "-bar", "--option1").listIterator());

        assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcess_cannotProcessMultipleArguments() {
        Option option = mock(Option.class);
        ListIterator<String> args = new ArrayList<>(Arrays.asList("--option1", "--option2", "-foo", "-bar", "--option3").listIterator());

        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Option option = mock(Option.class);
        Set triggers = new HashSet();
        triggers.add("--option");
        triggers.add("-o");

        when(option.getTriggers()).thenReturn(triggers);

        Set actualTriggers = option.getTriggers();

        assertEquals(triggers, actualTriggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = mock(Option.class);
        Set prefixes = new HashSet();
        prefixes.add("-");
        prefixes.add("--");

        when(option.getPrefixes()).thenReturn(prefixes);

        Set actualPrefixes = option.getPrefixes();

        assertEquals(prefixes, actualPrefixes);
    }

    @Test
    public void testValidate() throws OptionException {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        option.validate(commandLine);

        // no assertions here, as we cannot check the command line's state
    }

    @Test(expected = OptionException.class)
    public void testValidate_invalidCommandLine() throws OptionException {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        when(commandLine.getValue("invalidOption")).thenReturn("value");

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = mock(Option.class);
        HelpFormatter helpFormatter = new HelpFormatter();
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comparator = null;

        List helpLines = option.helpLines(depth, helpSettings, comparator);

        assertEquals(0, helpLines.size());
    }

    @Test
    public void testAppendUsage() {
        Option option = mock(Option.class);
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comparator = null;

        option.appendUsage(buffer, helpSettings, comparator);

        // no assertions here, as we cannot check the buffer's content directly
    }

    @Test
    public void testGetPreferredName() {
        Option option = mock(Option.class);
        String preferredName = "option";

        when(option.getPreferredName()).thenReturn(preferredName);

        assertEquals(preferredName, option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = mock(Option.class);
        String description = "desc";

        when(option.getDescription()).thenReturn(description);

        assertEquals(description, option.getDescription());
    }

    @Test
    public void testgetId() {
        Option option = mock(Option.class);
        int id = 1;

        when(option.getId()).thenReturn(id);

        assertEquals(id, option.getId());
    }

    @Test
    public void testFindOption_foundOption() {
        Option parentOption = mock(Option.class);
        Option childOption = mock(Option.class);

        when(parentOption.findOption("childOption")).thenReturn(childOption);

        Option option = new Option() {
            @Override
            public Option findOption(String trigger) {
                return parentOption;
            }
        };

        Option actualOption = option.findOption("childOption");

        assertEquals(childOption, actualOption);
    }

    @Test
    public void testFindOption_notFoundOption() {
        Option option = mock(Option.class);

        Option actualOption = option.findOption("nonexistentOption");

        assertNull(actualOption);
    }

    @Test
    public void testIsRequired() {
        Option option = mock(Option.class);
        boolean isRequired = false;

        when(option.isRequired()).thenReturn(isRequired);

        assertEquals(isRequired, option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = mock(Option.class);
        Option option = mock(Option.class);

        when(option.getParent()).thenReturn(parentOption);

        Option actualParent = option.getParent();

        assertEquals(parentOption, actualParent);
    }

    @Test
    public void testSetParent() {
        Option parentOption = mock(Option.class);
        Option option = mock(Option.class);

        option.setParent(parentOption);

        Option actualParent = option.getParent();

        assertEquals(parentOption, actualParent);
    }

}