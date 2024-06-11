package org.apache.commons.cli2.option;

import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_MISSING_VALUES;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_MIN_EXCEEDS_MAX;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_TOO_FEW_DEFAULTS;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_TOO_MANY_DEFAULTS;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_UNEXPECTED_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.ArgumentImpl;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.NoArgValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        resourceHelper = new ResourceHelper();
        commandLine = new CommandLine();
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ',
                new NoArgValidator(), "--", Collections.singletonList("test"));
    }

    @After
    public void tearDown() {
        argument = null;
        commandLine = null;
        resourceHelper = null;
    }

    @Test
    public void testArgumentName() {
        assertEquals("testArg", argument.getPreferredName());
    }

    @Test
    public void testArgumentDescription() {
        assertEquals("Test Argument", argument.getDescription());
    }

    @Test
    public void testArgumentMinMax() {
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testArgumentInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testArgumentSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void testArgumentSubsequentSplit() {
        assertFalse(argument.getSubsequentSplit());
    }

    @Test
    public void testArgumentValidator() {
        assertNotNull(argument.getValidator());
    }

    @Test
    public void testArgumentConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testArgumentDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testProcessValuesWithMinimumValues() {
        List<String> arguments = new LinkedList<>();
        arguments.add("testValue1");
        arguments.add("testValue2");

        argument.process(commandLine, arguments.listIterator(), argument);

        assertEquals(2, commandLine.getValues(argument).size());
    }

    @Test
    public void testProcessValuesWithMaximumValues() {
        List<String> arguments = new LinkedList<>();
        arguments.add("testValue1");
        arguments.add("testValue2");
        arguments.add("testValue3");

        argument.process(commandLine, arguments.listIterator(), argument);

        assertEquals(3, commandLine.getValues(argument).size());
    }

    @Test
    public void testProcessValuesWithConsumingOption() {
        List<String> arguments = new LinkedList<>();
        arguments.add("--test");
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("arg3");

        argument.process(commandLine, arguments.listIterator(), argument);

        assertEquals(3, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));
        assertEquals("arg2", commandLine.getValues(argument).get(1));
        assertEquals("arg3", commandLine.getValues(argument).get(2));
    }

    @Test
    public void testProcessValuesWithSubsequentSeparator() {
        List<String> arguments = new LinkedList<>();
        arguments.add("testValue1 testValue2 testValue3");

        argument.setSubsequentSeparator(';');

        argument.process(commandLine, arguments.listIterator(), argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));
        assertEquals("testValue3", values.get(2));
    }

    @Test
    public void testProcessValuesWithInvalidValue() {
        List<String> arguments = new LinkedList<>();
        arguments.add("testValue1");
        arguments.add("testValue2");
        arguments.add("InvalidValue");

        assertThrows(OptionException.class, () -> {
            argument.process(commandLine, arguments.listIterator(), argument);
        });

        List<String> values = commandLine.getValues(argument);
        assertEquals(2, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testCanProcess() {
        assertFalse(argument.canProcess(commandLine, "testValue"));
        assertTrue(argument.canProcess(commandLine, "testValue testValue2"));
    }

    @Test
    public void testProcessWithEmptyArgumentList() {
        argument.process(commandLine, commandLine.argumentList().listIterator(), argument);
        assertEquals(0, commandLine.getValues(argument).size());
    }

    @Test
    public void testValidateWithMinimumValues() {
        List<String> values = Arrays.asList("testValue1", "testValue2");
        commandLine.setValues(argument, values);

        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithFewerValues() {
        List<String> values = Arrays.asList("testValue1");
        commandLine.setValues(argument, values);

        assertThrows(OptionException.class, () -> {
            argument.validate(commandLine);
        });

        assertEquals(ARGUMENT_MISSING_VALUES, commandLine.getGlobalValues().get(0));
    }

    @Test
    public void testValidateWithMoreValues() {
        List<String> values = Arrays.asList("testValue1", "testValue2", "testValue3");
        commandLine.setValues(argument, values);

        assertThrows(OptionException.class, () -> {
            argument.validate(commandLine);
        });

        assertEquals(ARGUMENT_UNEXPECTED_VALUE, commandLine.getGlobalValues().get(0));
    }

    @Test
    public void testValidateWithInvalidValue() {
        List<String> values = Arrays.asList("testValue1", "testValue2", "InvalidValue");
        commandLine.setValues(argument, values);

        assertThrows(OptionException.class, () -> {
            argument.validate(commandLine);
        });

        assertEquals(ARGUMENT_UNEXPECTED_VALUE, commandLine.getGlobalValues().get(0));
    }

    @Test
    public void testValidateWithValidator() {
        List<String> values = Arrays.asList("testValue1", "testValue2", "testValue3");
        commandLine.setValues(argument, values);

        Validator customValidator = new Validator() {
            @Override
            public void validate(List<String> values) throws InvalidArgumentException {
                throw new InvalidArgumentException(ARGUMENT_UNEXPECTED_VALUE, "Invalid Value");
            }
        };

        argument.setValidator(customValidator);
        argument.validate(commandLine);

        assertEquals(ARGUMENT_UNEXPECTED_VALUE, commandLine.getGlobalValues().get(0));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer sb = new StringBuffer();
        argument.appendUsage(sb, null, null);

        assertEquals("testArg [test]  ", sb.toString().trim());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> helpLines = argument.helpLines(1, null, null);

        formatter.printHelp("Help Test", "", "", "", "", helpLines);
        System.out.println();

        assertEquals("testArg [test]\n", System.out.getHistory(0).getLine());
    }

    @Test
    public void testDefaults() {
        argument.defaults(commandLine);

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("test", commandLine.getValues(argument).get(0));
    }
}