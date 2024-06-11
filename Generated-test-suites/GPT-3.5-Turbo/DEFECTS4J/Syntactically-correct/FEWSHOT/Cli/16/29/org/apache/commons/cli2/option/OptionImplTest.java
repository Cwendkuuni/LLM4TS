package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setUp() {
        option = Mockito.mock(OptionImpl.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator<String> arguments = Arrays.asList("arg1").listIterator();
        Mockito.when(option.canProcess(commandLine, "arg1")).thenReturn(true);
        assertTrue(option.canProcess(commandLine, arguments));
        Mockito.verify(option).canProcess(commandLine, "arg1");

        arguments = Arrays.asList().listIterator();
        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, DisplaySetting.ALL, null);
        Mockito.when(option.toString()).thenReturn(buffer.toString());
        assertEquals(buffer.toString(), option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl otherOption = Mockito.mock(OptionImpl.class);
        Mockito.when(option.getId()).thenReturn(1);
        Mockito.when(option.getPreferredName()).thenReturn("name");
        Mockito.when(option.getDescription()).thenReturn("description");
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);
        Set<String> triggers = new HashSet<String>();
        triggers.add("trigger");
        Mockito.when(option.getTriggers()).thenReturn(triggers);

        Mockito.when(otherOption.getId()).thenReturn(1);
        Mockito.when(otherOption.getPreferredName()).thenReturn("name");
        Mockito.when(otherOption.getDescription()).thenReturn("description");
        Mockito.when(otherOption.getPrefixes()).thenReturn(prefixes);
        Mockito.when(otherOption.getTriggers()).thenReturn(triggers);

        assertTrue(option.equals(otherOption));

        Mockito.when(otherOption.getId()).thenReturn(2);
        assertFalse(option.equals(otherOption));
    }

    @Test
    public void testHashCode() {
        Mockito.when(option.getId()).thenReturn(1);
        Mockito.when(option.getPreferredName()).thenReturn("name");
        Mockito.when(option.getDescription()).thenReturn("description");
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);
        Set<String> triggers = new HashSet<String>();
        triggers.add("trigger");
        Mockito.when(option.getTriggers()).thenReturn(triggers);

        int expectedHashCode = 1 + 37 * "name".hashCode() + 37 * "description".hashCode() + 37 * prefixes.hashCode()
                + 37 * triggers.hashCode();

        assertEquals(expectedHashCode, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl triggerOption = Mockito.mock(OptionImpl.class);
        Set<String> triggers = new HashSet<String>();
        triggers.add("trigger");
        Mockito.when(triggerOption.getTriggers()).thenReturn(triggers);

        Option result = option.findOption("trigger");
        assertNull(result);

        Mockito.when(option.getTriggers()).thenReturn(triggers);
        result = option.findOption("trigger");
        assertEquals(option, result);

        result = option.findOption("invalid");
        assertNull(result);
    }

    @Test
    public void testIsRequired() {
        Mockito.when(option.isRequired()).thenReturn(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        option.defaults(commandLine);
        Mockito.verify(option).defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        ResourceHelper helper = ResourceHelper.getResourceHelper();
        String message = helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "trigger", "prefixes");
        Mockito.when(option.getPreferredName()).thenReturn("trigger");

        Set<String> prefixes = new HashSet<String>();
        option.checkPrefixes(prefixes);
        Mockito.verify(option, Mockito.never()).checkPrefix(prefixes, "trigger");

        prefixes.add("-");
        option.checkPrefixes(prefixes);
        Mockito.verify(option).checkPrefix(prefixes, "trigger");

        prefixes.clear();
        prefixes.add("--");
        option.checkPrefixes(prefixes);
        Mockito.verify(option).checkPrefix(prefixes, "trigger");

        prefixes.clear();
        prefixes.add("invalid");
        try {
            option.checkPrefixes(prefixes);
        } catch (IllegalArgumentException e) {
            assertEquals(message, e.getMessage());
        }

        Mockito.when(option.getTriggers()).thenReturn(new HashSet<String>());
        option.checkPrefixes(prefixes);
        Mockito.verify(option, Mockito.never()).checkPrefix(prefixes, Mockito.anyString());
    }
}