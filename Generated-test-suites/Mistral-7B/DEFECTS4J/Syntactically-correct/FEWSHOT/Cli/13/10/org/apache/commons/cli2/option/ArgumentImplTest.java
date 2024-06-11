package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        assertNotNull(argument);
        assertEquals("argName", argument.getPreferredName());
        assertEquals("description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
        assertFalse(argument.canProcess(null, ""));
        assertTrue(argument.canProcess(null, "argName"));
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcessValues() throws OptionException {
        final String[] args = {"arg1", "arg2", "--option"};
        final List<String> argumentValues = new ArrayList<>();
        final WriteableCommandLine commandLine = new CommandLineParserImpl().parse(Collections.EMPTY_LIST, "argName argValue", args);
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.processValues(commandLine, commandLine.getArgIterator(), argument);
        assertEquals(2, argumentValues.size());
        assertEquals("arg1", argumentValues.get(0));
        assertEquals("arg2", argumentValues.get(1));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesInvalidNumberOfArguments() throws OptionException {
        final String[] args = {"arg1", "arg2", "arg3"};
        final WriteableCommandLine commandLine = new CommandLineParserImpl().parse(Collections.EMPTY_LIST, "argName argValue", args);
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.processValues(commandLine, commandLine.getArgIterator(), argument);
    }

    @Test
    public void testProcessValuesConsumingOption() throws OptionException {
        final String[] args = {"--consumingOption", "arg1", "arg2"};
        final WriteableCommandLine commandLine = new CommandLineParserImpl().parse(Collections.EMPTY_LIST, "argName argValue", args);
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.processValues(commandLine, commandLine.getArgIterator(), argument);
        assertTrue(commandLine.getValues(argument).isEmpty());
        assertEquals(2, commandLine.size());
        assertEquals("arg1", commandLine.getArgument(1));
        assertEquals("arg2", commandLine.getArgument(2));
    }

    @Test
    public void testProcessValuesWithSeparator() throws OptionException {
        final String[] args = {"arg1", "--argName=arg2,arg3"};
        final WriteableCommandLine commandLine = new CommandLineParserImpl().parse(Collections.EMPTY_LIST, "argName argValue", args);
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.processValues(commandLine, commandLine.getArgIterator(), argument);
        assertEquals(2, argument.values().size());
        assertEquals("arg2", argument.values().get(0));
        assertEquals("arg3", argument.values().get(1));
    }

    @Test
    public void testValidate() throws OptionException {
        final List<String> values = new ArrayList<>();
        values.add("value1");
        final WriteableCommandLine commandLine = new CommandLineParserImpl().parse(Collections.EMPTY_LIST, "argName argValue", Collections.EMPTY_LIST.toArray(new String[0]));
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.validate(commandLine);
        assertEquals(1, values.size());
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFewValues() throws OptionException {
        final List<String> values = new ArrayList<>();
        final WriteableCommandLine commandLine = new CommandLineParserImpl().parse(Collections.EMPTY_LIST, "argName argValue", Collections.EMPTY_LIST.toArray(new String[0]));
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyValues() throws OptionException {
        final List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        values.add("value3");
        final WriteableCommandLine commandLine = new CommandLineParserImpl().parse(Collections.EMPTY_LIST, "argName argValue", Collections.EMPTY_LIST.toArray(new String[0]));
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        argument.validate(commandLine, argument);
        argument.validate(commandLine, argument);
        argument.validate(commandLine, argument);
    }

    @Test
    public void testHelpLines() {
        final ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        final List<HelpLine> helpLines = argument.helpLines(1, Collections.EMPTY_SET, null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("-argName, --argName\n  argName\n  description\n", helpLine.getShortText());
        assertEquals("argName\n  description\n", helpLine.getLongText());
        assertEquals(1, helpLine.getNumberedValues().size());
        assertEquals("", helpLine.getNumberedValues().get(0));
    }
}