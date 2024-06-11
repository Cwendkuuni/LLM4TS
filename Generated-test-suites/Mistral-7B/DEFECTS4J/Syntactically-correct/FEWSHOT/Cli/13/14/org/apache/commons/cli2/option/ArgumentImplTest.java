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
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.MockValidator;
import org.apache.commons.cli2.validation.Validator;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplCreation() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        assertEquals("arg", arg.getPreferredName());
        assertEquals("Argument test", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, arg.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, arg.getSubsequentSeparator());
        assertEquals(false, arg.isRequired());
        assertEquals(Collections.EMPTY_SET, arg.getPrefixes());
        assertNull(arg.getValidator());
        assertNull(arg.getConsumeRemaining());
        assertNull(arg.getDefaultValues());

        Argument argWithDefaults = new ArgumentImpl("arg", "Argument test", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("value1", "value2"), 0);
        assertEquals("arg", argWithDefaults.getPreferredName());
        assertEquals("Argument test", argWithDefaults.getDescription());
        assertEquals(1, argWithDefaults.getMinimum());
        assertEquals(3, argWithDefaults.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argWithDefaults.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argWithDefaults.getSubsequentSeparator());
        assertEquals(false, argWithDefaults.isRequired());
        assertEquals(Collections.EMPTY_SET, argWithDefaults.getPrefixes());
        assertNull(argWithDefaults.getValidator());
        assertEquals("value1", argWithDefaults.getDefaultValues().get(0));
        assertEquals("value2", argWithDefaults.getDefaultValues().get(1));
    }

    @Test
    public void testArgumentImplGetters() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        assertEquals("arg", arg.getPreferredName());
        assertEquals("Argument test", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, arg.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, arg.getSubsequentSeparator());
        assertEquals(false, arg.isRequired());
        assertEquals(Collections.EMPTY_SET, arg.getPrefixes());
        assertNull(arg.getValidator());
        assertNull(arg.getConsumeRemaining());
        assertNull(arg.getDefaultValues());

        Argument argWithValidator = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);
        assertEquals("arg", argWithValidator.getPreferredName());
        assertEquals("Argument test", argWithValidator.getDescription());
        assertEquals(1, argWithValidator.getMinimum());
        assertEquals(1, argWithValidator.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argWithValidator.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argWithValidator.getSubsequentSeparator());
        assertEquals(false, argWithValidator.isRequired());
        assertEquals(Collections.EMPTY_SET, argWithValidator.getPrefixes());
        assertSame(new MockValidator(), argWithValidator.getValidator());
        assertNull(argWithValidator.getConsumeRemaining());
        assertNull(argWithValidator.getDefaultValues());
    }

    @Test
    public void testArgumentImplProcessValues() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new CommandLine();
        List<String> arguments = new ArrayList<>();

        // Single argument
        arguments.add("--arg value1");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(1, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));

        // Multiple arguments
        arguments.clear();
        arguments.add("--arg value1");
        arguments.add("--arg value2");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));

        // Consuming option
        arguments.clear();
        arguments.add("--consume-option");
        arguments.add("--arg value1");
        assertFalse(arg.canProcess(commandLine, "--consume-option"));
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(1, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));

        // Consuming option with multiple values
        arguments.clear();
        arguments.add("--consume-option");
        arguments.add("--arg value1");
        arguments.add("--arg value2");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));
    }

    @Test
    public void testArgumentImplProcessValuesWithSeparator() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new CommandLine();
        List<String> arguments = new ArrayList<>();

        // Single argument with separator
        arguments.add("--arg value1 value2");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));

        // Multiple arguments with separator
        arguments.clear();
        arguments.add("--arg value1 value2");
        arguments.add("--arg value3 value4");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(4, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));
        assertEquals("value3", commandLine.getValues(arg).get(2));
        assertEquals("value4", commandLine.getValues(arg).get(3));
    }

    @Test
    public void testArgumentImplProcessValuesWithSubsequentSeparator() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new CommandLine();
        List<String> arguments = new ArrayList<>();

        // Single argument with subsequent separator
        arguments.add("--arg value1--value2");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(1, commandLine.getValues(arg).size());
        assertEquals("value1--value2", commandLine.getValues(arg).get(0));

        // Multiple arguments with subsequent separator
        arguments.clear();
        arguments.add("--arg value1--value2");
        arguments.add("--arg value3--value4");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1--value2", commandLine.getValues(arg).get(0));
        assertEquals("value3--value4", commandLine.getValues(arg).get(1));
    }

    @Test
    public void testArgumentImplProcessValuesWithSubsequentSplit() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, ';', null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new CommandLine();
        List<String> arguments = new ArrayList<>();

        // Single argument with subsequent split
        arguments.add("--arg value1;value2");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));

        // Multiple arguments with subsequent split
        arguments.clear();
        arguments.add("--arg value1;value2");
        arguments.add("--arg value3;value4");
        arg.processValues(commandLine, arguments.listIterator(), arg);
        assertEquals(4, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));
        assertEquals("value3", commandLine.getValues(arg).get(2));
        assertEquals("value4", commandLine.getValues(arg).get(3));
    }

    @Test
    public void testArgumentImplValidate() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new CommandLine();

        List<String> validValues = Arrays.asList("value1", "value2");
        arg.setValidator(new MockValidator(validValues));

        List<String> validArguments = Arrays.asList("--arg value1", "--arg value1 value2");
        for (String argument : validArguments) {
            arg.processValues(commandLine, argument.split(" "), arg);
            arg.validate(commandLine, arg);
        }

        List<String> invalidArguments = Arrays.asList("--arg invalidValue", "--arg value1 value3");
        for (String argument : invalidArguments) {
            arg.processValues(commandLine, argument.split(" "), arg);
            try {
                arg.validate(commandLine, arg);
                fail();
            } catch (OptionException e) {
                // expected
            }
        }
    }

    @Test
    public void testArgumentImplHelpLines() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        HelpLine helpLine = arg.helpLines(1, Collections.EMPTY_SET, null).get(0);
        assertEquals("--arg <value>", helpLine.getLongOpt());
        assertEquals("Argument test", helpLine.getOptDescription());
        assertEquals("ARGUMENT", helpLine.getOptType());
        assertEquals(false, helpLine.isArgRequired());
        assertEquals(Argument.UNLIMITED, helpLine.getArgNumber());
        assertNull(helpLine.getArgMetavar());
        assertEquals("", helpLine.getArgDescription());
        assertEquals("", helpLine.getArgDefaultValue());

        List<HelpLine> helpLines = arg.helpLines(1, Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED), null);
        assertEquals(1, helpLines.size());
        HelpLine helpLineNumbered = helpLines.get(0);
        assertEquals("--arg [ARG]", helpLineNumbered.getLongOpt());
        assertEquals("Argument test", helpLineNumbered.getOptDescription());
        assertEquals("ARGUMENT", helpLineNumbered.getOptType());
        assertEquals(false, helpLineNumbered.isArgRequired());
        assertEquals(Argument.UNLIMITED, helpLineNumbered.getArgNumber());
        assertNull(helpLineNumbered.getArgMetavar());
        assertEquals("", helpLineNumbered.getArgDescription());
        assertEquals("", helpLineNumbered.getArgDefaultValue());
    }
}