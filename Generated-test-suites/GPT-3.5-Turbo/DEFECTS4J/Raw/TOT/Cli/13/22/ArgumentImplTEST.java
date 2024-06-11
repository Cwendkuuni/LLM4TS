import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    private static final String NAME = "arg";
    private static final String DESCRIPTION = "Test Argument";
    private static final int MINIMUM = 1;
    private static final int MAXIMUM = 3;
    private static final char INITIAL_SEPARATOR = '=';
    private static final char SUBSEQUENT_SEPARATOR = ',';
    private static final String CONSUME_REMAINING = "--";
    private static final List<String> DEFAULT_VALUES = Arrays.asList("val1", "val2");
    private ArgumentImpl argument;

    @Before
    public void setup() {
        argument = new ArgumentImpl(NAME, DESCRIPTION, MINIMUM, MAXIMUM, INITIAL_SEPARATOR, SUBSEQUENT_SEPARATOR,
                null, CONSUME_REMAINING, DEFAULT_VALUES, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(NAME, argument.getPreferredName());
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(null, "test"));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(DEFAULT_VALUES, argument.getDefaultValues());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.addValue(argument, "value");
        commandLine.addValue(argument, "value2");

        argument.validate(commandLine);

        assertTrue(true);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidMinimum() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.addValue(argument, "value");

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidMaximum() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.addValue(argument, "value");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        commandLine.addValue(argument, "value4");

        argument.validate(commandLine);

    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidArgumentException() throws OptionException {
        Validator validator = (values) -> {
            throw new InvalidArgumentException("Invalid argument");
        };
        ArgumentImpl argumentWithValidator = new ArgumentImpl(NAME, DESCRIPTION, 1, 3, INITIAL_SEPARATOR,
                SUBSEQUENT_SEPARATOR, validator, CONSUME_REMAINING, DEFAULT_VALUES, 0);

        WriteableCommandLine commandLine = new CommandLine();
        commandLine.addValue(argumentWithValidator, "value");

        argumentWithValidator.validate(commandLine);
    }

    @Test
    public void testProcessValues() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        List<String> testValues = Arrays.asList("value1", "value2", "value3");

        argument.processValues(commandLine, testValues.listIterator(), argument);

        assertEquals(testValues, commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testProcessValues_InvalidRemaining() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        List<String> testValues = Arrays.asList("value1", "value2", "--", "value3");

        argument.processValues(commandLine, testValues.listIterator(), argument);
    }

    @Test
    public void testProcessValues_SubsequentSplit() throws OptionException {
        char separator = ',';
        ArgumentImpl argumentWithSubsequentSplit = new ArgumentImpl(NAME, DESCRIPTION, 1, 3,
                INITIAL_SEPARATOR, separator, null, CONSUME_REMAINING, DEFAULT_VALUES, 0);
        WriteableCommandLine commandLine = new CommandLine();
        List<String> testValues = Arrays.asList("value1,value2,value3");

        argumentWithSubsequentSplit.processValues(commandLine, testValues.listIterator(),
                argumentWithSubsequentSplit);

        assertEquals(testValues, commandLine.getValues(argumentWithSubsequentSplit));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("arg arg2 arg3", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals(DESCRIPTION, argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        ResourceHelper resourceHelper = ResourceHelper.getResourceHelper();
        HelpLineImpl helpLine = new HelpLineImpl(argument, 0);
        List<HelpLine> expectedHelpLines = Arrays.asList(helpLine);

        List<HelpLine> result = argument.helpLines(0, null, null);

        assertEquals(expectedHelpLines, result);
    }

    @Test
    public void testGetMaximum() {
        assertEquals(MAXIMUM, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(MINIMUM, argument.getMinimum());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new CommandLine();

        argument.defaults(commandLine);

        assertEquals(DEFAULT_VALUES, commandLine.getValues(argument));
    }
}