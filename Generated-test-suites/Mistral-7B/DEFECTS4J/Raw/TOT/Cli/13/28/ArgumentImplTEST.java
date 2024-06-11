package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithMinimumMaximum() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.defaults(commandLine);

        CommandLine parsedArgs = arg.parse(commandLine, new String[]{"arg", "value1", "value2"});

        Assert.assertEquals("value1", parsedArgs.getArgument(arg.getIndex()));
        Assert.assertEquals("value2", parsedArgs.getArgument(arg.getIndex(1)));
        Assert.assertNull(parsedArgs.getArgument(arg.getIndex(2)));

        parsedArgs = arg.parse(commandLine, new String[]{"arg", "value1", "value2", "value3"});

        Assert.assertEquals("value1", parsedArgs.getArgument(arg.getIndex()));
        Assert.assertEquals("value2", parsedArgs.getArgument(arg.getIndex(1)));
        Assert.assertEquals("value3", parsedArgs.getArgument(arg.getIndex(2)));
    }

    @Test
    public void testArgumentWithMinimumOnly() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.defaults(commandLine);

        CommandLine parsedArgs = arg.parse(commandLine, new String[]{"arg"});

        Assert.assertEquals("", parsedArgs.getArgument(arg.getIndex()));

        try {
            parsedArgs = arg.parse(commandLine, new String[]{"arg", "value1"});
        } catch (OptionException e) {
            Assert.assertEquals("The argument 'arg' requires exactly 1 value but 2 values were provided", e.getMessage());
        }

        parsedArgs = arg.parse(commandLine, new String[]{"arg", "value1", "value2"});

        Assert.assertEquals("value1", parsedArgs.getArgument(arg.getIndex()));
        Assert.assertNull(parsedArgs.getArgument(arg.getIndex(1)));
    }

    @Test
    public void testArgumentWithMaximumOnly() {
        Argument arg = new ArgumentImpl("arg", "an argument", 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.defaults(commandLine);

        CommandLine parsedArgs = arg.parse(commandLine, new String[]{"arg", "value1", "value2"});

        Assert.assertEquals("value1", parsedArgs.getArgument(arg.getIndex()));
        Assert.assertEquals("value2", parsedArgs.getArgument(arg.getIndex(1)));

        parsedArgs = arg.parse(commandLine, new String[]{"arg", "value1", "value2", "value3"});

        Assert.assertEquals("value1", parsedArgs.getArgument(arg.getIndex()));
        Assert.assertEquals("value2", parsedArgs.getArgument(arg.getIndex(1)));
        Assert.assertEquals("value3", parsedArgs.getArgument(arg.getIndex(2)));

        try {
            parsedArgs = arg.parse(commandLine, new String[]{"arg", "value1", "value2", "value3", "value4"});
        } catch (OptionException e) {
            Assert.assertEquals("The argument 'arg' requires at most 2 values but 4 values were provided", e.getMessage());
        }
    }

    @Test
    public void testArgumentWithDefaults() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("default1", "default2"), 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.defaults(commandLine);

        CommandLine parsedArgs = arg.parse(commandLine, new String[0]);

        Assert.assertEquals("default1", parsedArgs.getArgument(arg.getIndex()));
        Assert.assertEquals("default2", parsedArgs.getArgument(arg.getIndex(1)));

        parsedArgs = arg.parse(commandLine, new String[]{"arg"});

        Assert.assertEquals("", parsedArgs.getArgument(arg.getIndex()));

        parsedArgs = arg.parse(commandLine, new String[]{"arg", "value"});

        Assert.assertEquals("value", parsedArgs.getArgument(arg.getIndex()));
        Assert.assertEquals("default2", parsedArgs.getArgument(arg.getIndex(1)));
    }

    @Test
    public void testArgumentWithCustomValidator() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new ArgumentCustomValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            arg.defaults(commandLine);
        } catch (OptionException e) {
            Assert.fail("Defaults should not fail");
        }

        CommandLine parsedArgs = arg.parse(commandLine, new String[]{"arg", "invalid"});

        try {
            arg.validate(commandLine, arg);
        } catch (OptionException e) {
            Assert.assertEquals("The argument 'arg' is invalid: invalid", e.getMessage());
        }

        parsedArgs = arg.parse(commandLine, new String[]{"arg", "valid"});

        arg.validate(commandLine, arg);
    }

    public static class ArgumentCustomValidator implements Validator {
        @Override
        public void validate(List values) throws InvalidArgumentException {
            if (!values.get(0).toString().startsWith("valid")) {
                throw new InvalidArgumentException("The argument is invalid: invalid");
            }
        }
    }
}