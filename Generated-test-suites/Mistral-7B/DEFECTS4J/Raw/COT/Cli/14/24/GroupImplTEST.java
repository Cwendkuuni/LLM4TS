package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupImplTest {

    @Test
    public void testGroupImpl_BasicFunctionality() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(mock(Option.class));
        options.add(mock(Option.class));

        final Group group = new GroupImpl(options, null, null, 0, 0);

        // When
        final WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        final ListIterator<String> arguments = mock(ListIterator.class);

        // Then
        Assert.assertFalse(group.canProcess(commandLine, null));
        Assert.assertFalse(group.canProcess(commandLine, ""));
        Assert.assertFalse(group.canProcess(commandLine, "notAnOption"));

        final String[] args = {"--groupOption1", "--groupOption2"};
        when(arguments.next()).thenReturn(args);

        group.process(commandLine, arguments);

        Assert.assertEquals(args[0], "groupOption1");
        Assert.assertEquals(args[1], "groupOption2");

        final Set<String> prefixes = group.getPrefixes();
        Assert.assertEquals(Collections.emptySet(), prefixes);

        final Set<String> triggers = group.getTriggers();
        Assert.assertEquals(Collections.emptySet(), triggers);

        Assert.assertEquals(0, group.getMinimum());
        Assert.assertEquals(0, group.getMaximum());
        Assert.assertEquals(false, group.isRequired());

        Assert.assertEquals(null, group.getPreferredName());
        Assert.assertEquals("", group.getDescription());

        final List<Option> groupOptions = group.getOptions();
        Assert.assertEquals(2, groupOptions.size());
        Assert.assertTrue(groupOptions.contains(mock(Option.class)));
        Assert.assertTrue(groupOptions.contains(mock(Option.class)));
    }

    @Test
    public void testGroupImpl_EdgeCases() {
        // Given
        final List<Option> options = new ArrayList<>();
        final Argument argument = mock(Argument.class);
        options.add(argument);
        options.add(mock(Option.class));

        final Group group = new GroupImpl(options, null, null, 0, Integer.MAX_VALUE);

        // When
        final WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        final ListIterator<String> arguments = mock(ListIterator.class);

        // Then
        when(arguments.next()).thenReturn("groupOption1");
        group.process(commandLine, arguments);
        Assert.assertEquals("groupOption1", argument.getValue());

        when(arguments.next()).thenReturn("groupOption2");
        group.process(commandLine, arguments);
        Assert.assertEquals("groupOption2", argument.getValue());

        final String[] args = {"--groupOption1", "--groupOption1"};
        when(arguments.next()).thenReturn(args);

        try {
            group.process(commandLine, arguments);
            Assert.fail("Expected OptionException");
        } catch (OptionException e) {
            // Expected
        }

        when(arguments.next()).thenReturn("--groupOption3");
        group.process(commandLine, arguments);
        Assert.assertEquals("groupOption3", argument.getValue());

        when(arguments.next()).thenReturn("--anotherOption");
        Assert.assertFalse(argument.getValue().isEmpty());
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_ExceptionHandling() {
        // Given
        final List<Option> options = new ArrayList<>();
        final Argument argument = mock(Argument.class);
        options.add(argument);
        options.add(mock(Option.class));

        final Group group = new GroupImpl(options, null, null, 0, 0);

        // When
        final WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        final ListIterator<String> arguments = mock(ListIterator.class);

        // Then
        when(arguments.next()).thenReturn("notAnOption");

        group.process(commandLine, arguments);
    }
}