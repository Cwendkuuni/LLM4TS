package org.apache.commons.cli2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.cli2.util.HelpLine;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }

    @Test
    public void testProcess() {
        when(option.process(commandLine, anyListIterator()))
                .thenReturn(null);

        List<String> args = new ArrayList<>();
        option.process(commandLine, args.listIterator());

        verify(option).process(commandLine, args.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithException() {
        when(option.process(commandLine, anyListIterator()))
                .thenThrow(new OptionException("Test Exception"));

        List<String> args = new ArrayList<>();
        option.process(commandLine, args.listIterator());
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);

        verifyNoMoreInteractions(commandLine);
    }

    @Test
    public void testCanProcessWithArgument() {
        when(option.canProcess(commandLine, "arg"))
                .thenReturn(true);

        assertTrue(option.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcessWithArguments() {
        when(option.canProcess(commandLine, anyListIterator()))
                .thenReturn(true);

        List<String> args = Arrays.asList("arg1", "arg2");
        assertTrue(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        when(option.canProcess(commandLine, "arg"))
                .thenReturn(false);

        assertFalse(option.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcessWithInvalidArguments() {
        when(option.canProcess(commandLine, anyListIterator()))
                .thenReturn(false);

        List<String> args = Arrays.asList("arg1", "arg2");
        assertFalse(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new HashSet<>(Arrays.asList("--opt", "-o"));
        when(option.getTriggers()).thenReturn(triggers);

        assertEquals(triggers, option.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("--", "-"));
        when(option.getPrefixes()).thenReturn(prefixes);

        assertEquals(prefixes, option.getPrefixes());
    }

    @Test
    public void testValidate() throws OptionException {
        option.validate(commandLine);

        verifyNoMoreInteractions(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithException() throws OptionException {
        when(option.validate(commandLine)).thenThrow(new OptionException("Test Exception"));

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Set<DisplaySetting> helpSettings = Collections.emptySet();
        Comparator<Option> comp = null;

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, comp);

        assertNotNull(helpLines);
        assertEquals(0, helpLines.size());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = Collections.emptySet();
        Comparator<Option> comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = "opt";
        when(option.getPreferredName()).thenReturn(preferredName);

        assertEquals(preferredName, option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        String description = "Description of the option";
        when(option.getDescription()).thenReturn(description);

        assertEquals(description, option.getDescription());
    }

    @Test
    public void testgetId() {
        int id = 1;
        when(option.getId()).thenReturn(id);

        assertEquals(id, option.getId());
    }

    @Test
    public void testFindOption() {
        Option matchingOption = mock(Option.class);
        when(option.findOption("opt")).thenReturn(matchingOption);

        assertEquals(matchingOption, option.findOption("opt"));
    }

    @Test
    public void testIsRequired() {
        boolean isRequired = false;
        when(option.isRequired()).thenReturn(isRequired);

        assertEquals(isRequired, option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = mock(Option.class);
        when(option.getParent()).thenReturn(parentOption);

        assertEquals(parentOption, option.getParent());
    }

    @Test
    public void testSetParent() {
        Option parentOption = mock(Option.class);
        option.setParent(parentOption);

        assertEquals(parentOption, option.getParent());
    }
}