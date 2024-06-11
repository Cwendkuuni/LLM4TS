package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.MockValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for class ArgumentImpl.
 */
public class ArgumentImplTest {
    private ResourceHelper resources;
    private Argument argument;

    @Before
    public void setUp() {
        resources = ResourceHelper.getResourceHelper();
        argument = new ArgumentImpl("myArg", "Description for myArg", 1, 1, 'a', 'b', null, "--", null, 0);
    }

    @Test
    public void testArgumentImpl() {
        assertEquals("myArg", argument.getPreferredName());
        assertEquals("Description for myArg", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals('a', argument.getInitialSeparator());
        assertEquals('b', argument.getSubsequentSeparator());
        assertFalse(argument.getSubsequentSplit());
        assertNull(argument.getValidator());
        assertSame(ResourceConstants.ARGUMENT_CONSUME_REMAINING, argument.getConsumeRemaining());
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new CommandLine();
        List<Object> arguments = Arrays.asList(new Object[]{"--myArg", "value1", "--myArg", "value2",
                "--myArg", "value3", "--consume"});
        ListIterator<Object> argumentsIterator = Arrays.asList(arguments.toArray()).listIterator();

        try {
            argument.processValues(commandLine, argumentsIterator, argument);
            CommandLine parsedCommandLine = commandLine;
            assertEquals(3, parsedCommandLine.getValues(argument).size());
            assertEquals("value1", parsedCommandLine.getValues(argument).get(0));
            assertEquals("value2", parsedCommandLine.getValues(argument).get(1));
            assertEquals("value3", parsedCommandLine.getValues(argument).get(2));
        } catch (OptionException e) {
            throw new AssertionError(e);
        }
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesInvalidNumberOfValues() {
        WriteableCommandLine commandLine = new CommandLine();
        List<Object> arguments = Arrays.asList(new Object[]{"--myArg", "value1", "--myArg", "value2"});
        ListIterator<Object> argumentsIterator = Arrays.asList(arguments.toArray()).listIterator();

        try {
            argument.processValues(commandLine, argumentsIterator, argument);
            fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(new CommandLine(), "value"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        List<Object> arguments = Arrays.asList(new Object[]{"--myArg", "value1", "--myArg", "value2"});
        ListIterator<Object> argumentsIterator = Arrays.asList(arguments.toArray()).listIterator();
        Option option = new OptionImpl(1, false, null, null, null, null, null, null, null, 0);

        argument.process(commandLine, argumentsIterator);
        CommandLine parsedCommandLine = commandLine;

        assertEquals(2, parsedCommandLine.getValues(option).size());
        assertEquals("value1", parsedCommandLine.getValues(option).get(0));
        assertEquals("value2", parsedCommandLine.getValues(option).get(1));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidOption() {
        WriteableCommandLine commandLine = new CommandLine();
        List<Object> arguments = Arrays.asList(new Object[]{"--myArg", "--invalid"});
        ListIterator<Object> argumentsIterator = Arrays.asList(arguments.toArray()).listIterator();

        try {
            argument.process(commandLine, argumentsIterator);
            fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('a', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('b', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidValue() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        List<Object> arguments = Arrays.asList(new Object[]{"--myArg", "invalidValue"});
        ListIterator<Object> argumentsIterator = Arrays.asList(arguments.toArray()).listIterator();

        MockValidator mockValidator = new MockValidator();
        argument.setValidator(mockValidator);

        try {
            argument.validate(commandLine);
            fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // expected
        }

        assertTrue(mockValidator.isValidCalled());
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidValues() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        List<Object> arguments = Arrays.asList(new Object[]{"--myArg", "value1", "invalidValue"});
        ListIterator<Object> argumentsIterator = Arrays.asList(arguments.toArray()).listIterator();

        MockValidator mockValidator = new MockValidator();
        argument.setValidator(mockValidator);

        try {
            argument.validate(commandLine, argument);
            fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // expected
        }

        assertTrue(mockValidator.isValidCalled());
    }

    @Test
    public void testValidateValidValues() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        List<Object> arguments = Arrays.asList(new Object[]{"--myArg", "value1", "value2"});
        ListIterator<Object> argumentsIterator = Arrays.asList(arguments.toArray()).listIterator();

        MockValidator mockValidator = new MockValidator();
        argument.setValidator(mockValidator);

        try {
            argument.validate(commandLine, argument);
            assertTrue(mockValidator.isValidCalled());
        } catch (OptionException e) {
            fail("Unexpected OptionException thrown");
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT), null);

        assertEquals("--myArg [value] ..." + System.lineSeparator(), buffer.toString());
    }

    @Test
    public void testAppendUsageWithDescription() {
        StringBuffer buffer = new StringBuffer();
        argument.setDescription("A test argument");
        argument.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT), null);

        assertEquals("--myArg [value]  A test argument" + System.lineSeparator(), buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptional() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT);
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("[--myArg [value]]" + System.lineSeparator(), buffer.toString());
    }

    @Test
    public void testAppendUsageWithNumbering() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("[1]--myArg [value]\n[2]--myArg [value]" + System.lineSeparator(), buffer.toString());
    }

    @Test
    public void testAppendUsageWithBrackets() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("<--myArg [value]> " + System.lineSeparator(), buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptionalityAndBrackets() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT);
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("<--myArg [value]> [optional]" + System.lineSeparator(), buffer.toString());
    }

    @Test
    public void testAppendUsageWithNumberingAndBrackets() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new ArrayList<>();
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("[1]<--myArg [value]>\n[2]<--myArg [value]>" + System.lineSeparator(), buffer.toString());
    }
}

class MockValidator implements Validator {
    private boolean isValidCalled = false;

    @Override
    public void validate(List values) throws InvalidArgumentException {
        isValidCalled = true;
    }

    public boolean isValidCalled() {
        return isValidCalled;
    }
}