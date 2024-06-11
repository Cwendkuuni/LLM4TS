package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    @Mock
    private WriteableCommandLine commandLine;

    @Mock
    private Option option;

    @Mock
    private Argument argument;

    private GroupImpl group;

    @Before
    public void setUp() {
        final List options = new ArrayList();
        options.add(option);
        options.add(argument);

        final List anonymous = new ArrayList();
        anonymous.add(argument);

        group = new GroupImpl(options, "groupName", "groupDescription", 0, 1, anonymous, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void testCanProcess() {
        Mockito.when(commandLine.looksLikeOption(ArgumentMatchers.anyString())).thenReturn(false);

        assertTrue(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, "trigger"));
        assertTrue(group.canProcess(commandLine, "trigger1"));
        assertTrue(group.canProcess(commandLine, "trigger1"));
        assertFalse(group.canProcess(commandLine, "trigger2"));
        assertFalse(group.canProcess(commandLine, "trigger3"));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(true);

        for (int i = 0; i < group.getMaximum() + 1; i++) {
            Mockito.when(commandLine.hasOption(String.valueOf(i))).thenReturn(true);
        }

        group.validate(commandLine);
    }

    @Test
    public void testProcess() {
        final List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("arg3");
        arguments.add("arg4");

        Mockito.when(commandLine.getArguments()).thenReturn(arguments);

        group.process(commandLine, arguments.listIterator());

        Mockito.verify(option, Mockito.times(1)).process(commandLine, arguments.listIterator());
        Mockito.verify(argument, Mockito.times(1)).process(commandLine, arguments.listIterator());
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, new HashSet<>(), null);

        assertTrue(buffer.toString().contains("groupName"));
        assertTrue(buffer.toString().contains("groupDescription"));
        assertTrue(buffer.toString().contains("|"));
        assertTrue(buffer.toString().contains("trigger1"));
        assertTrue(buffer.toString().contains("trigger2"));
        assertTrue(buffer.toString().contains("."));
        assertTrue(buffer.toString().contains("argName"));
    }

    @Test
    public void testHelpLines() {
        final List helpLines = group.helpLines(1, new HashSet<>(), null);

        assertTrue(helpLines.size() > 0);
        assertTrue(helpLines.get(0) instanceof HelpLine);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }
}