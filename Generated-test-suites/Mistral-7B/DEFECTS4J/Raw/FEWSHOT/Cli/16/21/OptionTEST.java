package org.apache.commons.cli2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }

    @Test
    public void testProcess() {
        when(option.getId()).thenReturn(1);
        when(option.process(commandLine, new ListIterator<String>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                return "arg";
            }
        })).thenReturn(true);

        option.process(commandLine, new ListIterator<String>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                return "arg";
            }
        });

        verify(commandLine).setValue("arg", 1);
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);

        verify(commandLine).setValue("defaultValue", 1);
    }

    @Test
    public void testCanProcess() {
        Set<String> triggers = new HashSet<>();
        triggers.add("--opt");
        when(option.getTriggers()).thenReturn(triggers);

        when(option.canProcess(commandLine, "arg")).thenReturn(true);

        assertTrue(option.canProcess(commandLine, "arg"));
        verify(commandLine).setValue("arg", 1);

        when(option.canProcess(commandLine, "arg--opt")).thenReturn(false);

        assertFalse(option.canProcess(commandLine, "arg--opt"));
    }

    @Test
    public void testHelpLines() {
        Set<String> triggers = new HashSet<>();
        triggers.add("--opt");
        when(option.getTriggers()).thenReturn(triggers);
        when(option.getPrefixes()).thenReturn(new HashSet<>());
        when(option.getDescription()).thenReturn("option description");

        List<HelpLine> helpLines = option.helpLines(0, new HashSet<>(), null);

        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("--opt", helpLine.getLongOpt());
        assertEquals("opt", helpLine.getOpt());
        assertEquals("option description", helpLine.getDescription());
    }

    @Test
    public void testAppendUsage() {
        Set<String> triggers = new HashSet<>();
        triggers.add("--opt");
        when(option.getTriggers()).thenReturn(triggers);
        when(option.getPrefixes()).thenReturn(new HashSet<>());
        when(option.getDescription()).thenReturn("option description");

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet<>(), null);

        assertEquals("--opt\n\toption description\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        when(option.getPreferredName()).thenReturn("opt");

        assertEquals("opt", option.getPreferredName());
    }

    @Test
    public void testFindOption() {
        Set<String> triggers = new HashSet<>();
        triggers.add("--opt1");
        triggers.add("--opt2");
        when(option.getTriggers()).thenReturn(triggers);

        Option opt1 = mock(Option.class);
        Option opt2 = mock(Option.class);
        when(opt1.getPreferredName()).thenReturn("opt1");
        when(opt2.getPreferredName()).thenReturn("opt2");

        when(findOption(triggers.iterator().next())).thenReturn(opt1);
        when(opt1.findOption("--opt2")).thenReturn(opt2);

        Option foundOption = option.findOption("--opt2");

        assertEquals(opt2, foundOption);
    }

    @Test
    public void testIsRequired() {
        when(option.isRequired()).thenReturn(true);

        assertTrue(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parent = mock(Option.class);
        when(option.getParent()).thenReturn(parent);

        assertEquals(parent, option.getParent());
    }

    @Test
    public void testSetParent() {
        Option parent = mock(Option.class);

        option.setParent(parent);

        assertEquals(parent, option.getParent());
    }

    private Option findOption(String trigger) {
        // Replace this with actual implementation
        return null;
    }
}