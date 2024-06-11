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
import org.apache.commons.cli2.validation.DefaultValidator;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Arrays.asList("value1"), 0);
        assertEquals("arg", arg.getPreferredName());
        assertEquals("test argument", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, arg.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, arg.getSubsequentSeparator());
        assertFalse(arg.subsequentSplit());
        assertSame(new DefaultValidator(), arg.getValidator());
        assertEquals(DEFAULT_CONSUME_REMAINING, arg.getConsumeRemaining());
        assertEquals(Arrays.asList("value1"), arg.getDefaultValues());
    }

    @Test
    public void testArgumentProcessValues() {
        final String[] args = new String[]{"--arg", "--consume", "--arg1", "value1", "--arg2", "value2"};
        final List<String> arguments = Arrays.asList(args);
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine commandLine = new CommandLine();

        arg.processValues(commandLine, arguments.listIterator(), arg);

        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessValuesInvalid() {
        final String[] args = new String[]{"--arg", "--consume", "--arg1", "value1", "--arg2", "value1", "value3"};
        final List<String> arguments = Arrays.asList(args);
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine commandLine = new CommandLine();

        arg.processValues(commandLine, arguments.listIterator(), arg);
    }

    @Test
    public void testArgumentCanProcess() {
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        assertTrue(arg.canProcess(new CommandLine(), "value"));
    }

    @Test
    public void testArgumentProcess() {
        final String[] args = new String[]{"--arg", "--consume", "value1", "value2"};
        final List<String> arguments = Arrays.asList(args);
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine commandLine = new CommandLine();

        arg.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));
    }

    @Test
    public void testArgumentValidate() {
        final String[] args = new String[]{"--arg", "value1", "value2"};
        final List<String> arguments = Arrays.asList(args);
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine commandLine = new CommandLine();

        arg.validate(commandLine);

        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));

        final String[] invalidArgs = new String[]{"--arg", "value1", "value2", "invalidValue"};
        final List<String> invalidArguments = Arrays.asList(invalidArgs);
        final Argument invalidArg = new ArgumentImpl("arg", "test argument", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine invalidCommandLine = new CommandLine();

        try {
            arg.validate(invalidCommandLine, arg);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testArgumentHelpLines() {
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final List<HelpLine> helpLines = arg.helpLines(1, Collections.emptySet(), null);

        assertEquals(1, helpLines.size());
        final HelpLine helpLine = helpLines.get(0);
        assertEquals("--arg <value>", helpLine.getOptionSpec().getLongOpt());
        assertEquals("test argument", helpLine.getOptionSpec().getOptDescription());
        assertEquals(null, helpLine.getOptionSpec().getValues());
        assertEquals("", helpLine.getOptionSpec().getArgName());
        assertEquals("", helpLine.getOptionSpec().getArgDescription());
        assertEquals("", helpLine.getOptionSpec().getArgDefaultValue());
    }

    @Test
    public void testArgumentValidateWithInvalidValue() {
        final String[] args = new String[]{"--arg", "value1", "value2", "invalidValue"};
        final List<String> arguments = Arrays.asList(args);
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine commandLine = new CommandLine();

        try {
            arg.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(3, commandLine.getValues(arg).size());
            assertEquals("value1", commandLine.getValues(arg).get(0));
            assertEquals("value2", commandLine.getValues(arg).get(1));
            assertEquals("invalidValue", commandLine.getValues(arg).get(2));
            assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, e.getMessage());
        }
    }

    @Test
    public void testArgumentValidateWithInvalidValueAndMaximum() {
        final String[] args = new String[]{"--arg", "value1", "value2", "value3"};
        final List<String> arguments = Arrays.asList(args);
        final Argument arg = new ArgumentImpl("arg", "test argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine commandLine = new CommandLine();

        try {
            arg.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(1, commandLine.getValues(arg).size());
            assertEquals("value1", commandLine.getValues(arg).get(0));
            assertEquals(ResourceConstants.ARGUMENT_TOO_MANY_VALUES, e.getMessage());
        }
    }

    @Test
    public void testArgumentValidateWithInvalidValueAndMinimum() {
        final String[] args = new String[]{"--arg", "value1"};
        final List<String> arguments = Arrays.asList(args);
        final Argument arg = new ArgumentImpl("arg", "test argument", 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new DefaultValidator(), DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
        final WriteableCommandLine commandLine = new CommandLine();

        try {
            arg.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(1, commandLine.getValues(arg).size());
            assertEquals("value1", commandLine.getValues(arg).get(0));
            assertEquals(ResourceConstants.ARGUMENT_TOO_FEW_VALUES, e.getMessage());
        }
    }
}