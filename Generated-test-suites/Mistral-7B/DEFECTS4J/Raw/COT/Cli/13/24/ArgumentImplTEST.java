package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.*;

/**
 * Test class for ArgumentImpl.
 */
public class ArgumentImplTest {

    private Argument argument;
    private WriteableCommandLine commandLine;
    private List<String> arguments;
    private Option option;

    @Before
    public void setup() {
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, NUL, NUL, null, null, null, 0);
        commandLine = new WriteableCommandLine();
        arguments = new ArrayList<>();
        option = new OptionImpl(1, false);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("testArg", argument.getName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("Test Argument", argument.getDescription());
    }

    @Test
    public void testProcessValues() {
        arguments.add("value1");
        arguments.add("value2");
        argument.process(commandLine, arguments.listIterator(), option);

        List<String> expectedValues = Arrays.asList("value1", "value2");
        Assert.assertEquals(expectedValues, commandLine.getValues(option));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidArgument() {
        arguments.add("value1");
        arguments.add("optionName");
        argument.process(commandLine, arguments.listIterator(), option);
    }

    @Test
    public void testCanProcess() {
        Assert.assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = argument.getPrefixes();
        Assert.assertEquals(Collections.EMPTY_SET, prefixes);
    }

    @Test
    public void testProcessWithConsumeRemaining() {
        arguments.add("--consume-remaining");
        argument.process(commandLine, arguments.listIterator(), option);

        List<String> expectedValues = Arrays.asList("value1", "value2");
        Assert.assertEquals(expectedValues, commandLine.getValues(option));

        Assert.assertEquals(0, arguments.size());
    }

    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals(NUL, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals(NUL, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = argument.getTriggers();
        Assert.assertEquals(Collections.EMPTY_SET, triggers);
    }

    @Test
    public void testGetConsumeRemaining() {
        Assert.assertEquals(null, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        Assert.assertNull(argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Assert.assertNull(argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidArgument() {
        argument.validate(commandLine);

        arguments.add("invalidValue");
        commandLine.setValues(option, Arrays.asList("validValue"));
        argument.validate(commandLine, option);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        Assert.assertEquals(" testArg  <value>", buffer.toString());
    }

    @Test
    public void testGetMinimum() {
        Assert.assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        Assert.assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"value\"";
        Assert.assertEquals("value", argument.stripBoundaryQuotes(token));
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        // No default values
        argument.defaults(commandLine);
        Assert.assertNull(commandLine.getValues(option));

        // Default values
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, NUL, NUL, null, null, Arrays.asList("default1"), 0);
        commandLine.setValues(option, Collections.emptyList());
        argument.defaults(commandLine);
        Assert.assertEquals(Arrays.asList("default1"), commandLine.getValues(option));
    }
}