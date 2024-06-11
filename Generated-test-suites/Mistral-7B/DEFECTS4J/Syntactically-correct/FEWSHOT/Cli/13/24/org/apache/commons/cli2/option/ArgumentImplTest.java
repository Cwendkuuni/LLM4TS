package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ResourceHelper;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        List<String> values = new ArrayList<>();
        values.add("foo");
        values.add("bar");

        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", values, 0);

        assertEquals("arg", argument.getName());
        assertEquals("description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(2, argument.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
        assertFalse(argument.getSubsequentSplit());
        assertNull(argument.getValidator());
        assertEquals("--", argument.getConsumeRemaining());
        assertSame(values, argument.getDefaultValues());
    }

    @Test
    public void testArgumentProcessValues() {
        List<String> values = Arrays.asList("foo", "bar");
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Collections.emptyList(), 0);
        WriteableCommandLine commandLine = TestUtils.createCommandLine();

        List<Object> arguments = Arrays.asList("--arg", "--arg foo", "--arg foo bar", "--arg foo bar baz");
        ListIterator<Object> argumentIterator = arguments.listIterator();

        argument.processValues(commandLine, argumentIterator, argument);

        CommandLine parsedCommandLine = TestUtils.parseArguments(commandLine, arguments.subList(0, 4));
        assertEquals(2, parsedCommandLine.getValues(argument).size());
        assertEquals("foo", parsedCommandLine.getValues(argument).get(0));
        assertEquals("bar baz", parsedCommandLine.getValues(argument).get(1));
    }

    @Test
    public void testArgumentCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Collections.emptyList(), 0);

        assertTrue(argument.canProcess(TestUtils.createCommandLine(), "argValue"));
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessInvalidValue() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<Object> arguments = Arrays.asList("--arg", "--arg foo");
        ListIterator<Object> argumentIterator = arguments.listIterator();

        argument.processValues(commandLine, argumentIterator, argument);

        // the following should throw an OptionException
        argument.processValues(commandLine, argumentIterator, argument);
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessMoreValuesThanAllowed() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<Object> arguments = Arrays.asList("--arg", "--arg foo", "--arg foo bar");
        ListIterator<Object> argumentIterator = arguments.listIterator();

        argument.processValues(commandLine, argumentIterator, argument);
    }

    @Test
    public void testArgumentValidate() {
        List<String> values = Arrays.asList("foo", "bar");
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), "--", values, 0);

        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        commandLine.setValues(argument, values);

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testArgumentValidateWithInvalidValue() {
        List<String> values = Arrays.asList("foo", "bar");
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), "--", values, 0);

        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        commandLine.setValues(argument, Arrays.asList("foo", "bar", "invalid"));

        argument.validate(commandLine);
    }

    @Test
    public void testArgumentAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Collections.emptyList(), 0);

        StringBuilder buffer = new StringBuilder();
        Set<DisplaySetting> helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NAME);

        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("arg", buffer.toString().split(" ")[0]);
    }

    @Test
    public void testArgumentHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Collections.emptyList(), 0);

        List<HelpLine> helpLines = argument.helpLines(1, Collections.emptySet(), null);

        HelpLine helpLine = helpLines.get(0);

        assertEquals("arg", helpLine.getOption().getName());
        assertEquals("description", helpLine.getOption().getDescription());
    }

    @Test
    public void testArgumentIsRequired() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Collections.emptyList(), 0);

        assertTrue(argument.isRequired());
    }

    @Test
    public void testArgumentDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Arrays.asList("foo"), 0);

        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        argument.defaults(commandLine);

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("foo", commandLine.getValues(argument).get(0));
    }

    private static class MyValidator implements Validator {
        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            if (!values.get(0).equals("foo")) {
                throw new InvalidArgumentException("Invalid value: " + values.get(0));
            }
        }
    }
}