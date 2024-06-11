package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {
    private WriteableCommandLine commandLine;
    private Group group;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        List<Option> options = new ArrayList<>();
        Option option1 = Mockito.spy(new Option("option1", "Opt 1"));
        Option option2 = Mockito.spy(new Option("option2", "Opt 2"));
        Argument arg = Mockito.spy(new Argument("argument", "Arg"));
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 1, 2, false);
    }

    

    @Test
    public void testCanProcess_ValidOption() {
        Assert.assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_BurstingOption() {
        Mockito.when(commandLine.hasOption("option1")).thenReturn(true);
        Assert.assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_InvalidOption() {
        Assert.assertFalse(group.canProcess(commandLine, "invalid"));
    }

    @Test
    public void testCanProcess_Argument() {
        Assert.assertTrue(group.canProcess(commandLine, "argument"));
    }

    @Test
    public void testCanProcess_EmptyCommandLine() {
        Assert.assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testProcess_ValidOption() {
        Mockito.when(commandLine.hasOption("option1")).thenReturn(true);

        List<String> arguments = new ArrayList<>();
        group.process(commandLine, arguments.listIterator());

        Mockito.verify(option1).process(commandLine, arguments.listIterator());
    }

    @Test
    public void testProcess_BurstingOption() {
        Mockito.when(commandLine.hasOption("option1")).thenReturn(true);

        List<String> arguments = new ArrayList<>();
        group.process(commandLine, arguments.listIterator());
        group.process(commandLine, arguments.listIterator());

        Mockito.verify(option1, Mockito.times(2)).process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption() {
        Mockito.when(commandLine.hasOption("invalid")).thenReturn(true);

        List<String> arguments = new ArrayList<>();
        group.process(commandLine, arguments.listIterator());

        Mockito.verify(option1, Mockito.never()).process(commandLine, arguments.listIterator());
    }

    @Test
    public void testProcess_Argument() {
        Mockito.when(commandLine.hasOption("argument")).thenReturn(false);

        List<String> arguments = new ArrayList<>();
        group.process(commandLine, arguments.listIterator());

        Mockito.verify(arg, Mockito.never()).process(commandLine, arguments.listIterator());
    }

    @Test
    public void testProcess_EmptyCommandLine() {
        List<String> arguments = new ArrayList<>();
        group.process(commandLine, arguments.listIterator());

        Mockito.verify(option1, Mockito.never()).process(commandLine, arguments.listIterator());
        Mockito.verify(option2, Mockito.never()).process(commandLine, arguments.listIterator());
    }

    @Test
    public void testValidate_ValidOptions() {
        group.validate(commandLine);

        Mockito.verifyNoMoreInteractions(commandLine);
        Mockito.verifyNoMoreInteractions(option1);
        Mockito.verifyNoMoreInteractions(option2);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions() {
        Mockito.when(commandLine.hasOption("option1")).thenReturn(false);

        group.validate(commandLine);

        Mockito.verify(option1).validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooFewOptions() {
        Mockito.when(commandLine.size())
                .thenReturn(commandLine.size() - 1);

        group.validate(commandLine);

        Mockito.verify(option1).validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooManyOptions() {
        List<String> arguments = new ArrayList<>();
        arguments.add("option1");
        arguments.add("option2");
        arguments.add("option3");

        Mockito.when(commandLine.size())
                .thenReturn(commandLine.size() + 1);

        group.validate(commandLine);

        Mockito.verify(option1).validate(commandLine);
        Mockito.verify(option2).validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "|group " + (group.isRequired() ? "" : "[") + "\n";
        String actualUsage = buffer.toString();

        Assert.assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, null, null);

        Assert.assertEquals(2, helpLines.size());
    }

    
}