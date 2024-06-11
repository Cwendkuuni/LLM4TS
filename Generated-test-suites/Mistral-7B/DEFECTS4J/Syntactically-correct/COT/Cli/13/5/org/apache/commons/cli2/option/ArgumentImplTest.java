import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validator.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ArgumentImplTest {

    @Test
    public void testArgumentConstructor() {
        String name = "myArgument";
        String description = "This is my argument";
        int minimum = 1;
        int maximum = 5;
        char initialSeparator = '-';
        char subsequentSeparator = ':';
        Validator validator = mock(Validator.class);
        String consumeRemaining = "--consume";
        List defaultValues = Collections.singletonList("defaultValue");
        int id = 0;

        Argument argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        Assert.assertEquals(name, argument.getPreferredName());
        Assert.assertEquals(description, argument.getDescription());
        Assert.assertEquals(minimum, argument.getMinimum());
        Assert.assertEquals(maximum, argument.getMaximum());
        Assert.assertEquals(initialSeparator, argument.getInitialSeparator());
        Assert.assertEquals(subsequentSeparator, argument.getSubsequentSeparator());
        Assert.assertEquals(consumeRemaining, argument.getConsumeRemaining());
        Assert.assertEquals(defaultValues, argument.getDefaultValues());
        Assert.assertNotNull(argument.getValidator());
    }

    @Test
    public void testArgumentProcessValues() {
        String name = "myArgument";
        String description = "This is my argument";
        int minimum = 1;
        int maximum = 3;
        char initialSeparator = '-';
        char subsequentSeparator = ':';
        Validator validator = mock(Validator.class);
        String consumeRemaining = "--consume";
        List defaultValues = Collections.singletonList("defaultValue");
        int id = 0;

        Argument argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        List<String> arguments = Arrays.asList("arg1", "--consume", "arg2", "arg3");
        ListIterator<String> argumentIterator = arguments.listIterator();

        when(commandLine.getValues(eq(argument))).thenReturn(arguments);

        argument.process(commandLine, argumentIterator);

        Argument expectedArgument = argument;
        WriteableCommandLine expectedCommandLine = commandLine;
        List<String> expectedArguments = new ArrayList<>(arguments);

        verify(commandLine).addValue(eq(argument), eq("arg1"));
        verify(commandLine).addValue(eq(argument), "arg2");
        verify(commandLine).addValue(eq(argument), "arg3");

        Assert.assertEquals(expectedArgument, argument);
        Assert.assertEquals(expectedCommandLine, commandLine);
        Assert.assertEquals(Arrays.asList("arg1", "arg2", "arg3"), expectedArguments);
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessValuesWithInvalidArguments() {
        String name = "myArgument";
        String description = "This is my argument";
        int minimum = 1;
        int maximum = 3;
        char initialSeparator = '-';
        char subsequentSeparator = ':';
        Validator validator = mock(Validator.class);
        String consumeRemaining = "--consume";
        List defaultValues = Collections.singletonList("defaultValue");
        int id = 0;

        Argument argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        List<String> arguments = Arrays.asList("arg1", "--consume", "arg2", "option", "arg3");
        ListIterator<String> argumentIterator = arguments.listIterator();

        when(commandLine.getValues(eq(argument))).thenReturn(arguments);

        argument.process(commandLine, argumentIterator);

        Argument expectedArgument = argument;
        WriteableCommandLine expectedCommandLine = commandLine;
        List<String> expectedArguments = new ArrayList<>(arguments);

        verify(commandLine).addValue(eq(argument), "arg1");
        verify(commandLine).addValue(eq(argument), "arg2");
        verify(commandLine).addValue(eq(argument), "arg3");

        Assert.assertEquals(expectedArgument, argument);
        Assert.assertEquals(expectedCommandLine, commandLine);
        Assert.assertEquals(Arrays.asList("arg1", "arg2", "arg3"), expectedArguments);

        verify(argument).validate(eq(commandLine));
        verify(validator).validate(eq(Collections.singletonList("option")));

        ArgumentException exception = assertThrows(OptionException.class, () -> argument.process(commandLine, argumentIterator));
        InvalidArgumentException innerException = (InvalidArgumentException) exception.getInnerException();

        Assert.assertEquals("option", innerException.getValue());
        Assert.assertEquals("Invalid value: option", innerException.getMessage());
    }

    @Test
    public void testArgumentValidate() {
        String name = "myArgument";
        String description = "This is my argument";
        int minimum = 1;
        int maximum = 3;
        char initialSeparator = '-';
        char subsequentSeparator = ':';
        Validator validator = mock(Validator.class);
        String consumeRemaining = "--consume";
        List defaultValues = Collections.singletonList("defaultValue");
        int id = 0;

        Argument argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        List<String> values = Arrays.asList("arg1", "arg2", "arg3");

        when(commandLine.getValues(eq(argument))).thenReturn(values);
        when(validator.validate(eq(values))).thenReturn(null);

        argument.validate(commandLine);

        verify(validator).validate(eq(values));

        Argument expectedArgument = argument;
        WriteableCommandLine expectedCommandLine = commandLine;
        List<String> expectedValues = values;

        Assert.assertEquals(expectedArgument, argument);
        Assert.assertEquals(expectedCommandLine, commandLine);
        Assert.assertEquals(expectedValues, commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testArgumentValidateWithInvalidValues() {
        String name = "myArgument";
        String description = "This is my argument";
        int minimum = 1;
        int maximum = 3;
        char initialSeparator = '-';
        char subsequentSeparator = ':';
        Validator validator = mock(Validator.class);
        String consumeRemaining = "--consume";
        List defaultValues = Collections.singletonList("defaultValue");
        int id = 0;

        Argument argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        List<String> values = Arrays.asList("arg1", "arg2", "arg4");

        when(commandLine.getValues(eq(argument))).thenReturn(values);
        when(validator.validate(eq(values))).thenThrow(new InvalidArgumentException("Invalid value: arg4"));

        argument.validate(commandLine);

        Argument expectedArgument = argument;
        WriteableCommandLine expectedCommandLine = commandLine;
        List<String> expectedValues = values;

        Assert.assertEquals(expectedArgument, argument);
        Assert.assertEquals(expectedCommandLine, commandLine);
        Assert.assertEquals(expectedValues, commandLine.getValues(argument));

        ArgumentException exception = assertThrows(OptionException.class, () -> argument.validate(commandLine));
        InvalidArgumentException innerException = (InvalidArgumentException) exception.getInnerException();

        Assert.assertEquals("arg4", innerException.getValue());
        Assert.assertEquals("Invalid value: arg4", innerException.getMessage());
    }
}