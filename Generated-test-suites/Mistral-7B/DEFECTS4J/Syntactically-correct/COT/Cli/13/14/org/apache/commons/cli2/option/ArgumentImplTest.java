package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ArgumentImplTest {

    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        resourceHelper = new ResourceHelper() {
            @Override
            public String getMessage(String key) {
                return ResourceConstants.getMessage(key);
            }
        };

        argument = new ArgumentImpl("argName", "description", 1, 3, '(', ')',
                new Validator() {
                    @Override
                    public void validate(List values) throws InvalidArgumentException {
                        // validation logic here
                    }
                }, "", Collections.emptyList(), 0);

        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("argName", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3");
        commandLine.parse(arguments.subList(0, 3).toArray(new String[0]));

        argument.processValues(commandLine, arguments.listIterator(), new Option(""));

        List<String> commandLineValues = commandLine.getValues(argument);

        assertEquals(3, commandLineValues.size());
        assertEquals("arg1", commandLineValues.get(0));
        assertEquals("arg2", commandLineValues.get(1));
        assertEquals("arg3", commandLineValues.get(2));

        // Test with consumeRemaining
        arguments = Arrays.asList("--consume-remaining", "arg1", "arg2", "arg3");
        commandLine.parse(arguments.subList(0, 3).toArray(new String[0]));
        argument.processValues(commandLine, arguments.listIterator(), new Option(""));

        commandLineValues = commandLine.getValues(argument);

        assertEquals(3, commandLineValues.size());
        assertEquals("arg1", commandLineValues.get(0));
        assertEquals("arg2", commandLineValues.get(1));
        assertEquals("arg3", commandLineValues.get(2));
        assertTrue(commandLine.hasValue("consumeRemaining"));
        assertEquals(3, commandLine.size());
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "arg1"));
        assertTrue(argument.canProcess(commandLine, "--consume-remaining"));
        assertFalse(argument.canProcess(commandLine, "--option"));
    }

    @Test
    public void testProcess() {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3");
        commandLine.parse(arguments.toArray(new String[0]));

        argument.process(commandLine, commandLine.listIterator());

        List<String> commandLineValues = commandLine.getValues(argument);

        assertEquals(3, commandLineValues.size());
        assertEquals("arg1", commandLineValues.get(0));
        assertEquals("arg2", commandLineValues.get(1));
        assertEquals("arg3", commandLineValues.get(2));

        // Test with consumeRemaining
        arguments = Arrays.asList("--consume-remaining", "arg1", "arg2", "arg3");
        commandLine.parse(arguments.toArray(new String[0]));
        argument.process(commandLine, commandLine.listIterator());

        commandLineValues = commandLine.getValues(argument);

        assertEquals(3, commandLineValues.size());
        assertEquals("arg1", commandLineValues.get(0));
        assertEquals("arg2", commandLineValues.get(1));
        assertEquals("arg3", commandLineValues.get(2));
        assertTrue(commandLine.hasValue("consumeRemaining"));
        assertEquals(3, commandLine.size());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('(', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.emptyList(), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertEquals(argument.getValidator(), argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateMinimumValue() {
        argument = new ArgumentImpl("argName", "description", 0, 3, '(', ')',
                new Validator() {
                    @Override
                    public void validate(List values) throws InvalidArgumentException {
                        // validation logic here
                    }
                }, "", Collections.emptyList(), 0);

        commandLine.parse(new String[]{"arg1"});
        argument.validate(commandLine);

        fail();
    }

    @Test(expected = OptionException.class)
    public void testValidateMaximumValue() {
        argument = new ArgumentImpl("argName", "description", 1, 5, '(', ')',
                new Validator() {
                    @Override
                    public void validate(List values) throws InvalidArgumentException {
                        // validation logic here
                    }
                }, "", Collections.emptyList(), 0);

        commandLine.parse(Arrays.asList("arg1", "arg2", "arg3", "arg4", "arg5").toArray(new String[0]));
        argument.validate(commandLine);

        fail();
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidValue() {
        argument = new ArgumentImpl("argName", "description", 1, 3, '(', ')',
                new Validator() {
                    @Override
                    public void validate(List values) throws InvalidArgumentException {
                        if (values.size() > 1) {
                            throw new InvalidArgumentException("Invalid value", "test");
                        }
                    }
                }, "", Collections.emptyList(), 0);

        commandLine.parse(Arrays.asList("arg1", "arg2").toArray(new String[0]));
        argument.validate(commandLine);

        fail();
    }

    @Test
    public void testAppendUsage() {
        List<String> helpSettings = Arrays.asList(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED, DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        StringBuilder sb = new StringBuilder();

        argument.appendUsage(sb, helpSettings, null);

        assertEquals("1. [argName]", sb.toString());

        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_OPTIONAL);

        sb.setLength(0);
        argument.appendUsage(sb, helpSettings, null);

        assertEquals("1. [argName] 2. [argName] ...", sb.toString());
    }
}