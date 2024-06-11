import org.apache.commons.cli2.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ArgumentImplTest {

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Argument argument = new ArgumentImpl("test", "test", 1, 1, ' ', ' ', null, null, null, 1);
        List<String> arguments = Arrays.asList("test1");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        List<String> expected = Arrays.asList("test1");
        List<String> actual = commandLine.getValues(argument);

        assertEquals(expected, actual);
    }

    // Test for minimum and maximum values
    @Test(expected = OptionException.class)
    public void testProcessValuesMinValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Argument argument = new ArgumentImpl("test", "test", 1, 0, ' ', ' ', null, null, null, 1);
        List<String> arguments = Arrays.asList("test1", "test2");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        List<String> actual = commandLine.getValues(argument);

        assertEquals(Arrays.asList("test1"), actual);

        // Test for maximum values
        argument = new ArgumentImpl("test", "test", 1, 1, ' ', ' ', null, null, null, 1);
        arguments = Arrays.asList("test1", "test2");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        actual = commandLine.getValues(argument);

        assertEquals(Arrays.asList("test1"), actual);

        // Test for minimum values not met
        argument = new ArgumentImpl("test", "test", 2, 0, ' ', ' ', null, null, null, 1);
        arguments = Arrays.asList("test1", "test2");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        actual = commandLine.getValues(argument);

        assertEquals(Arrays.asList("test1", "test2"), actual);
    }

    // Test for validator exception
    @Test(expected = OptionException.class)
    public void testProcessValuesValidatorException() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Argument argument = new ArgumentImpl("test", "test", 1, 1, ' ', ' ', new MyCustomValidator(), null, null, 1);
        List<String> arguments = Arrays.asList("test1", "test2");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        List<String> actual = commandLine.getValues(argument);

        assertEquals(Arrays.asList("test1"), actual);

        try {
            argument.validator.validate(arguments);
        } catch (InvalidArgumentException ive) {
            throw new AssertionError(ive.getMessage());
        }

        // Test for validator exception being thrown
        assertThrows(InvalidArgumentException.class, () -> argument.validator.validate(arguments));
    }

    // Test for consuming remaining arguments
    @Test
    public void testProcessValuesConsumeRemaining() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Argument argument = new ArgumentImpl("test", "test", 1, 1, ' ', ' ', null, "--", null, 1);
        List<String> arguments = Arrays.asList("test1", "--", "test3");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        List<String> actual = commandLine.getValues(argument);

        assertEquals(Arrays.asList("test1", "test3"), actual);
    }

    // Test for splitting values with subsequentSeparator
    @Test
    public void testProcessValuesSubsequentSeparator() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Argument argument = new ArgumentImpl("test", "test", 1, 1, ' ', ' ', null, null, null, 1);
        List<String> arguments = Arrays.asList("test1 test2");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        List<String> actual = commandLine.getValues(argument);

        assertEquals(Arrays.asList("test1", "test2"), actual);
    }

    // Test for infinite arguments
    @Test
    public void testProcessValuesInfiniteArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Argument argument = new ArgumentImpl("test", "test", 1, Integer.MAX_VALUE, ' ', ' ', null, null, null, 1);
        List<String> arguments = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        List<String> actual = commandLine.getValues(argument);

        // Test for number of arguments
        assertEquals(6, actual.size());

        // Test for last argument
        assertEquals("test6", actual.get(actual.size() - 1));
    }
}