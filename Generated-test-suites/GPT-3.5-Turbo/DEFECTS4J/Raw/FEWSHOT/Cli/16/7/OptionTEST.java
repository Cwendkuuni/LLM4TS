package org.apache.commons.cli2;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;

public class OptionTest {
    @Mock
    WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

    @Test
    public void testProcess() throws OptionException {
        Option option = new OptionImpl("testOption");
        ListIterator<String> args = Arrays.asList("arg1", "arg2", "arg3").listIterator();
        option.process(commandLine, args);
        verify(commandLine, times(1)).addValue(option.getName(), "arg1");
        verify(commandLine, times(1)).addValue(option.getName(), "arg2");
        verify(commandLine, times(1)).addValue(option.getName(), "arg3");

        Option option2 = new OptionImpl("testOption2");
        ListIterator<String> args2 = Arrays.asList("arg4", "arg5").listIterator();
        option2.process(commandLine, args2);
        verify(commandLine, times(1)).addValue(option2.getName(), "arg4");
        verify(commandLine, times(1)).addValue(option2.getName(), "arg5");
    }

    @Test
    public void testDefaults() throws OptionException {
        Option option = new OptionImpl("testOption");
        option.defaults(commandLine);
        verify(commandLine, times(1)).addDefaultValue(option.getName());

        Option option2 = new OptionImpl("testOption2");
        option2.defaults(commandLine);
        verify(commandLine, times(1)).addDefaultValue(option2.getName());
    }

    @Test
    public void testCanProcess() {
        Option option = new OptionImpl("testOption");
        assertTrue(option.canProcess(commandLine, "arg1"));
        assertFalse(option.canProcess(commandLine, "-arg1"));

        Option option2 = new OptionImpl("testOption2");
        assertTrue(option2.canProcess(commandLine, "arg2"));
        assertFalse(option2.canProcess(commandLine, "-arg2"));
    }

    @Test
    public void testGetTriggers() {
        Option option = new OptionImpl("testOption");
        Set<String> triggers = option.getTriggers();
        assertEquals(1, triggers.size());
        assertEquals("testOption", triggers.iterator().next());

        Option option2 = new OptionImpl("testOption2");
        Set<String> triggers2 = option2.getTriggers();
        assertEquals(1, triggers2.size());
        assertEquals("testOption2", triggers2.iterator().next());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new OptionImpl("testOption");
        Set<String> prefixes = option.getPrefixes();
        assertEquals(0, prefixes.size());

        Option option2 = new OptionImpl("testOption2");
        Set<String> prefixes2 = option2.getPrefixes();
        assertEquals(0, prefixes2.size());
    }

    @Test
    public void testValidate() throws OptionException {
        Option option = new OptionImpl("testOption");
        option.validate(commandLine);
        verify(commandLine, times(0)).addError(anyString());

        Option option2 = new OptionImpl("testOption2");
        option2.validate(commandLine);
        verify(commandLine, times(0)).addError(anyString());
    }

    @Test
    public void testHelpLines() {
        Option option = new OptionImpl("testOption");
        int depth = 0;
        Set<HelpSetting> helpSettings = new HashSet<>();
        Comparator comp = null;
        List helpLines = option.helpLines(depth, helpSettings, comp);
        assertNotNull(helpLines);

        Option option2 = new OptionImpl("testOption2");
        List helpLines2 = option2.helpLines(depth, helpSettings, comp);
        assertNotNull(helpLines2);
    }

    @Test
    public void testAppendUsage() {
        Option option = new OptionImpl("testOption");
        StringBuffer buffer = new StringBuffer();
        Set<HelpSetting> helpSettings = new HashSet<>();
        Comparator comp = null;
        option.appendUsage(buffer, helpSettings, comp);
        assertNotNull(buffer);

        Option option2 = new OptionImpl("testOption2");
        StringBuffer buffer2 = new StringBuffer();
        option2.appendUsage(buffer2, helpSettings, comp);
        assertNotNull(buffer2);
    }

    @Test
    public void testGetPreferredName() {
        Option option = new OptionImpl("testOption");
        assertEquals("testOption", option.getPreferredName());

        Option option2 = new OptionImpl("testOption2");
        assertEquals("testOption2", option2.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new OptionImpl("testOption");
        assertEquals("", option.getDescription());

        Option option2 = new OptionImpl("testOption2");
        assertEquals("", option2.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new OptionImpl("testOption");
        assertEquals(0, option.getId());

        Option option2 = new OptionImpl("testOption2");
        assertEquals(0, option2.getId());
    }

    @Test
    public void testFindOption() {
        Option option = new OptionImpl("testOption");
        assertNull(option.findOption(""));

        Option option2 = new OptionImpl("testOption2");
        assertNull(option2.findOption(""));
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl("testOption");
        assertFalse(option.isRequired());

        Option option2 = new OptionImpl("testOption2");
        assertFalse(option2.isRequired());
    }
}