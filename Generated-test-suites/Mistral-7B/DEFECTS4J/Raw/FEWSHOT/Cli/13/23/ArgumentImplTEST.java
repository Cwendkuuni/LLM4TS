package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

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
import org.apache.commons.cli2.validation.NoArgValidator;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);
        assertEquals("test", option.getPreferredName());
        assertEquals("Test arg", option.getDescription());
        assertEquals(1, option.getMinimum());
        assertEquals(1, option.getMaximum());
        assertFalse(option.canProcess(new WriteableCommandLine(), ""));
        assertNull(option.getValue());
        assertNull(option.getValidator());
        assertNull(option.getDefaultValues());
    }

    @Test
    public void testArgumentImplWithValidator() {
        final String[] validValues = {"valid1", "valid2"};
        final NoArgValidator validator = new NoArgValidator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
                assertTrue(Arrays.asList(values.toArray()).containsAll(Arrays.asList(validValues)));
            }
        };
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, validator, "",
                                                Collections.unmodifiableList(Arrays.asList(validValues)), 0);
        assertEquals("test", option.getPreferredName());
        assertEquals("Test arg", option.getDescription());
        assertEquals(1, option.getMinimum());
        assertEquals(1, option.getMaximum());
        assertFalse(option.canProcess(new WriteableCommandLine(), ""));
        assertNull(option.getValue());
        assertTrue(option.getValidator() instanceof NoArgValidator);
        assertSame(validValues, option.getDefaultValues().toArray(new String[0]));
    }

    @Test
    public void testProcessValuesWithOneValue() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);

        final CommandLine commandLine = new CommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("--test foo");

        option.process(commandLine, arguments.listIterator());

        assertEquals(1, commandLine.getValues(option).size());
        assertEquals("foo", commandLine.getValues(option).get(0));
    }

    @Test
    public void testProcessValuesWithMultipleValues() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);

        final CommandLine commandLine = new CommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("--test foo bar");

        option.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("foo", commandLine.getValues(option).get(0));
        assertEquals("bar", commandLine.getValues(option).get(1));
    }

    @Test
    public void testProcessValuesWithConsumingOption() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);

        final CommandLine commandLine = new CommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("--test");
        arguments.add("--flag1");
        arguments.add("--flag2");

        option.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("--flag1", commandLine.getValues(option).get(0));
        assertEquals("--flag2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testProcessValuesWithMultipleConsumingOptionsAndValues() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);

        final CommandLine commandLine = new CommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("--test");
        arguments.add("--flag1");
        arguments.add("value1");
        arguments.add("--flag2");
        arguments.add("value2");

        option.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testProcessValuesWithSubsequentSeparator() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                '=', null, "", null, 0);

        final CommandLine commandLine = new CommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("--test=foo=bar");

        option.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("foo", commandLine.getValues(option).get(0));
        assertEquals("bar", commandLine.getValues(option).get(1));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithMoreValuesThanMaximumAllowed() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);

        final CommandLine commandLine = new CommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("--test foo bar baz");

        option.process(commandLine, arguments.listIterator());

        // The test should throw an OptionException as the number of arguments
        // passed is more than the maximum allowed.
    }

    @Test
    public void testValidateWithValidValues() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);
        final CommandLine commandLine = new CommandLine();
        commandLine.setValues(option, new ArrayList<>(Arrays.asList("foo", "bar")));
        option.validate(commandLine, option);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);
        final CommandLine commandLine = new CommandLine();
        commandLine.setValues(option, new ArrayList<>(Arrays.asList("foo", "bar", "baz")));
        option.validate(commandLine, option);
        // The test should throw an OptionException as the number of arguments
        // passed is more than the maximum allowed.
    }

    @Test
    public void testHelpLinesWithNoValues() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", null, 0);
        final List<HelpLine> helpLines = option.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        final HelpLine helpLine = helpLines.get(0);
        assertEquals("[--test]", helpLine.getUsageLine());
        assertEquals("Test arg\n", helpLine.getLongDescription());
    }

    @Test
    public void testHelpLinesWithValues() {
        final Argument option = new ArgumentImpl("test", "Test arg", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                                                DEFAULT_SUBSEQUENT_SEPARATOR, null, "", new ArrayList<>(
                                                        Arrays.asList("foo", "bar")), 0);
        final List<HelpLine> helpLines = option.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        final HelpLine helpLine = helpLines.get(0);
        assertEquals("[--test=FOO BAR]", helpLine.getUsageLine());
        assertEquals("Test arg\n", helpLine.getLongDescription());
    }
}