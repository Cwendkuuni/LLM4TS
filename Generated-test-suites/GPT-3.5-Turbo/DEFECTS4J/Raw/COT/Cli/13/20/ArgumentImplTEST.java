import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);

        String expected = "arg1";
        String actual = argument.getPreferredName();

        assertEquals(expected, actual);
    }

    @Test
    public void testProcessValues() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1").listIterator();
        Option option = null;

        try {
            argument.processValues(commandLine, arguments, option);
        } catch (OptionException e) {
            fail("Caught unexpected OptionException: " + e.getMessage());
        }

        List<String> expected = Arrays.asList("value1");
        List<String> actual = commandLine.getValues(option);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "value1";

        boolean actual = argument.canProcess(commandLine, arg);

        assertTrue(actual);
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);

        char expected = ' ';
        char actual = argument.getInitialSeparator();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ',', null, "--", null, 0);

        char expected = ',';
        char actual = argument.getSubsequentSeparator();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);

        String expected = "--";
        String actual = argument.getConsumeRemaining();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", defaultValues, 0);

        List<String> expected = defaultValues;
        List<String> actual = argument.getDefaultValues();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetValidator() {
        Validator validator = new ValidatorImpl();
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', validator, "--", null, 0);

        Validator expected = validator;
        Validator actual = argument.getValidator();

        assertEquals(expected, actual);
    }

    @Test
    public void testValidate() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = null;

        commandLine.addValue(option, "value1");

        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Caught unexpected OptionException: " + e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 3, ' ', ',', null, "--", null, 0);
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator comp = null;

        argument.appendUsage(buffer, helpSettings, comp);

        String expected = "arg1";
        String actual = buffer.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);

        String expected = "description";
        String actual = argument.getDescription();

        assertEquals(expected, actual);
    }

    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);
        int depth = 0;
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator comp = null;

        List<HelpLine> helpLines = argument.helpLines(depth, helpSettings, comp);

        assertEquals(1, helpLines.size());
        assertEquals("arg1", helpLines.get(0).getOption().getName());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 3, ' ', ' ', null, "--", null, 0);

        int expected = 3;
        int actual = argument.getMaximum();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 2, 3, ' ', ' ', null, "--", null, 0);

        int expected = 2;
        int actual = argument.getMinimum();

        assertEquals(expected, actual);
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);

        String expected = "value";
        String actual = argument.stripBoundaryQuotes("\"value\"");

        assertEquals(expected, actual);
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", null, 0);

        boolean expected = true;
        boolean actual = argument.isRequired();

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaults() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        ArgumentImpl argument = new ArgumentImpl("arg1", "description", 1, 1, ' ', ' ', null, "--", defaultValues, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        argument.defaults(commandLine);

        List<String> expected = defaultValues;
        List<String> actual = commandLine.getValues(argument);

        assertEquals(expected, actual);
    }

}