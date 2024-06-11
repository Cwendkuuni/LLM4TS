import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        assertEquals("argName", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, '\0', '\0',
                null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "value2", "value3", "value4").listIterator();
        argument.processValues(commandLine, arguments, argument);
        assertEquals(Arrays.asList("value1", "value2", "value3"), commandLine.getValues(argument));
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, ' ', '\0',
                null, "--", null, 0);
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', ',',
                null, "--", null, 0);
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = Arrays.asList("value1", "value2", "value3");
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, '\0', '\0',
                null, "--", defaultValues, 0);
        assertEquals(defaultValues, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Validator validator = new MyValidator();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                validator, "--", null, 0);
        assertEquals(validator, argument.getValidator());
    }
    
    @Test
    public void testValidate() throws OptionException {
        Validator validator = new MyValidator();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, '\0', '\0',
                validator, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidValue() throws OptionException {
        Validator validator = new MyValidator();
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, '\0', '\0',
                validator, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "invalid value");
        commandLine.addValue(argument, "value3");
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFewValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 2, 3, '\0', '\0',
                null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 2, '\0', '\0',
                null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, '\0', '\0',
                null, "--", null, 0);
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("argName", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        List<HelpLine> helpLines = argument.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals(0, helpLine.getDepth());
        assertEquals(argument, helpLine.getOption());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, '\0', '\0',
                null, "--", null, 0);
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 2, 3, '\0', '\0',
                null, "--", null, 0);
        assertEquals(2, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument1 = new ArgumentImpl("arg1", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        assertTrue(argument1.isRequired());

        ArgumentImpl argument2 = new ArgumentImpl("arg2", "description", 0, 1, '\0', '\0',
                null, "--", null, 0);
        assertFalse(argument2.isRequired());
    }

    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 1, '\0', '\0',
                null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        argument.defaults(commandLine);
        assertEquals(Collections.emptyList(), commandLine.getValues(argument));
    }

    private static class MyValidator implements Validator {

        @Override
        public void validate(List values) throws InvalidArgumentException {
            // custom validation logic
        }
    }
}