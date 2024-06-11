package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {
    private Group group;

    @Mock
    private WriteableCommandLine commandLine;

    @Mock
    private Option option;

    @Mock
    private Argument argument;

    @Mock
    private HelpLine helpLine;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(option);
        options.add(option);

        List<Argument> anonymous = new ArrayList<>();
        anonymous.add(argument);

        SortedMap<String, Option> optionMap = new TreeMap<>(ReverseStringComparator.getInstance());

        Set<String> prefixes = new HashSet<>();

        group = new GroupImpl(options, null, null, 0, 0, anonymous, optionMap, prefixes);
    }

    @Test
    public void testCanProcess_withOption() {
        String optionName = "testOption";
        when(option.getTrigger()).thenReturn(optionName);
        when(option.canProcess(commandLine, optionName)).thenReturn(true);

        assertTrue(group.canProcess(commandLine, optionName));
        verify(option).canProcess(commandLine, optionName);
    }

    @Test
    public void testCanProcess_withoutOption() {
        String arg = "testArg";
        assertFalse(group.canProcess(commandLine, arg));

        // Test case for the case when an option is found after the given arg
        Option option = new Option("testOption", "testOption", false, "test");
        when(optionMap.get(arg)).thenReturn(option);
        assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testValidate_withMinimumNumberOfOptions() {
        when(commandLine.hasOption(argument.getLongOpt())).thenReturn(true);
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_withLessThanMinimumNumberOfOptions() {
        when(commandLine.hasOption(argument.getLongOpt())).thenReturn(false);
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_withMoreThanMaximumNumberOfOptions() {
        when(commandLine.hasOption(argument.getLongOpt())).thenReturn(true);
        when(commandLine.hasOption(argument.getLongOpt())).thenReturn(true);
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        String expectedUsage = "[testOption]\n" +
                " testOption: testOption\n";

        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, null, null);
        assertNotNull(helpLines);
        assertEquals(2, helpLines.size());
    }

    @Test
    public void testFindOption() {
        when(optionMap.get(ArgumentMatchers.eq("testOption"))).thenReturn(option);
        Option foundOption = group.findOption("testOption");
        assertSame(option, foundOption);
    }

    @Test
    public void testFindOption_whenOptionNotFound() {
        Option foundOption = group.findOption("testOptionNotFound");
        assertNull(foundOption);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetAnonymous() {
        List<Argument> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAnonymous_whenAccessedDirectly() {
        group.anonymous.clear();
        assertThrows(UnsupportedOperationException.class, () -> group.anonymous.get(0));
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        verify(option, times(2)).defaults(commandLine);
        verify(argument, times(1)).defaults(commandLine);
    }
}