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
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

/**
 * Tests for ArgumentImpl class
 */
public class ArgumentImplTest {
    @Test
    public void testArgumentImplWithNoArguments() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 0, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.emptyList(), 0);

        assertEquals("testarg", argument.getPreferredName());
        assertEquals("Test Argument", argument.getDescription());
        assertEquals(0, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertFalse(argument.isRequired());
        assertNull(argument.getInitialSeparator());
        assertNull(argument.getSubsequentSeparator());
        assertFalse(argument.canProcess(null, ""));
        assertFalse(argument.isConsumesRemainingValues());
        assertSame(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testArgumentImplWithOneArgument() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.singletonList("test"), 0);

        assertEquals("testarg", argument.getPreferredName());
        assertEquals("Test Argument", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertTrue(argument.isRequired());
        assertNull(argument.getInitialSeparator());
        assertNull(argument.getSubsequentSeparator());
        assertFalse(argument.canProcess(null, ""));
        assertFalse(argument.isConsumesRemainingValues());
        assertSame(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testArgumentImplWithMinimumMoreThanMaximum() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 3, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.singletonList("test"), 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> argument);
        assertEquals(ResourceConstants.ARGUMENT_MIN_EXCEEDS_MAX, exception.getMessage());
    }

    @Test
    public void testArgumentImplWithDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 0, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("test1", "test2"), 0);

        assertEquals("testarg", argument.getPreferredName());
        assertEquals("Test Argument", argument.getDescription());
        assertEquals(0, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertFalse(argument.isRequired());
        assertNull(argument.getInitialSeparator());
        assertNull(argument.getSubsequentSeparator());
        assertFalse(argument.isConsumesRemainingValues());
        assertSame(Collections.EMPTY_SET, argument.getPrefixes());
        assertEquals(2, argument.getDefaultValues().size());
    }

    @Test
    public void testArgumentImplWithValidator() {
        List<String> validValues = Arrays.asList("valid1", "valid2");
        List<String> invalidValues = Arrays.asList("invalid1", "invalid2");

        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 0, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), null, Collections.emptyList(), 0);

        assertEquals("testarg", argument.getPreferredName());
        assertEquals("Test Argument", argument.getDescription());
        assertEquals(0, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertFalse(argument.isRequired());
        assertNull(argument.getInitialSeparator());
        assertNull(argument.getSubsequentSeparator());
        assertFalse(argument.isConsumesRemainingValues());
        assertSame(Collections.EMPTY_SET, argument.getPrefixes());
        assertSame(argument.getValidator(), new MyValidator());

        assertThrows(OptionException.class,
                () -> argument.processValues(new CommandLine(), new ListIterator<String>() {
                }, argument));

        CommandLine commandLine = new CommandLine();
        commandLine.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .validators(new MyValidator()).build(), new String[]{"--testarg", "valid1"}));

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("valid1", commandLine.getValues(argument).get(0));

        assertThrows(OptionException.class,
                () -> argument.processValues(new CommandLine(), new ListIterator<String>() {
                }, argument));

        commandLine = new CommandLine();
        commandLine.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .validators(new MyValidator()).build(), new String[]{"--testarg", "valid1", "invalid1"}));

        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("valid1", commandLine.getValues(argument).get(0));
        assertEquals("invalid1", commandLine.getValues(argument).get(1));

        commandLine = new CommandLine();
        commandLine.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .validators(new MyValidator()).build(), new String[]{"--testarg", "valid1", "valid2", "invalid1"}));

        assertEquals(3, commandLine.getValues(argument).size());
        assertEquals("valid1", commandLine.getValues(argument).get(0));
        assertEquals("valid2", commandLine.getValues(argument).get(1));
        assertEquals("invalid1", commandLine.getValues(argument).get(2));

        commandLine = new CommandLine();
        commandLine.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .validators(new MyValidator()).build(), new String[]{"--testarg", "valid1", "valid2"}));

        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("valid1", commandLine.getValues(argument).get(0));
        assertEquals("valid2", commandLine.getValues(argument).get(1));
    }

    @Test
    public void testArgumentImplWithConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 0, Integer.MAX_VALUE,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, "--",
                Collections.emptyList(), 0);

        assertEquals("testarg", argument.getPreferredName());
        assertEquals("Test Argument", argument.getDescription());
        assertEquals(0, argument.getMinimum());
        assertEquals(Integer.MAX_VALUE, argument.getMaximum());
        assertFalse(argument.isRequired());
        assertNull(argument.getInitialSeparator());
        assertSame(DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
        assertTrue(argument.isConsumesRemainingValues());
        assertSame(Collections.singleton("--"), argument.getConsumeRemaining());
        assertSame(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testArgumentImplWithSeparator() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 1, 1, ';', DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.emptyList(), 0);

        assertEquals("testarg", argument.getPreferredName());
        assertEquals("Test Argument", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertFalse(argument.isRequired());
        assertEquals(';', argument.getInitialSeparator());
        assertNull(argument.getSubsequentSeparator());
        assertFalse(argument.isConsumesRemainingValues());
        assertSame(Collections.EMPTY_SET, argument.getPrefixes());

        ArgumentImpl argument2 = new ArgumentImpl("testarg2", "Test Argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, ';',
                null, null, Collections.emptyList(), 0);

        assertEquals("testarg2", argument2.getPreferredName());
        assertEquals("Test Argument", argument2.getDescription());
        assertEquals(1, argument2.getMinimum());
        assertEquals(1, argument2.getMaximum());
        assertFalse(argument2.isRequired());
        assertNull(argument2.getInitialSeparator());
        assertEquals(';', argument2.getSubsequentSeparator());
        assertFalse(argument2.isConsumesRemainingValues());
        assertSame(Collections.EMPTY_SET, argument2.getPrefixes());

        ArgumentImpl argument3 = new ArgumentImpl("testarg3", "Test Argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.singletonList("test3"), 0);

        assertEquals("testarg3", argument3.getPreferredName());
        assertEquals("Test Argument", argument3.getDescription());
        assertEquals(1, argument3.getMinimum());
        assertEquals(1, argument3.getMaximum());
        assertFalse(argument3.isRequired());
        assertNull(argument3.getInitialSeparator());
        assertNull(argument3.getSubsequentSeparator());
        assertFalse(argument3.isConsumesRemainingValues());
        assertSame(Collections.singleton("test3"), argument3.getDefaultValues());
    }

    @Test
    public void testArgumentImplWithInvalidValues() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.emptyList(), 0);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .build(), new String[]{"--testarg", "valid1", "invalid1"}));

        OptionException exception = assertThrows(OptionException.class,
                () -> argument.validate(commandLine, argument));

        assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, exception.getMessage());
        assertEquals("invalid1", exception.getArgument());
    }

    @Test
    public void testArgumentImplWithMinimumValues() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 2, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.emptyList(), 0);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .build(), new String[]{"--testarg", "test1", "test2"}));

        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("test1", commandLine.getValues(argument).get(0));
        assertEquals("test2", commandLine.getValues(argument).get(1));

        CommandLine commandLine2 = new CommandLine();
        commandLine2.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .build(), new String[]{"--testarg", "test1"}));

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> argument.validate(commandLine2, argument));

        assertEquals("Argument testarg does not have the required number of arguments.",
                exception.getMessage());
    }

    @Test
    public void testArgumentImplWithMaximumValues() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.emptyList(), 0);

        CommandLine commandLine = new CommandLine();
        commandLine.parse(new DefaultParser().parse(OptionBuilder.option("testarg", true, "t").isRequired(false)
                .build(), new String[]{"--testarg", "test1", "test2"}));

        OptionException exception = assertThrows(OptionException.class,
                () -> argument.validate(commandLine, argument));

        assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, exception.getMessage());
        assertEquals("test2", exception.getArgument());
    }

    @Test
    public void testArgumentImplWithHelp() {
        ArgumentImpl argument = new ArgumentImpl("testarg", "Test Argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.emptyList(), 0);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", "1.0", true, "Test program",
                new Option[]{}, new String[]{"--testarg", "[value]"},
                new String[]{"Test argument", "Test Argument", "A test argument. [value] is the value for the test argument."});

        String expectedHelp = "Test program:\n\n" +
                "options:\n" +
                "  --testarg <value>\n" +
                "    Test argument. [value] is the value for the test argument.\n";

        assertEquals(expectedHelp, formatter.getHelpText());
    }

    private static class MyValidator implements Validator {
        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            boolean hasValidValue = false;

            for (String value : values) {
                if (isValidValue) {
                    throw new InvalidArgumentException("Invalid value: " + value);
                }
                if (value.equals("valid1")) {
                    hasValidValue = true;
                }
            }
        }
    }
}