package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.Mockito.*;

public class GroupImplTest {

    private static final String GROUP_NAME = "groupName";
    private static final String GROUP_DESCRIPTION = "groupDescription";
    private static final int GROUP_MINIMUM = 2;
    private static final int GROUP_MAXIMUM = 5;
    private static final List<Option> GROUP_OPTIONS = new ArrayList<>();
    private static final List<Argument> GROUP_ANONYMOUS = new ArrayList<>();

    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);

        final Option option1 = mock(Option.class);
        final Option option2 = mock(Option.class);
        final Option option3 = mock(Option.class);
        final Argument argument1 = mock(Argument.class);
        final Argument argument2 = mock(Argument.class);

        GROUP_OPTIONS.add(option1);
        GROUP_OPTIONS.add(option2);
        GROUP_OPTIONS.add(option3);

        GROUP_ANONYMOUS.add(argument1);
        GROUP_ANONYMOUS.add(argument2);

        group = new GroupImpl(GROUP_OPTIONS, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM, false);
    }

    @Test
    public void testCanProcess_WithValidOption() {
        when(commandLine.hasOption(option1.getPreferredName())).thenReturn(true);

        Assert.assertTrue(group.canProcess(commandLine, option1.getValue()));
        verify(commandLine).looksLikeOption(option1.getPreferredName());
    }

    @Test
    public void testCanProcess_WithInvalidOption() {
        Assert.assertFalse(group.canProcess(commandLine, "invalidOption"));
    }

    @Test
    public void testCanProcess_WithAnonymousArgument() {
        when(commandLine.looksLikeOption(anyString())).thenReturn(false);

        GROUP_OPTIONS.clear();
        GROUP_OPTIONS.add(option1);

        group = new GroupImpl(GROUP_OPTIONS, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM, false);

        when(commandLine.hasOption(option1.getPreferredName())).thenReturn(false);

        group.process(commandLine, commandLine.listIterator());

        verify(commandLine, times(GROUP_ANONYMOUS.size())).addOption(any(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithTooFewOptions() {
        when(commandLine.hasOption(option1.getPreferredName())).thenReturn(false);
        when(commandLine.hasOption(option2.getPreferredName())).thenReturn(false);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithTooManyOptions() {
        when(commandLine.hasOption(option1.getPreferredName())).thenReturn(true);
        when(commandLine.hasOption(option2.getPreferredName())).thenReturn(true);
        when(commandLine.hasOption(option3.getPreferredName())).thenReturn(true);

        group.validate(commandLine);
    }

    @Test
    public void testValidate_WithValidOptions() {
        when(commandLine.hasOption(option1.getPreferredName())).thenReturn(true);
        when(commandLine.hasOption(option2.getPreferredName())).thenReturn(true);

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage_WithValidGroup() {
        final StringBuilder sb = new StringBuilder();

        group.appendUsage(sb, null, null);

        Assert.assertEquals("groupName\n" +
                "groupDescription\n" +
                "[option1]\n" +
                "option1Description\n" +
                "[option2]\n" +
                "option2Description\n" +
                "[option3]\n" +
                "option3Description\n", sb.toString());
    }

    @Test
    public void testHelpLines_WithValidGroup() {
        final List<HelpLine> helpLines = group.helpLines(0, null, null);

        Assert.assertEquals(4, helpLines.size());
    }

    @Test
    public void testGetOptions_WithValidGroup() {
        final List<Option> options = group.getOptions();

        Assert.assertEquals(3, options.size());
    }

    @Test
    public void testGetAnonymous_WithValidGroup() {
        final List<Argument> anonymous = group.getAnonymous();

        Assert.assertEquals(2, anonymous.size());
    }
}