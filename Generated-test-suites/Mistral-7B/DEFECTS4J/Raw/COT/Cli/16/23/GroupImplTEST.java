package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    @Mock
    private WriteableCommandLine commandLine;

    private GroupImpl group;

    @Before
    public void setUp() {
        final List options = new ArrayList();
        options.add(Mockito.spy(new Option("option1", "option1Description")));
        options.add(Mockito.spy(new Option("option2", "option2Description")));
        options.add(Mockito.spy(new Option("arg1", "arg1Description")));

        group = new GroupImpl(options, "groupName", "groupDescription", 0, Integer.MAX_VALUE);
    }

    @Test
    public void testCanProcess_OptionFound() {
        Mockito.when(commandLine.looksLikeOption("option1")).thenReturn(true);

        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_OptionNotFound() {
        Mockito.when(commandLine.looksLikeOption("option1")).thenReturn(false);

        assertFalse(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        Mockito.when(commandLine.looksLikeOption("arg1")).thenReturn(false);

        assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_TooManyOptions() {
        Mockito.when(commandLine.looksLikeOption("option1")).thenReturn(true);
        Mockito.when(commandLine.looksLikeOption("option1")).thenReturn(true);

        group.canProcess(commandLine, "option1");
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();

        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();

        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testProcess_NoOptions() {
        ListIterator arguments = Mockito.mock(ListIterator.class);

        group.process(commandLine, arguments);

        Mockito.verifyNoInteractions(commandLine);
        Mockito.verifyNoInteractions(arguments);
    }

    @Test
    public void testProcess_SingleOption() {
        Mockito.when(commandLine.looksLikeOption("option1")).thenReturn(true);
        ListIterator arguments = Mockito.mock(ListIterator.class);

        group.process(commandLine, arguments);

        Mockito.verify(commandLine).processOption("option1", null);
        Mockito.verify(arguments).previous();
    }

    @Test
    public void testProcess_MultipleOptions() {
        Mockito.when(commandLine.looksLikeOption("option1")).thenReturn(true);
        Mockito.when(commandLine.looksLikeOption("option2")).thenReturn(true);
        ListIterator arguments = Mockito.mock(ListIterator.class);

        group.process(commandLine, arguments);

        Mockito.verify(commandLine).processOption("option1", null);
        Mockito.verify(commandLine).processOption("option2", null);
        Mockito.verify(arguments).previous();
        Mockito.verify(arguments).previous();
    }

    @Test
    public void testProcess_AnonymousArgument() {
        Mockito.when(commandLine.looksLikeOption("arg1")).thenReturn(false);
        ListIterator arguments = Mockito.mock(ListIterator.class);

        final Argument arg = Mockito.spy(new Argument("arg1", "arg1Description"));
        group.anonymous.add(arg);

        group.process(commandLine, arguments);

        Mockito.verifyZeroInteractions(commandLine);
        Mockito.verify(arg).process(commandLine, arguments);
        Mockito.verify(arguments).previous();
    }

    @Test
    public void testValidate_NoOptions() {
        group.validate(commandLine);

        Mockito.verifyNoInteractions(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooFewOptions() {
        Mockito.when(commandLine.size()).thenReturn(1);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooManyOptions() {
        Mockito.when(commandLine.size()).thenReturn(3);

        group.validate(commandLine);
    }

    @Test
    public void testValidate_ValidOptions() {
        Mockito.when(commandLine.size()).thenReturn(2);

        group.validate(commandLine);

        Mockito.verifyNoInteractions(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("groupName", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("groupDescription", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("groupDescription\n" +
                "[option1] option1Description\n" +
                "[option2] option2Description\n" +
                "[arg1] arg1Description", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(3, helpLines.size());

        final HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("groupName", helpLine1.getOptionSpec().getName());
        assertEquals("groupDescription", helpLine1.getShortDescription());

        final HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("option1", helpLine2.getOptionSpec().getName());
        assertEquals("option1Description", helpLine2.getShortDescription());

        final HelpLine helpLine3 = (HelpLine) helpLines.get(2);
        assertEquals("option2", helpLine3.getOptionSpec().getName());
        assertEquals("option2Description", helpLine3.getShortDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(1, group.getAnonymous().size());
    }

    @Test
    public void testFindOption() {
        assertSame("option1", group.findOption("option1"));
        assertNull(group.findOption("nonexistent"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        Mockito.verify(commandLine).clearParsed();
        Mockito.verifyNoMoreInteractions(commandLine);

        for (final Option option : group.getOptions()) {
            option.defaults(commandLine);
        }

        for (final Option option : group.getAnonymous()) {
            option.defaults(commandLine);
        }
    }

    @Test
    public void testReverseStringComparator() {
        Comparator comparator = ReverseStringComparator.getInstance();

        List list = new ArrayList();
        list.add("z");
        list.add("a");
        list.add("x");

        Collections.sort(list, comparator);

        assertEquals("x", list.get(0));
        assertEquals("a", list.get(1));
        assertEquals("z", list.get(2));
    }
}