package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

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
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.DefaultValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

    private ResourceHelper resources;
    private ArgumentImpl argument;
    private WriteableCommandLine cmdLine;

    @Before
    public void setUp() {
        resources = ResourceHelper.getResourceHelper();
        cmdLine = new DefaultCommandLine();
        argument = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                                   new DefaultValidator(), DEFAULT_CONSUME_REMAINING, new ArrayList<>(), 0);
    }

    @Test
    public void testArgumentImplConstructor() {
        assertEquals("arg", argument.getName());
        assertEquals("description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals(NUL, argument.getInitialSeparator());
        assertEquals(NUL, argument.getSubsequentSeparator());
        assertFalse(argument.isSubsequentSplit());
        assertNull(argument.getValidator());
        assertEquals(DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
        assertSame(Collections.EMPTY_LIST, argument.getDefaultValues());

        ArgumentImpl argument2 = new ArgumentImpl("arg2", null, 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                                                  null, DEFAULT_CONSUME_REMAINING, new ArrayList<>(), 0);
        assertEquals("arg2", argument2.getName());
        assertNull(argument2.getDescription());
        assertEquals(2, argument2.getMinimum());
        assertEquals(2, argument2.getMaximum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplMinimumGreaterThanMaximum() {
        new ArgumentImpl("arg", "description", 3, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                        null, DEFAULT_CONSUME_REMAINING, new ArrayList<>(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplDefaultValuesLessThanMinimum() {
        List defaultValues = Arrays.asList("default");
        new ArgumentImpl("arg", "description", 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                        null, DEFAULT_CONSUME_REMAINING, defaultValues, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplDefaultValuesGreaterThanMaximum() {
        List defaultValues = Arrays.asList("default1", "default2");
        new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                        null, DEFAULT_CONSUME_REMAINING, defaultValues, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        String[] args = {"--arg", "--arg foo", "--arg foo bar"};
        cmdLine.parseArgument(OptionBuilder.withLongOpt("arg").build(), args);

        List<String> values = cmdLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("foo bar", values.get(2));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(cmdLine, "arg"));
        assertTrue(argument.canProcess(cmdLine, "arg foo"));
        assertTrue(argument.canProcess(cmdLine, "arg foo bar"));
        assertFalse(argument.canProcess(cmdLine, "--consume"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() throws OptionException {
        String[] args = {"--arg", "--arg foo", "--arg foo bar"};
        cmdLine.parseArgument(OptionBuilder.withLongOpt("arg").build(), args);

        argument.process(cmdLine, cmdLine.argIterator());

        assertEquals(3, cmdLine.getValues(argument).size());
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
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.EMPTY_LIST, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertEquals(new DefaultValidator(), argument.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        List values = Arrays.asList("value1", "value2");
        cmdLine.putValues(argument, values);

        argument.validate(cmdLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithEmptyValues() throws OptionException {
        List values = Collections.emptyList();
        cmdLine.putValues(argument, values);

        argument.validate(cmdLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() throws OptionException {
        List values = Arrays.asList("value1", "invalid value");
        cmdLine.putValues(argument, values);

        argument.validate(cmdLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        String expected = "arg [<ARG>...]";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        StringBuffer buffer = new StringBuffer();
        List helpLines = argument.helpLines(0, null, null);

        ArgumentHelpLine argHelpLine = (ArgumentHelpLine) helpLines.get(0);

        String expectedHelpLine = "--arg <ARG>...";
        assertEquals(expectedHelpLine, argHelpLine.getHelpLine());

        expectedHelpLine = "arg:";
        assertEquals(expectedHelpLine, argHelpLine.getOptionSpec());

        assertEquals("description", argHelpLine.getDescription());

        expectedHelpLine = "arg [<ARG>...]";
        assertEquals(expectedHelpLine, argHelpLine.getUsageLine());

        expectedHelpLine = null;
        List values = Arrays.asList("value1", "value2");
        argHelpLine.setValues(values);

        expectedHelpLine = "arg: value1 value2 ...";
        assertEquals(expectedHelpLine, argHelpLine.getValuesUsageLine());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        assertEquals("arg", argument.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg foo", argument.stripBoundaryQuotes("\"arg foo\""));
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() throws OptionException {
        List defaultValues = Arrays.asList("default1", "default2");
        argument = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                                   new DefaultValidator(), DEFAULT_CONSUME_REMAINING, defaultValues, 0);

        cmdLine.parseOption(OptionBuilder.withLongOpt("arg").build(), null);

        argument.defaults(cmdLine);

        assertEquals(2, cmdLine.getValues(argument).size());
        assertEquals("default1", cmdLine.getValue(argument, 0));
        assertEquals("default2", cmdLine.getValue(argument, 1));
    }
}

class ArgumentHelpLine extends HelpLine {
    private List values;

    public ArgumentHelpLine(Argument argument, int depth) {
        super(argument, depth);
    }

    public void setValues(List values) {
        this.values = values;
    }

    @Override
    public String getValuesUsageLine() {
        return getArgument().getName() + " [<ARG>...]";
    }

    @Override
    public String getValuesDescription() {
        if (values == null) {
            return "";
        }

        StringBuilder description = new StringBuilder();
        description.append("accepts ").append(values.size()).append(" values, including: ");

        for (Object value : values) {
            description.append(value).append(" ");
        }

        return description.toString();
    }
}

class HelpLineImpl implements HelpLine {
    private Argument argument;
    private int depth;

    public HelpLineImpl(Argument argument, int depth) {
        this.argument = argument;
        this.depth = depth;
    }

    @Override
    public String getHelpLine() {
        return getArgument().getLongOpt();
    }

    @Override
    public String getOptionSpec() {
        StringBuilder optionSpec = new StringBuilder();
        optionSpec.append(getArgument().getName());

        if (getArgument().isRequired()) {
            optionSpec.append(":");
        }

        return optionSpec.toString();
    }

    @Override
    public String getDescription() {
        return getArgument().getDescription();
    }

    @Override
    public String getLongOpt() {
        return "--" + getArgument().getName();
    }

    @Override
    public String getShortOpt() {
        return null;
    }

    @Override
    public String getUsageLine() {
        return getLongOpt() + " " + getArgument().getUsage();
    }

    @Override
    public String getValuesUsageLine() {
        return getArgument().getName() + " [<ARG>...]";
    }

    @Override
    public Argument getArgument() {
        return argument;
    }
}