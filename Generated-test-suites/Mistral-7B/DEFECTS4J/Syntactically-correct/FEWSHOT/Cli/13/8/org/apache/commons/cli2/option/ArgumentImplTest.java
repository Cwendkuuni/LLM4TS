package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {
    private static final String NAME = "argument";
    private static final String DESCRIPTION = "A test argument";
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 2;
    private static final char INITIAL_SEPARATOR = 'a';
    private static final char SUBSEQUENT_SEPARATOR = 'b';
    private static final Validator VALIDATOR = new Validator() {
        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            // no-op validator
        }
    };
    private static final String CONSUME_REMAINING = "--consume";
    private static final List DEFAULT_VALUES = new ArrayList<>(Arrays.asList("default1", "default2"));
    private static final ResourceHelper RESOURCE_HELPER = ResourceHelper.getResourceHelper();
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        argument = new ArgumentImpl(NAME, DESCRIPTION, MINIMUM, MAXIMUM, INITIAL_SEPARATOR, SUBSEQUENT_SEPARATOR, VALIDATOR, CONSUME_REMAINING, DEFAULT_VALUES, 0);
        commandLine = new CommandLineImpl();
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, argument.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(DESCRIPTION, argument.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(MINIMUM, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(MAXIMUM, argument.getMaximum());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetSubsequentSplit() {
        assertTrue(argument.isSubsequentSplitEnabled());
    }

    @Test
    public void testGetValidator() {
        assertSame(VALIDATOR, argument.getValidator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List expectedDefaultValues = new ArrayList<>(DEFAULT_VALUES);
        List actualDefaultValues = argument.getDefaultValues();

        assertSame(expectedDefaultValues, actualDefaultValues);
    }

    @Test
    public void testProcessValues() throws OptionException {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3", "--consume", "--foo");
        argument.processValues(commandLine, arguments.listIterator(), this.argument);

        List<String> expectedValues = Arrays.asList("arg1", "arg2");
        List<String> actualValues = commandLine.getValues(argument);

        assertSame(expectedValues, actualValues);
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "arg1"));
    }

    @Test
    public void testGetPrefixes() {
        Set expectedPrefixes = Collections.emptySet();
        Set actualPrefixes = argument.getPrefixes();

        assertSame(expectedPrefixes, actualPrefixes);
    }

    @Test
    public void testProcess() throws OptionException {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3", "--consume", "--foo");

        argument.process(commandLine, arguments.listIterator());

        List<String> expectedValues = Arrays.asList("arg1", "arg2");
        List<String> actualValues = commandLine.getValues(argument);

        assertSame(expectedValues, actualValues);
    }

    @Test
    public void testValidate() throws OptionException {
        List<String> argumentValues = Arrays.asList("value1", "value2", "value3");
        commandLine.setValues(argument, argumentValues);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewValues() throws OptionException {
        List<String> argumentValues = Collections.emptyList();
        commandLine.setValues(argument, argumentValues);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyValues() throws OptionException {
        List<String> argumentValues = Arrays.asList("value1", "value2", "value3", "value4");
        commandLine.setValues(argument, argumentValues);
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuilder expectedUsage = new StringBuilder();
        expectedUsage.append("--");
        expectedUsage.append(NAME);
        expectedUsage.append(" [VALUE]...");

        StringBuilder actualUsage = new StringBuilder();
        argument.appendUsage(actualUsage, null, null);

        assertEquals(expectedUsage.toString(), actualUsage.toString());
    }

    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());
    }

    @Test
    public void testDefaults() throws OptionException {
        argument.defaults(commandLine);

        List<String> expectedDefaultValues = Arrays.asList("default1", "default2");
        List<String> actualDefaultValues = commandLine.getValues(argument);

        assertSame(expectedDefaultValues, actualDefaultValues);
    }
}

class CommandLineImpl extends CommandLine {
    @Override
    public void addValue(Option option, String value) {
        super.addValue(option, value);
    }

    @Override
    public void setValues(Option option, List values) {
        super.setValues(option, values);
    }

    @Override
    public List getValues(Option option) {
        return super.getValues(option);
    }

    @Override
    public boolean looksLikeOption(String name) {
        return false;
    }
}

class HelpLineImpl extends HelpLine {
    public HelpLineImpl(Option option, int depth) {
        super(option, depth);
    }
}