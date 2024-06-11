package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplWithMinimumMaximumZero() {
        Argument argument = new ArgumentImpl("arg", "description", 0, 0, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<String> args = Arrays.asList("arg1", "arg2");
        argument.process(commandLine, args.listIterator());

        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));
        assertEquals("arg2", commandLine.getValues(argument).get(1));
    }

    @Test
    public void testArgumentImplWithMinimumMaximumOne() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<String> args = Arrays.asList("arg1");
        argument.process(commandLine, args.listIterator());

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));

        List<String> wrongArgs = Arrays.asList("arg1", "arg2");
        try {
            argument.process(commandLine, wrongArgs.listIterator());
            fail("Should have thrown OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testArgumentImplWithMinimumMaximumMoreThanOne() {
        Argument argument = new ArgumentImpl("arg", "description", 3, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<String> args = Arrays.asList("arg1", "arg2", "arg3");
        argument.process(commandLine, args.listIterator());

        assertEquals(3, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));
        assertEquals("arg2", commandLine.getValues(argument).get(1));
        assertEquals("arg3", commandLine.getValues(argument).get(2));

        List<String> wrongArgs = Arrays.asList("arg1", "arg2", "arg3", "arg4");
        try {
            argument.process(commandLine, wrongArgs.listIterator());
            fail("Should have thrown OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testArgumentImplWithDefaultValue() {
        Argument argument = new ArgumentImpl("arg", "description", 0, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("defaultValue"), 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<String> args = Collections.emptyList();
        argument.process(commandLine, args.listIterator());

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("defaultValue", commandLine.getValues(argument).get(0));
    }

    @Test
    public void testArgumentImplWithValidator() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        List<String> validArgs = Arrays.asList("validValue1", "validValue2");
        WriteableCommandLine commandLine = new WriteableCommandLine();

        argument.process(commandLine, validArgs.listIterator());
        List<String> values = commandLine.getValues(argument);
        assertTrue(values.size() == 1);
        assertTrue(values.get(0) instanceof MyValidatedValue);

        List<String> invalidArgs = Arrays.asList("invalidValue1", "invalidValue2");
        try {
            argument.process(commandLine, invalidArgs.listIterator());
            fail("Should have thrown OptionException");
        } catch (OptionException e) {
            // expected
        }
    }
}

class MyValidator implements Validator {
    @Override
    public void validate(List<String> values) throws InvalidArgumentException {
        for (String value : values) {
            if (!isValid(value)) {
                throw new InvalidArgumentException("Invalid argument", value);
            }
        }
    }

    private boolean isValid(String value) {
        // custom validation logic here
        return true;
    }
}

class MyValidatedValue {
    private final String value;

    public MyValidatedValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}