import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setup() {
        argument = new ArgumentImpl("arg", "description", 1, 3, '\0', '\0', null, "", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"value\"";
        String expected = "value";
        String actual = argument.stripBoundaryQuotes(token);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("value1", "value2").listIterator();
        Option option = new OptionImpl(0, false);

        argument.processValues(commandLine, arguments, option);

        List<String> actualValues = commandLine.getValues(option);
        List<String> expectedValues = Arrays.asList("value1", "value2");

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "value";
        assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator args = Arrays.asList("value1", "value2").listIterator();

        try {
            argument.process(commandLine, args);
        } catch (OptionException e) {
            fail("Unexpected OptionException");
        }

        List<String> actualValues = commandLine.getValues(argument);
        List<String> expectedValues = Arrays.asList("value1", "value2");

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetTriggers() {
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");

        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected OptionException");
        }
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithUnexpectedValue() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");

        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.emptySet(), null);
        String expected = "arg";
        String actual = buffer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescription() {
        String expected = "description";
        String actual = argument.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        HelpLine expectedHelpLine = new HelpLineImpl(argument, depth);

        List<HelpLine> expected = Collections.singletonList(expectedHelpLine);
        List<HelpLine> actual = argument.helpLines(depth, Collections.emptySet(), null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximum() {
        int expected = 3;
        int actual = argument.getMaximum();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimum() {
        int expected = 1;
        int actual = argument.getMinimum();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        argument.defaults(commandLine);

        List<String> actualValues = commandLine.getValues(argument);

        assertNull(actualValues);
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        List<String> defaultValues = Arrays.asList("default1", "default2");

        argument.defaultValues(commandLine, option);

        List<String> actualValues = commandLine.getDefaultValues(option);

        assertEquals(defaultValues, actualValues);
    }
}