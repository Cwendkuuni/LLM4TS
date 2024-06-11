package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.GroupImpl;
import org.apache.commons.cli2.Group;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    private Group group;
    private WriteableCommandLine commandLine;
    private List<Option> options;

    @Before
    public void setup() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        options = new ArrayList<>();

        final Option option1 = Mockito.mock(Option.class);
        final Option option2 = Mockito.mock(Option.class);
        final Argument argument = Mockito.mock(Argument.class);

        options.add(option1);
        options.add(option2);

        group = new GroupImpl(options, null, null, 0, 1);
    }

    @Test
    public void testCanProcess_whenOptionIsPresent_shouldReturnTrue() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.eq(option1.getLongOpt()))).thenReturn(true);

        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_whenOptionIsNotPresent_shouldReturnFalse() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.eq(option1.getLongOpt()))).thenReturn(false);

        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_whenArgumentIsPresent_shouldReturnTrue() {
        final ListIterator<String> arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.hasNext()).thenReturn(true);
        Mockito.when(arguments.next()).thenReturn("test");

        assertTrue(group.canProcess(commandLine, arguments));
    }

    @Test(expected = OptionException.class)
    public void testValidate_whenNumberOfOptionsIsLessThanMinimum_shouldThrowOptionException() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_whenNumberOfOptionsIsMoreThanMaximum_shouldThrowOptionException() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(true);

        group.validate(commandLine);
    }

    @Test
    public void testValidate_whenNumberOfOptionsIsWithinLimit_shouldNotThrowOptionException() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(true);

        group.validate(commandLine);
    }

    @Test
    public void testProcess_whenOptionIsPresent_shouldProcessTheOption() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.eq(option1.getLongOpt()))).thenReturn(true);
        Mockito.when(option1.process(commandLine, ArgumentMatchers.any(ListIterator.class))).thenReturn();

        group.process(commandLine, Mockito.mock(ListIterator.class));

        Mockito.verify(option1).process(commandLine, ArgumentMatchers.any(ListIterator.class));
    }

    @Test
    public void testProcess_whenOptionIsNotPresent_shouldNotProcessTheOption() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);

        group.process(commandLine, Mockito.mock(ListIterator.class));

        Mockito.verifyNoMoreInteractions(option1);
    }

    @Test
    public void testProcess_whenArgumentIsPresent_shouldProcessTheArgument() {
        final ListIterator<String> arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.hasNext()).thenReturn(true);
        Mockito.when(arguments.next()).thenReturn("test");

        group.process(commandLine, arguments);

        Mockito.verify(argument).process(commandLine, ArgumentMatchers.any(ListIterator.class));
    }

    @Test
    public void testGetName_shouldReturnGroupName() {
        assertNotNull(group.getName());
    }

    @Test
    public void testGetDescription_shouldReturnGroupDescription() {
        assertNotNull(group.getDescription());
    }

    @Test
    public void testGetOptions_shouldReturnListOfOptions() {
        assertNotNull(group.getOptions());
    }

    @Test
    public void testGetAnonymous_shouldReturnListOfAnonymousArguments() {
        assertNotNull(group.getAnonymous());
    }

    @Test
    public void testFindOption_shouldReturnOptionWithTrigger() {
        final String trigger = "test";
        final Option option = new Option("test", "Test Option", false, null, null);
        options.set(0, option);

        assertNotNull(group.findOption(trigger));
    }

    @Test
    public void testFindOption_shouldReturnNullWhenOptionIsNotPresent() {
        final String trigger = "test";

        assertNull(group.findOption(trigger));
    }

    @Test
    public void testGetMinimum_shouldReturnMinimumNumberOfOptions() {
        assertTrue(group.getMinimum() > 0);
    }

    @Test
    public void testGetMaximum_shouldReturnMaximumNumberOfOptions() {
        assertTrue(group.getMaximum() > 0);
    }

    @Test
    public void testIsRequired_shouldReturnTrueWhenMinimumIsGreaterThanZero() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testIsRequired_shouldReturnFalseWhenMinimumIsZero() {
        final Group group = new GroupImpl(options, null, null, 0, 0);

        assertFalse(group.isRequired());
    }

    @Test
    public void testAppendUsage_shouldAppendUsageInformationForGroup() {
        final List<String> expectedHelpLines = new ArrayList<>();
        expectedHelpLines.add("testGroup:");
        expectedHelpLines.add(" OPTIONS:");
        expectedHelpLines.add("");
        expectedHelpLines.add("  --test:Test Option");
        expectedHelpLines.add("");

        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comparator = null;

        group.appendUsage(buffer, helpSettings, comparator);

        final List<String> helpLines = group.helpLines(0, helpSettings, comparator);

        for (int i = 0; i < expectedHelpLines.size(); i++) {
            assertTrue(helpLines.get(i).startsWith(expectedHelpLines.get(i)));
        }
    }
}