package org.apache.commons.cli2.option;

import static org.apache.commons.cli2.resource.ResourceConstants.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.ValidateOptions;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

    private static final ResourceHelper resources = ResourceHelper.getResourceHelper();
    private WriteableCommandLine commandLine;
    private Argument argument;
    private Option option;
    private List<String> args;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine();
        option = new OptionBuilder()
                .withName("arg")
                .withDescription(ARGUMENT_DESCRIPTION)
                .withLongOpt("longarg")
                .create();
        argument = new ArgumentImpl("arg", ARGUMENT_DESCRIPTION, 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        args = new ArrayList<>();
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        String[] values = {"value1", "value2"};
        for (String value : values) {
            args.add(value);
        }
        argument.processValues(commandLine, args.listIterator(), option);

        CommandLine parsedCommandLine = commandLine.parse();
        List<String> valuesInCommandLine = parsedCommandLine.getValues(option);
        assertArrayEquals(values, valuesInCommandLine.toArray(new String[0]));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test(expected = OptionException.class)
    public void testCannotProcess() {
        argument.canProcess(commandLine, "option");
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.emptyList(), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.setValues(option, values);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() throws OptionException {
        List<String> values = Arrays.asList("value1", "value2", "invalidValue");
        commandLine.setValues(option, values);
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.emptySet(), null);
        String usage = buffer.toString();
        String expectedUsage = "  [--arg <value>]";
        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testGetDescription() {
        assertEquals(ARGUMENT_DESCRIPTION, argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> helpLines = argument.helpLines(1, Collections.emptySet(), null);
        HelpLine helpLine = helpLines.get(0);
        String usage = formatter.printHelp("", option, helpLine.getHelp());
        String expectedUsage = "  [--arg <value>]";
        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"value\"";
        String expectedToken = "value";
        assertEquals(expectedToken, argument.stripBoundaryQuotes(token));
    }

    @Test
    public void testDefaults() {
        argument.defaults(commandLine);
        List<String> defaultValues = commandLine.getDefaultValues(option);
        assertNull(defaultValues);
    }

    @Test
    public void testDefaultValuesWhenSpecified() {
        argument = new ArgumentImpl("arg", ARGUMENT_DESCRIPTION, 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("value1", "value2"), 0);
        argument.defaults(commandLine);
        List<String> defaultValues = commandLine.getDefaultValues(option);
        assertArrayEquals(new String[]{"value1", "value2"}, defaultValues.toArray(new String[0]));
    }

    @Test
    public void testValidateWithValidator() throws OptionException, InvalidArgumentException {
        Validator validator = new ValidateOptions();
        argument = new ArgumentImpl("arg", ARGUMENT_DESCRIPTION, 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, validator, DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        List<String> values = Arrays.asList("value1", "value2", "value3");
        commandLine.setValues(option, values);
        argument.validate(commandLine);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testValidateWithValidatorInvalidArgument() throws OptionException, InvalidArgumentException {
        Validator validator = new ValidateOptions();
        argument = new ArgumentImpl("arg", ARGUMENT_DESCRIPTION, 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, validator, DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        List<String> values = Arrays.asList("value1", "value2", "invalidValue");
        commandLine.setValues(option, values);
        argument.validate(commandLine);
    }
}