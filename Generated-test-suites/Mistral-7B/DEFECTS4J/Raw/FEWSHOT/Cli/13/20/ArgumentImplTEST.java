package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.MockValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

    private ArgumentImpl arg;
    private Option option;
    private WriteableCommandLine commandLine;
    private List<String> args;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        resourceHelper = mock(ResourceHelper.class);
        option = mock(Option.class);
        commandLine = mock(WriteableCommandLine.class);
        args = new ArrayList<>();
        arg = new ArgumentImpl("argName", "argDescription", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), "--defaultValue",
                Arrays.asList("defaultValue1", "defaultValue2"), 0);

        when(option.getName()).thenReturn(arg.getName());
        when(resourceHelper.getMessage(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE)).thenReturn("Unexpected value");
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("argName", arg.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        args.add("--defaultValue1");
        args.add("argValue1");
        args.add("argValue2");

        when(commandLine.getValues(option)).thenReturn(args);

        arg.processValues(commandLine, args.listIterator(), option);

        assertEquals(3, commandLine.getValues(option).size());
        assertEquals("argValue1", commandLine.getValues(option).get(0));
        assertEquals("argValue2", commandLine.getValues(option).get(1));
        assertNull(commandLine.getValues(option).get(2));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooManyValues() throws OptionException {
        args.add("--defaultValue1");
        args.add("argValue1");
        args.add("argValue2");
        args.add("argValue3");

        when(commandLine.getValues(option)).thenReturn(args);

        arg.processValues(commandLine, args.listIterator(), option);
    }

    @Test
    public void testProcessValuesWithConsumeRemainingOption() throws OptionException {
        args.add("--argName");
        args.add("--consumeRemaining");
        args.add("argValue1");
        args.add("argValue2");

        when(commandLine.getValues(option)).thenReturn(args);

        arg.processValues(commandLine, args.listIterator(), option);

        assertEquals(3, commandLine.getValues(option).size());
        assertEquals("argValue1", commandLine.getValues(option).get(0));
        assertEquals("argValue2", commandLine.getValues(option).get(1));
        assertSame(args.listIterator(), args.listIterator(args.size()));
    }

    @Test
    public void testCanProcess() {
        assertEquals("argValue1", arg.canProcess(commandLine, "argValue1"));
    }

    @Test
    public void testGetPrefixes() {
        Set<DisplaySetting> helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        assertEquals(Collections.EMPTY_SET, arg.getPrefixes());
    }

    @Test
    public void testProcess() throws OptionException {
        args.add("--argName1");
        args.add("argValue1");
        args.add("--argName2");
        args.add("argValue2");

        when(commandLine.getValues(option)).thenReturn(args);

        arg.process(commandLine, args.listIterator());

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("argValue1", commandLine.getValues(option).get(0));
        assertEquals("argValue2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, arg.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, arg.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, arg.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--defaultValue", arg.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> expectedDefaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        assertEquals(expectedDefaultValues, arg.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Validator validator = new MockValidator();
        ArgumentImpl argWithValidator = new ArgumentImpl("argName", "argDescription", 1, 1,
                ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, validator, "--defaultValue",
                Arrays.asList("defaultValue1", "defaultValue2"), 0);

        assertEquals(validator, argWithValidator.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() throws OptionException {
        args.add("--argName1");
        args.add("argValue1");
        args.add("--argName2");
        args.add("argValue2");
        args.add("invalidValue");

        when(commandLine.getValues(option)).thenReturn(args);

        arg.validate(commandLine, option);
    }

    @Test
    public void testValidateWithValidValues() throws OptionException {
        args.add("--argName1");
        args.add("argValue1");
        args.add("--argName2");
        args.add("argValue2");

        when(commandLine.getValues(option)).thenReturn(args);

        arg.validate(commandLine, option);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        HelpLine helpLine = arg.appendUsage(buffer, null, null);

        assertEquals("argName [argName] ", buffer.toString());
        assertEquals(helpLine, arg.helpLines(1, null, null).get(0));
    }

    @Test
    public void testGetDescription() {
        assertEquals("argDescription", arg.getDescription());
    }

    @Test
    public void testHelpLines() {
        StringBuffer buffer = new StringBuffer();
        HelpLine helpLine = arg.appendUsage(buffer, null, null);

        List<HelpLine> helpLines = arg.helpLines(2, null, null);

        assertEquals(1, helpLines.size());
        assertEquals(helpLine, helpLines.get(0));
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, arg.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, arg.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        assertEquals("argValue1", arg.stripBoundaryQuotes("argValue1"));
        assertEquals("argValue2", arg.stripBoundaryQuotes("argValue2"));
        assertEquals("argValue3", arg.stripBoundaryQuotes("\"argValue3\""));
        assertEquals("argValue4", arg.stripBoundaryQuotes("argValue4"));
    }

    @Test
    public void testIsRequired() {
        assertTrue(arg.isRequired());
    }

    @Test
    public void testDefaults() {
        commandLine.setDefaultValues(option, null);
        arg.defaults(commandLine);

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("defaultValue1", commandLine.getValues(option).get(0));
        assertEquals("defaultValue2", commandLine.getValues(option).get(1));
    }
}