package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.cli2.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ArgumentImpl.
 */
public class ArgumentImplTest {

    private ArgumentImpl argument;
    private ArgumentImpl argumentWithMaxValue;
    private ArgumentImpl argumentWithMinValue;
    private ArgumentImpl argumentWithConsumeRemaining;
    private ArgumentImpl argumentWithInitialSeparator;
    private ArgumentImpl argumentWithSubsequentSeparator;
    private ArgumentImpl argumentWithDefaultValues;
    private ArgumentImpl argumentWithValidator;
    private ArgumentImpl argumentWithInfiniteValues;
    private List<String> arguments;
    private WriteableCommandLine commandLine;
    private Option option;
    private Validator validator;
    private ResourceHelper resources;

    @Before
    public void setup() {
        resources = ResourceHelper.getResourceHelper();
        argument = new ArgumentImpl("testArg", "Test argument", 0, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, null, 0);
        argumentWithMaxValue = new ArgumentImpl("testArgMax", "Test argument with max value", 0, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("value1"), 0);
        argumentWithMinValue = new ArgumentImpl("testArgMin", "Test argument with min value", 1, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argumentWithConsumeRemaining = new ArgumentImpl("testArgConsume", "Test argument with consume remaining", 0, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", null, 0);
        argumentWithInitialSeparator = new ArgumentImpl("testArgInitialSeparator", "Test argument with initial separator", 0, 1,
                '>', DEFAULT_SUBSEQUENT_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argumentWithSubsequentSeparator = new ArgumentImpl("testArgSubsequentSeparator", "Test argument with subsequent separator", 0, 1,
                DEFAULT_INITIAL_SEPARATOR, '|', null, null, null, 0);
        argumentWithDefaultValues = new ArgumentImpl("testArgDefaultValues", "Test argument with default values", 0, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("value1", "value2"), 0);
        argumentWithValidator = new ArgumentImpl("testArgValidator", "Test argument with validator", 0, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new Validator() {
                    @Override
                    public void validate(List values) throws InvalidArgumentException {
                        assertEquals("expected", "value", values.get(0));
                    }
                }, null, null, 0);
        argumentWithInfiniteValues = new ArgumentImpl("testArgInfiniteValues", "Test argument with infinite values", 0, 0,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        commandLine = new CommandLine();
        option = new Option("testOption", "Test option", false, null, 0);
        validator = new ValidatorFactory().getValidator();
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testArg", argument.getPreferredName());
    }

    @Test
    public void testGetName() {
        assertEquals("arg", argument.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test argument", argument.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(NUL, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(NUL, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetSubsequentSplit() {
        assertFalse(argument.getSubsequentSplit());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertNull(argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals(0, commandLine.getValues(option).size());

        arguments.add("value1");
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals("value1", commandLine.getValues(option).get(0));

        arguments.add("value2");
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));

        arguments.add("--");
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals(2, commandLine.getValues(option).size());

        arguments.add("value3");
        assertThrows(OptionException.class, () -> argument.processValues(commandLine, arguments.listIterator(), option));
        assertEquals(2, commandLine.getValues(option).size());

        argument = argumentWithMaxValue;
        arguments.clear();
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals(1, commandLine.getValues(option).size());

        arguments.add("value1");
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals(1, commandLine.getValues(option).size());

        arguments.add("value2");
        assertThrows(OptionException.class, () -> argument.processValues(commandLine, arguments.listIterator(), option));
        assertEquals(1, commandLine.getValues(option).size());

        argument = argumentWithMinValue;
        arguments.clear();
        assertThrows(OptionException.class, () -> argument.processValues(commandLine, arguments.listIterator(), option));
        assertEquals(0, commandLine.getValues(option).size());

        arguments.add("value1");
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals(1, commandLine.getValues(option).size());
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "value1"));

        argument = argumentWithConsumeRemaining;
        assertTrue(argument.canProcess(commandLine, "--"));

        argument = argumentWithInitialSeparator;
        assertTrue(argument.canProcess(commandLine, "value1"));

        argument = argumentWithSubsequentSeparator;
        assertTrue(argument.canProcess(commandLine, "value1 value2"));

        argument = argumentWithDefaultValues;
        assertTrue(argument.canProcess(commandLine, "value1"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = argument.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testProcess() {
        argument.process(commandLine, arguments.listIterator());
        assertEquals(0, commandLine.getValues(option).size());

        arguments.add("value1");
        argument.process(commandLine, arguments.listIterator());
        assertEquals("value1", commandLine.getValues(option).get(0));

        arguments.add("value2");
        argument.process(commandLine, arguments.listIterator());
        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));

        arguments.add("--");
        argument.process(commandLine, arguments.listIterator());
        assertEquals(2, commandLine.getValues(option).size());

        argument = argumentWithMaxValue;
        arguments.clear();
        argument.process(commandLine, arguments.listIterator());
        assertEquals(0, commandLine.getValues(option).size());

        arguments.add("value1");
        argument.process(commandLine, arguments.listIterator());
        assertEquals(1, commandLine.getValues(option).size());

        arguments.add("value2");
        argument.process(commandLine, arguments.listIterator());
        assertEquals(1, commandLine.getValues(option).size());

        argument = argumentWithMinValue;
        arguments.clear();
        argument.process(commandLine, arguments.listIterator());
        assertEquals(0, commandLine.getValues(option).size());

        arguments.add("value1");
        argument.process(commandLine, arguments.listIterator());
        assertEquals(1, commandLine.getValues(option).size());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(NUL, argument.getInitialSeparator());
        assertEquals(NUL, argumentWithInitialSeparator.getInitialSeparator());
        assertEquals('>', argumentWithInitialSeparator.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(NUL, argument.getSubsequentSeparator());
        assertEquals(NUL, argumentWithSubsequentSeparator.getSubsequentSeparator());
        assertEquals('|', argumentWithSubsequentSeparator.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = argument.getTriggers();
        assertTrue(triggers.isEmpty());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertNull(argument.getConsumeRemaining());
        assertEquals("--", argumentWithConsumeRemaining.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
        assertNull(argumentWithDefaultValues.getDefaultValues());
        assertEquals(Arrays.asList("value1", "value2"), argumentWithDefaultValues.getDefaultValues());
    }

    @Test
    public void testValidate() {
        argument.validate(commandLine);

        argumentWithValidator.validate(commandLine);
        assertThrows(InvalidArgumentException.class, () -> {
            argumentWithValidator.validate(commandLine);
            commandLine.getValues(option).set(0, "invalidValue");
            argumentWithValidator.validate(commandLine);
        });

        argumentWithInfiniteValues.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        List<HelpLine> helpLines = argument.helpLines(1, null, null);
        assertEquals(1, helpLines.size());

        String usage = helpLines.get(0).getShortOption() + ":" + argument.getDescription();
        assertEquals("testArg:Test argument", usage);

        usage = argumentWithDefaultValues.helpLines(1, null, null).get(0).getShortOption() + ": "
                + argumentWithDefaultValues.getDescription() + " [value1, value2]";
        assertEquals("testArgDefaultValues:Test argument with default values [value1, value2]", usage);

        usage = argumentWithConsumeRemaining.helpLines(1, null, null).get(0).getShortOption() + ": "
                + argumentWithConsumeRemaining.getDescription() + " [consume]...";
        assertEquals("testArgConsume:Test argument with consume remaining [consume]...", usage);

        usage = argumentWithInitialSeparator.helpLines(1, null, null).get(0).getShortOption() + ": "
                + argumentWithInitialSeparator.getDescription() + " <value>";
        assertEquals("testArgInitialSeparator:Test argument with initial separator <value>", usage);

        usage = argumentWithSubsequentSeparator.helpLines(1, null, null).get(0).getShortOption() + ": "
                + argumentWithSubsequentSeparator.getDescription() + " <value1> <value2>...";
        assertEquals("testArgSubsequentSeparator:Test argument with subsequent separator <value1> <value2>...", usage);

        usage = argumentWithMaxValue.helpLines(1, null, null).get(0).getShortOption() + ": "
                + argumentWithMaxValue.getDescription() + " {value}...";
        assertEquals("testArgMax:Test argument with max value {value}...", usage);

        usage = argumentWithMinValue.helpLines(1, null, null).get(0).getShortOption() + ": "
                + argumentWithMinValue.getDescription() + " <value>";
        assertEquals("testArgMin:Test argument with min value <value>", usage);
    }

    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());
        assertTrue(argumentWithMinValue.isRequired());
    }

    @Test
    public void testDefaults() {
        argument.defaults(commandLine);
        assertNull(commandLine.getDefaultValues(option));

        argumentWithDefaultValues.defaults(commandLine);
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getDefaultValues(option));
    }
}