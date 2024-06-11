package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.NoOpValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    private Argument argument;
    private ResourceHelper resources;
    private Validator validator;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        resources = ResourceHelper.getResourceHelper();
        validator = new NoOpValidator();
        commandLine = new CommandLine();

        argument = new ArgumentImpl("optionName", "description", 1, 1, ' ', ' ', validator, "--values", Collections.singletonList("defaultValue"), 0);
    }

    @Test
    public void getName() {
        assertEquals("optionName", argument.getName());
    }

    @Test
    public void getDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void getMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void getMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void processValues() {
        List values = Arrays.asList("value1", "value2");
        commandLine.parseArgument(argument.getName(), values);

        argument.processValues(commandLine, values.listIterator(), argument);

        CommandLine parsedCommandLine = commandLine.parse();
        List parsedValues = parsedCommandLine.getValues(argument);

        assertEquals(2, parsedValues.size());
        assertEquals("value1", parsedValues.get(0));
        assertEquals("value2", parsedValues.get(1));
    }

    @Test
    public void canProcess() {
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void getPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void process() {
        List values = Arrays.asList("value1", "value2");
        commandLine.parseArgument(argument.getName(), values);

        argument.process(commandLine, values.listIterator());

        CommandLine parsedCommandLine = commandLine.parse();
        List parsedValues = parsedCommandLine.getValues(argument);

        assertEquals(2, parsedValues.size());
        assertEquals("value1", parsedValues.get(0));
        assertEquals("value2", parsedValues.get(1));
    }

    @Test
    public void getInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void getSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void getTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void getConsumeRemaining() {
        assertEquals("--values", argument.getConsumeRemaining());
    }

    @Test
    public void getDefaultValues() {
        assertEquals(Collections.singletonList("defaultValue"), argument.getDefaultValues());
    }

    @Test
    public void getValidator() {
        assertSame(validator, argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void validate() throws OptionException {
        List values = Arrays.asList("value1", "value2", "value3");
        commandLine.parseArgument(argument.getName(), values);

        argument.validate(commandLine);

        // validation should pass with 3 values for this argument
    }

    @Test(expected = OptionException.class)
    public void validateWithInvalidValue() throws OptionException {
        List values = Arrays.asList("value1", "value2", "invalidValue");
        commandLine.parseArgument(argument.getName(), values);

        argument.validate(commandLine);

        // validation should fail with an extra value for this argument
    }

    @Test
    public void appendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NAME);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("optionName [values]", buffer.toString());
    }

    @Test
    public void getDefaultValuesWhenNoDefaults() {
        argument = new ArgumentImpl("optionName", "description", 1, 1, ' ', ' ', validator, "--values", null, 0);

        assertEquals(Collections.EMPTY_LIST, argument.getDefaultValues());
    }

    @Test
    public void getDefaultValuesWhenHasDefaults() {
        argument = new ArgumentImpl("optionName", "description", 1, 1, ' ', ' ', validator, "--values", Arrays.asList("defaultValue1", "defaultValue2"), 0);

        assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), argument.getDefaultValues());
    }
}