import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;



public class ArgumentImplTest {

    @Test
    public void testProcessValues() throws OptionException {
        Argument argument = createArgument();
        WriteableCommandLine commandLine = new CommandLine();
        ListIterator<String> arguments = createArgumentsList("--value1", "--value2", "arg1", "arg2").listIterator();

        argument.processValues(commandLine, arguments, argument);

        Assert.assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValues(argument));
    }

    @Test
    public void testProcessValuesWithInitialSeparator() throws OptionException {
        Argument argument = createArgumentWithInitialSeparator();
        WriteableCommandLine commandLine = new CommandLine();
        ListIterator<String> arguments = createArgumentsList("--value1", "--", "arg1", "arg2").listIterator();

        argument.processValues(commandLine, arguments, argument);

        Assert.assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValues(argument));
    }

    @Test
    public void testProcessValuesWithSubsequentSeparator() throws OptionException {
        Argument argument = createArgumentWithSubsequentSeparator();
        WriteableCommandLine commandLine = new CommandLine();
        ListIterator<String> arguments = createArgumentsList("--value", "arg1,arg2,arg3").listIterator();

        argument.processValues(commandLine, arguments, argument);

        Assert.assertEquals(Arrays.asList("arg1", "arg2", "arg3"), commandLine.getValues(argument));
    }

    @Test
    public void testCanProcess() {
        Argument argument = createArgument();
        WriteableCommandLine commandLine = new CommandLine();

        Assert.assertTrue(argument.canProcess(commandLine, "arg"));
    }

    @Test
    public void testGetInitialSeparator() {
        Argument argument = createArgument();

        Assert.assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Argument argument = createArgument();

        Assert.assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        Argument argument = createArgument();

        Assert.assertEquals(ArgumentImpl.DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        Argument argument = createArgumentWithDefaultValues();

        Assert.assertEquals(Arrays.asList("default1", "default2"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Argument argument = createArgumentWithValidator();

        Assert.assertNotNull(argument.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        Argument argument = createArgumentWithMinimumAndMaximumValues(1, 2);
        WriteableCommandLine commandLine = new CommandLine();

        commandLine.addValue(argument, "value1");

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingValues() throws OptionException {
        Argument argument = createArgumentWithMinimumAndMaximumValues(2, 3);
        WriteableCommandLine commandLine = new CommandLine();

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithUnexpectedValue() throws OptionException {
        Argument argument = createArgumentWithMinimumAndMaximumValues(1, 2);
        WriteableCommandLine commandLine = new CommandLine();

        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");

        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        Argument argument = createArgument();

        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        Assert.assertEquals("<arg>", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        Argument argument = createArgument();

        Assert.assertEquals("A description of the argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        Argument argument = createArgument();

        List<?> helpLines = argument.helpLines(0, null, null);

        Assert.assertEquals(1, helpLines.size());
        Assert.assertEquals("    <arg>                A description of the argument", helpLines.get(0).toString());
    }

    @Test
    public void testGetMaximum() {
        Argument argument = createArgument();

        Assert.assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        Argument argument = createArgument();

        Assert.assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testIsRequired() {
        Argument argument = createArgumentWithMinimumAndMaximumValues(1, 1);
        Argument optionalArgument = createOptionalArgument();

        Assert.assertTrue(argument.isRequired());
        Assert.assertFalse(optionalArgument.isRequired());
    }

    @Test
    public void testDefaults() {
        Argument argument = createArgumentWithDefaultValues();
        WriteableCommandLine commandLine = new CommandLine();

        argument.defaults(commandLine);

        Assert.assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(argument));
    }

    private Argument createArgument() {
        return new ArgumentImpl("arg", "A description of the argument", 1, 1, '\0', '\0', null, "--", null, 0);
    }

    private Argument createArgumentWithInitialSeparator() {
        return new ArgumentImpl("arg", "A description of the argument", 1, 1, ',', '\0', null, "--", null, 0);
    }

    private Argument createArgumentWithSubsequentSeparator() {
        return new ArgumentImpl("arg", "A description of the argument", 1, Integer.MAX_VALUE, '\0', ',', null, "--", null, 0);
    }

    private Argument createArgumentWithDefaultValues() {
        return new ArgumentImpl("arg", "A description of the argument", 1, 1, '\0', '\0', null, "--", Arrays.asList("default1", "default2"), 0);
    }

    private Argument createArgumentWithValidator() {
        return new ArgumentImpl("arg", "A description of the argument", 1, 1, '\0', '\0', new Validator() {
            public void validate(List<?> values) throws InvalidArgumentException {
                // Custom validation logic
            }
        }, "--", null, 0);
    }

    private Argument createArgumentWithMinimumAndMaximumValues(int minimum, int maximum) {
        return new ArgumentImpl("arg", "A description of the argument", minimum, maximum, '\0', '\0', null, "--", null, 0);
    }

    private Argument createOptionalArgument() {
        return new ArgumentImpl("arg", "A description of the argument", 0, 1, '\0', '\0', null, "--", null, 0);
    }

    private List<String> createArgumentsList(String... arguments) {
        return Arrays.asList(arguments);
    }
}

