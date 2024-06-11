package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
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
    public void testProcess_processesArgument() throws OptionException {
        List<String> args = new ArrayList<>();
        args.add("--option");
        args.add("value");

        when(option.process(commandLine, args.listIterator())).thenCallRealMethod();

        option.process(commandLine, args.listIterator());

        verifyProcessedArgument(args, "option", "value");
    }

    @Test
    public void testProcess_ignoresInvalidArgument() throws OptionException {
        List<String> args = new ArrayList<>();
        args.add("invalidArgument");

        when(option.canProcess(commandLine, args.listIterator())).thenReturn(false);

        option.process(commandLine, args.listIterator());

        assertEquals(1, args.size());
        assertEquals("invalidArgument", args.get(0));
    }

    @Test
    public void testProcess_throwsException() throws OptionException {
        List<String> args = new ArrayList<>();
        args.add("--option");

        when(option.process(commandLine, args.listIterator())).thenThrow(new OptionException("Test exception"));

        assertThrows(OptionException.class, () -> option.process(commandLine, args.listIterator()));

        assertEquals(0, args.size());
    }

    @Test
    public void testDefaults_setsDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);

        assertEquals("defaultValue", commandLine.getOptionValue("option"));
    }

    @Test
    public void testCanProcess_returnsTrueIfArgumentCanBeProcessed() {
        when(option.canProcess(commandLine, "optionValue")).thenReturn(true);

        assertTrue(option.canProcess(commandLine, "optionValue"));
    }

    @Test
    public void testCanProcess_returnsFalseIfArgumentCannotBeProcessed() {
        when(option.canProcess(commandLine, "optionValue")).thenReturn(false);

        assertFalse(option.canProcess(commandLine, "optionValue"));
    }

    @Test
    public void testCanProcessIterator_returnsTrueIfArgumentCanBeProcessed() {
        String[] args = {"--option"};
        ListIterator<String> iterator = Arrays.asList(args).listIterator();

        when(option.canProcess(commandLine, iterator)).thenCallRealMethod();

        assertTrue(option.canProcess(commandLine, iterator));
    }

    @Test
    public void testCanProcessIterator_returnsFalseIfArgumentCannotBeProcessed() {
        String[] args = {"--invalidOption"};
        ListIterator<String> iterator = Arrays.asList(args).listIterator();

        when(option.canProcess(commandLine, iterator)).thenReturn(false);

        assertFalse(option.canProcess(commandLine, iterator));
    }

    @Test
    public void testGetTriggers_returnsSetWithTriggers() {
        Set<String> triggers = new HashSet<>();
        triggers.add("--option");

        when(option.getTriggers()).thenReturn(triggers);

        assertEquals(triggers, option.getTriggers());
    }

    @Test
    public void testGetPrefixes_returnsSetWithPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("--");

        when(option.getPrefixes()).thenReturn(prefixes);

        assertEquals(prefixes, option.getPrefixes());
    }

    @Test
    public void testValidate_throwsExceptionIfCommandLineIsInvalid() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        assertThrows(OptionException.class, () -> option.validate(commandLine));
    }

    @Test
    public void testValidate_doesNotThrowExceptionIfCommandLineIsValid() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.setOptionValue("option", "value");

        option.validate(commandLine);
    }

    @Test
    public void testFindOption_returnsMatchingOption() {
        Option childOption = mock(Option.class);
        Option parentOption = mock(Option.class);

        when(option.findOption("--child")).thenReturn(childOption);
        when(childOption.getParent()).thenReturn(parentOption);
        when(parentOption.getId()).thenReturn(1);

        assertEquals(parentOption, option.getParent());
    }

    @Test
    public void testFindOption_returnsNullIfNoMatchingOptionFound() {
        Option childOption = mock(Option.class);
        Option parentOption = mock(Option.class);

        when(option.findOption("--unrelated")).thenReturn(null);
        when(childOption.getParent()).thenReturn(parentOption);
        when(parentOption.getId()).thenReturn(1);

        assertNull(option.getParent());
    }

    @Test
    public void testIsRequired_returnsTrueIfOptionIsRequired() {
        when(option.isRequired()).thenReturn(true);

        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired_returnsFalseIfOptionIsNotRequired() {
        when(option.isRequired()).thenReturn(false);

        assertFalse(option.isRequired());
    }

    @Test
    public void testHelpLines_returnsListOfHelpLines() {
        Set<String> helpSettings = new HashSet<>();
        Option childOption = mock(Option.class);
        Option parentOption = mock(Option.class);

        when(option.getHelpLines(1, helpSettings, null)).thenReturn(Arrays.asList(childOption));
        when(childOption.getHelpLines(2, helpSettings, null)).thenReturn(Arrays.asList());
        when(parentOption.getId()).thenReturn(1);

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, null);

        assertEquals(1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        assertEquals(2, helpLine.getDepth());
        assertEquals("help text for child option", helpLine.getText());
        assertEquals(parentOption, helpLine.getOption());
    }

    @Test
    public void testHelpLines_recursivelyBuildsHelpLinesForChildOptions() {
        Set<String> helpSettings = new HashSet<>();
        Option parentOption = mock(Option.class);
        Option childOption1 = mock(Option.class);
        Option childOption2 = mock(Option.class);

        when(option.getHelpLines(1, helpSettings, null)).thenReturn(Arrays.asList(childOption1, childOption2));
        when(childOption1.getHelpLines(2, helpSettings, null)).thenReturn(Arrays.asList());
        when(childOption2.getHelpLines(2, helpSettings, null)).thenReturn(Arrays.asList());
        when(parentOption.getId()).thenReturn(1);

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, null);

        assertEquals(3, helpLines.size());

        HelpLine helpLine1 = helpLines.get(0);
        assertEquals(2, helpLine1.getDepth());
        assertEquals("help text for child option 1", helpLine1.getText());
        assertEquals(childOption1, helpLine1.getOption());
        assertEquals(parentOption, helpLine1.getParentOption());

        HelpLine helpLine2 = helpLines.get(1);
        assertEquals(2, helpLine2.getDepth());
        assertEquals("help text for child option 2", helpLine2.getText());
        assertEquals(childOption2, helpLine2.getOption());
        assertEquals(parentOption, helpLine2.getParentOption());

        HelpLine helpLine3 = helpLines.get(2);
        assertEquals(1, helpLine3.getDepth());
        assertEquals("help text for parent option", helpLine3.getText());
        assertNull(helpLine3.getOption());
        assertNull(helpLine3.getParentOption());
    }

    @Test
    public void testAppendUsage_appendsUsageInformation() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("usage information for parent option", buffer.toString());
    }

    @Test
    public void testGetPreferredName_returnsPreferredName() {
        when(option.getPreferredName()).thenReturn("--preferredName");

        assertEquals("--preferredName", option.getPreferredName());
    }

    @Test
    public void testGetDescription_returnsDescription() {
        when(option.getDescription()).thenReturn("description for parent option");

        assertEquals("description for parent option", option.getDescription());
    }

    @Test
    public void testgetId_returnsId() {
        when(option.getId()).thenReturn(1);

        assertEquals(1, option.getId());
    }

    private void verifyProcessedArgument(List<String> args, String optionName, String expectedValue) {
        assertEquals(optionName, commandLine.getOptionNames().get(args.size() - 2));
        assertEquals(expectedValue, commandLine.getOptionValue(optionName));
        assertEquals(args.size() - 2, args.lastIndexOf(optionName));
    }
}