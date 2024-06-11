import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplConstructor() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', null, "--", Arrays.asList("value1"), 0);
        Assert.assertEquals("argName", argument.getPreferredName());
        Assert.assertEquals("description", argument.getDescription());
        Assert.assertEquals(1, argument.getMinimum());
        Assert.assertEquals(1, argument.getMaximum());
        Assert.assertEquals(' ', argument.getInitialSeparator());
        Assert.assertEquals(' ', argument.getSubsequentSeparator());
        Assert.assertFalse(argument.isSubsequentSplit());
        Assert.assertNull(argument.getValidator());
        Assert.assertEquals("--", argument.getConsumeRemaining());
        Assert.assertEquals(Arrays.asList("value1"), argument.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        String[] args = {"--argName", "value1", "value2", "--consume"};
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 2, ' ', ' ', null, "--", Arrays.asList("valueDefault"), 0);

        argument.process(commandLine, Arrays.asList(args).listIterator());
        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooFewArguments() {
        String[] args = {"--argName"};
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', null, "--", Arrays.asList("valueDefault"), 0);

        argument.process(commandLine, Arrays.asList(args).listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooManyArguments() {
        String[] args = {"--argName", "value1", "value2", "value3"};
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', null, "--", Arrays.asList("valueDefault"), 0);

        argument.process(commandLine, Arrays.asList(args).listIterator());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', null, "--", Arrays.asList("valueDefault"), 0);

        Assert.assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', null, "--", Arrays.asList("value1", "value2"), 0);

        argument.validate(commandLine);

        List values = commandLine.getValues(argument);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', null, "--", null, 0);

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', null, "--", Arrays.asList("value1", "value2", "value3"), 0);

        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithCustomValidator() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', ' ', new MyCustomValidator(), "--", null, 0);

        List values = commandLine.getValues(argument);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    private static class MyCustomValidator implements Validator {
        @Override
        public void validate(List values) {
            if (!values.get(0).equals("value1")) {
                throw new InvalidArgumentException("Invalid value: " + values.get(0));
            }
        }
    }
}