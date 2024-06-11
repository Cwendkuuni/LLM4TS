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
import org.apache.commons.cli2.util.HelpLineImpl;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private List<HelpLine> helpLines;
    private Set<String> triggers, prefixes;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
        helpLines = new ArrayList<>();
        triggers = new HashSet<>();
        prefixes = new HashSet<>();
    }

    @Test
    public void testProcess() throws OptionException {
        when(option.canProcess(commandLine, anyString())).thenReturn(true);
        when(option.process(commandLine, any(ListIterator.class))).thenReturn(true);

        // Test processing a single argument
        ListIterator args = Arrays.asList("--option", "value").listIterator();
        boolean result = option.process(commandLine, args);
        assertTrue(result);
        verify(option, times(1)).process(commandLine, args);

        // Test processing multiple arguments
        args = Arrays.asList("--option1", "--option2", "--option").listIterator();
        result = option.process(commandLine, args);
        assertTrue(result);
        verify(option, times(3)).process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        when(option.defaults(commandLine)).thenReturn(true);

        option.defaults(commandLine);
        verify(commandLine, times(1)).setValue("defaultValue", null);
    }

    @Test
    public void testCanProcess() {
        when(option.canProcess(commandLine, anyString())).thenReturn(true);

        // Test single argument
        String argument = "argument";
        boolean result = option.canProcess(commandLine, argument);
        assertTrue(result);

        // Test multiple arguments
        ListIterator args = Arrays.asList("--option", "argument1", "argument2").listIterator();
        result = option.canProcess(commandLine, args);
        assertTrue(result);
    }

    @Test
    public void testGetTriggers() {
        triggers.add("--option");
        triggers.add("-o");
        when(option.getTriggers()).thenReturn(triggers);

        Set<String> actualTriggers = option.getTriggers();
        assertEquals(triggers, actualTriggers);
    }

    @Test
    public void testGetPrefixes() {
        prefixes.add("--");
        prefixes.add("-");
        when(option.getPrefixes()).thenReturn(prefixes);

        Set<String> actualPrefixes = option.getPrefixes();
        assertEquals(prefixes, actualPrefixes);
    }

    @Test
    public void testValidate() throws OptionException {
        when(option.validate(commandLine)).thenReturn(true);

        option.validate(commandLine);
        verify(option, times(1)).validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithException() throws OptionException {
        when(option.validate(commandLine)).thenThrow(new OptionException("Error message"));

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        when(option.helpLines(anyInt(), any(Set.class), any(Comparator.class)))
            .thenReturn(helpLines);

        List<HelpLine> actualHelpLines = option.helpLines(1, Collections.emptySet(), null);
        assertEquals(helpLines, actualHelpLines);

        verify(option, times(1)).helpLines(1, Collections.emptySet(), null);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        when(option.appendUsage(buffer, any(Set.class), any(Comparator.class)))
            .thenReturn(buffer);

        StringBuffer actualUsage = option.appendUsage(new StringBuffer(), Collections.emptySet(), null);
        assertEquals(buffer.toString(), actualUsage.toString());

        verify(option, times(1)).appendUsage(buffer, Collections.emptySet(), null);
    }

    @Test
    public void testGetPreferredName() {
        when(option.getPreferredName()).thenReturn("--option");

        String actualName = option.getPreferredName();
        assertEquals("--option", actualName);
    }

    @Test
    public void testGetDescription() {
        when(option.getDescription()).thenReturn("Option description");

        String actualDescription = option.getDescription();
        assertEquals("Option description", actualDescription);
    }

    @Test
    public void testgetId() {
        when(option.getId()).thenReturn(1);

        int actualId = option.getId();
        assertEquals(1, actualId);
    }

    @Test
    public void testFindOption() {
        Option anotherOption = mock(Option.class);

        when(option.findOption("--anotherOption")).thenReturn(anotherOption);

        Option actualOption = option.findOption("--anotherOption");
        assertEquals(anotherOption, actualOption);
    }

    @Test
    public void testIsRequired() {
        when(option.isRequired()).thenReturn(true);

        boolean actualIsRequired = option.isRequired();
        assertTrue(actualIsRequired);
    }

    @Test
    public void testGetParent() {
        Option parentOption = mock(Option.class);

        when(option.getParent()).thenReturn(parentOption);

        Option actualParent = option.getParent();
        assertEquals(parentOption, actualParent);
    }

    @Test
    public void testSetParent() {
        Option parentOption = mock(Option.class);

        option.setParent(parentOption);

        Option actualParent = option.getParent();
        assertEquals(parentOption, actualParent);
    }
}