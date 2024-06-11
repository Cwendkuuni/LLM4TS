package org.apache.commons.cli2;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    private WriteableCommandLine commandLine;

    private ListIterator<String> argsIterator;

    @Before
    public void setUp() {
        option = mock(Option.class);
        commandLine = mock(WriteableCommandLine.class);
        argsIterator = mock(ListIterator.class);
    }

    @Test
    public void testProcess() throws OptionException {
        when(argsIterator.hasNext()).thenReturn(true);
        when(argsIterator.next()).thenReturn("arg1");
        option.process(commandLine, argsIterator);
        verify(commandLine).setOptionValue(option, "arg1");
    }

    @Test(expected = OptionException.class)
    public void testProcess_NoArgument() throws OptionException {
        when(argsIterator.hasNext()).thenReturn(false);
        option.process(commandLine, argsIterator);
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        verify(commandLine).setOptionValue(option, null);
    }

    @Test
    public void testCanProcess_WithArgument() {
        when(argsIterator.hasNext()).thenReturn(true);
        when(argsIterator.next()).thenReturn("arg1");
        boolean result = option.canProcess(commandLine, argsIterator);
        assertEquals(true, result);
    }

    @Test
    public void testCanProcess_NoArgument() {
        when(argsIterator.hasNext()).thenReturn(false);
        boolean result = option.canProcess(commandLine, argsIterator);
        assertEquals(false, result);
    }

    @Test
    public void testGetTriggers() {
        Set triggers = option.getTriggers();
        assertNotNull(triggers);
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = option.getPrefixes();
        assertNotNull(prefixes);
    }

    @Test
    public void testValidate() throws OptionException {
        option.validate(commandLine);
        verify(commandLine).getOptionValues(option);
    }

    @Test
    public void testHelpLines() {
        int depth = 1;
        Set helpSettings = mock(Set.class);
        Comparator comp = mock(Comparator.class);
        List helpLines = option.helpLines(depth, helpSettings, comp);
        assertNotNull(helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = mock(Set.class);
        Comparator comp = mock(Comparator.class);
        option.appendUsage(buffer, helpSettings, comp);
        assertNotNull(buffer);
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();
        assertNotNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        assertNotNull(description);
    }

    @Test
    public void testGetId() {
        int id = option.getId();
        assertNotNull(id);
    }

    @Test
    public void testFindOption() {
        String trigger = "trigger";
        Option foundOption = option.findOption(trigger);
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        boolean isRequired = option.isRequired();
        assertNotNull(isRequired);
    }
}