package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.mockito.Mockito.*; // for Mockito framework

public class GroupImplTest {
    

    private WriteableCommandLine commandLine;
    private GroupImpl group;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);
        group = new GroupImpl(createOptions(), "MyGroup", "Group description", 1, 2, false);
    }

    @Test
    public void testCanProcess_WithValidOption() {
        when(commandLine.hasOption("-o")).thenReturn(true);

        Assert.assertTrue(group.canProcess(commandLine, "o"));
    }

    @Test
    public void testCanProcess_WithInvalidOption() {
        when(commandLine.hasOption("-o")).thenReturn(false);

        Assert.assertFalse(group.canProcess(commandLine, "o"));
    }

    @Test
    public void testCanProcess_WithBurstingOption() {
        when(commandLine.hasOption("-b")).thenReturn(true);

        final Option burstingOption = new Option("b", "bursting", true, "Bursting option");
        group.addOption(burstingOption);

        Assert.assertTrue(group.canProcess(commandLine, "b"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_WithTooManyOptions() {
        when(commandLine.hasOption("-o")).thenReturn(true);

        Assert.assertTrue(group.canProcess(commandLine, "o"));
        Assert.assertTrue(group.canProcess(commandLine, "o"));

        group.validate(commandLine);
    }

    @Test
    public void testCanProcess_WithAnonymousArgument() {
        when(commandLine.hasOption("-a")).thenReturn(true);

        final Argument anonymousArgument = new Argument("a", "anonymous argument");
        group.addArgument(anonymousArgument);

        Assert.assertTrue(group.canProcess(commandLine, "a"));
    }

    @Test
    public void testProcess_WithValidOption() {
        when(commandLine.hasOption("-o")).thenReturn(true);

        group.process(commandLine, getListIterator());

        verify(commandLine).addOption(any());
    }

    @Test(expected = OptionException.class)
    public void testProcess_WithInvalidOption() {
        when(commandLine.hasOption("-o")).thenReturn(false);

        group.process(commandLine, getListIterator());
    }

    @Test
    public void testProcess_WithBurstingOption() {
        when(commandLine.hasOption("-b")).thenReturn(true);

        final Option burstingOption = new Option("b", "bursting", true, "Bursting option");
        group.addOption(burstingOption);

        group.process(commandLine, getListIterator());

        verify(commandLine, times(2)).addOption(any());
    }

    @Test
    public void testProcess_WithAnonymousArgument() {
        when(commandLine.hasOption("-a")).thenReturn(true);

        final Argument anonymousArgument = new Argument("a", "anonymous argument");
        group.addArgument(anonymousArgument);

        group.process(commandLine, getListIterator());

        verify(commandLine).addArgument(any());
    }

    @Test
    public void testValidate_WithValidOptions() {
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithTooFewOptions() {
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithTooManyOptions() {
        group.addOption(new Option("o", "option", true, "An option"));
        group.addOption(new Option("o", "option", true, "An option"));
        group.addOption(new Option("o", "option", true, "An option"));

        group.validate(commandLine);
    }

    @Test
    public void testValidate_WithAnonymousArguments() {
        final Argument anonymousArgument1 = new Argument("a1", "Anonymous argument 1");
        final Argument anonymousArgument2 = new Argument("a2", "Anonymous argument 2");
        group.addArgument(anonymousArgument1);
        group.addArgument(anonymousArgument2);

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage_WithNoOptions() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, Collections.emptySet(), null);

        Assert.assertEquals("", buffer.toString());
    }

    @Test
    public void testAppendUsage_WithSingleOption() {
        final StringBuilder buffer = new StringBuilder();

        group.addOption(new Option("o", "option", true, "An option"));

        group.appendUsage(buffer, Collections.emptySet(), null);

        Assert.assertEquals("option \n", buffer.toString());
    }

    @Test
    public void testAppendUsage_WithMultipleOptions() {
        final StringBuilder buffer = new StringBuilder();

        group.addOption(new Option("o1", "option1", true, "Option 1"));
        group.addOption(new Option("o2", "option2", true, "Option 2"));

        group.appendUsage(buffer, Collections.emptySet(), null);

        Assert.assertEquals("option1 \noption2 \n", buffer.toString());
    }

    @Test
    public void testHelpLines_WithNoOptions() {
        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        Assert.assertEquals(0, helpLines.size());
    }

    @Test
    public void testHelpLines_WithSingleOption() {
        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        Assert.assertEquals(1, helpLines.size());

        final HelpLine helpLine = (HelpLine) helpLines.get(0);

        Assert.assertEquals("-o", helpLine.getOptionSpec().getLongOpt());
        Assert.assertEquals("An option", helpLine.getOptionSpec().getDescription());
    }

    @Test
    public void testHelpLines_WithMultipleOptions() {
        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        Assert.assertEquals(2, helpLines.size());

        final HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        final HelpLine helpLine2 = (HelpLine) helpLines.get(1);

        Assert.assertEquals("-o1", helpLine1.getOptionSpec().getLongOpt());
        Assert.assertEquals("Option 1", helpLine1.getOptionSpec().getDescription());

        Assert.assertEquals("-o2", helpLine2.getOptionSpec().getLongOpt());
        Assert.assertEquals("Option 2", helpLine2.getOptionSpec().getDescription());
    }

    private List<String> getListIterator() {
        return new ArrayList<String>() {{
            add("o");
            add(null);
        }};
    }

    private List<Option> createOptions() {
        final List options = new ArrayList<>();
        options.add(new Option("o", "option", true, "An option"));
        return options;
    }

    
}