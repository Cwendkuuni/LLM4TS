package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    @Mock
    private WriteableCommandLine commandLine;

    private GroupImpl group;
    private Option option;
    private List<Option> options;
    private List<Argument> anonymous;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        anonymous = new ArrayList<>();

        option = Mockito.mock(Option.class);
        Mockito.when(option.getTriggers()).thenReturn(Collections.singleton("opt"));

        options.add(option);
        group = new GroupImpl(options, "group", null, 0, 1, false);
        anonymous.add(Mockito.mock(Argument.class));
        group.setAnonymous(anonymous);
    }

    @Test
    public void testCanProcessWithOption() {
        Mockito.when(commandLine.hasOption("opt")).thenReturn(true);
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessWithUnmatchedOption() {
        assertFalse(group.canProcess(commandLine, "unmatched"));
    }

    @Test
    public void testCanProcessWithArgument() {
        Mockito.when(commandLine.hasOption("opt")).thenReturn(false);
        Mockito.when(anonymous.get(0).canProcess(commandLine, null)).thenReturn(true);
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithTooFewOptions() throws OptionException {
        group.validate(commandLine);
        group.validate(commandLine);
        assertFalse(group.canProcess(commandLine, null));
        group.process(commandLine, null);
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithTooManyOptions() throws OptionException {
        Mockito.when(commandLine.hasOption("opt")).thenReturn(true);
        Mockito.when(commandLine.hasOption("opt2")).thenReturn(true);
        group.validate(commandLine);
        assertFalse(group.canProcess(commandLine, null));
        group.process(commandLine, null);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(anonymous, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        assertSame(option, group.findOption("opt"));
        assertNull(group.findOption("unmatched"));
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

    @Test
    public void testProcess() throws OptionException {
        group.process(commandLine, null);
        Mockito.verify(option).process(commandLine, null);
    }

    @Test
    public void testValidate() throws OptionException {
        group.validate(commandLine);
        Mockito.verify(option).validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        group.appendUsage(buffer, helpSettings, comp);

        final String expected = "[group]";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testAppendUsageWithExpanded() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettings, comp);

        final String expected = "[group]\n" +
                "|  --opt [value]                   OPTION_DESCRIPTION\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testAppendUsageWithName() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);

        group.appendUsage(buffer, helpSettings, comp);

        final String expected = "group:\n" +
                "|  --opt [value]                   OPTION_DESCRIPTION\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testAppendUsageWithExpandedAndName() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);

        group.appendUsage(buffer, helpSettings, comp);

        final String expected = "group:\n" +
                "|  --opt [value]                   OPTION_DESCRIPTION\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testAppendUsageWithArguments() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        group.appendUsage(buffer, helpSettings, comp);

        final String expected = "[group]\n" +
                "|  ARGUMENT_DESCRIPTION\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testAppendUsageWithExpandedAndArguments() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        group.appendUsage(buffer, helpSettings, comp);

        final String expected = "[group]\n" +
                "|  ARGUMENT_DESCRIPTION\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, comp);

        assertEquals(1, helpLines.size());

        final HelpLine helpLine = helpLines.get(0);

        assertEquals("group:\n" +
                "|  --opt [value]                   OPTION_DESCRIPTION\n", helpLine.getHelpString());
    }

    @Test
    public void testHelpLinesWithExpanded() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, comp);

        assertEquals(1, helpLines.size());

        final HelpLine helpLine = helpLines.get(0);

        assertEquals("[group]\n" +
                "|  --opt [value]                   OPTION_DESCRIPTION\n", helpLine.getHelpString());
    }

    @Test
    public void testHelpLinesWithName() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, comp);

        assertEquals(1, helpLines.size());

        final HelpLine helpLine = helpLines.get(0);

        assertEquals("group:\n" +
                "|  --opt [value]                   OPTION_DESCRIPTION\n", helpLine.getHelpString());
    }

    @Test
    public void testHelpLinesWithNameAndExpanded() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, comp);

        assertEquals(1, helpLines.size());

        final HelpLine helpLine = helpLines.get(0);

        assertEquals("group:\n" +
                "|  --opt [value]                   OPTION_DESCRIPTION\n", helpLine.getHelpString());
    }

    @Test
    public void testHelpLinesWithArguments() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, comp);

        assertEquals(1, helpLines.size());

        final HelpLine helpLine = helpLines.get(0);

        assertEquals("[group]\n" +
                "|  ARGUMENT_DESCRIPTION\n", helpLine.getHelpString());
    }

    @Test
    public void testHelpLinesWithExpandedAndArguments() {
        final StringBuilder buffer = new StringBuilder();
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<String> comp = null;

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, comp);

        assertEquals(1, helpLines.size());

        final HelpLine helpLine = helpLines.get(0);

        assertEquals("[group]\n" +
                "|  ARGUMENT_DESCRIPTION\n", helpLine.getHelpString());
    }
}