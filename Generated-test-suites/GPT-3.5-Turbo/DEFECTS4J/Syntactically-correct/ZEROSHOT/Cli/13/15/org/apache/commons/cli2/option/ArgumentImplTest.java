import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {
    
    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "--", null, 0);
    }

    @After
    public void tearDown() {
        argument = null;
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
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "value2").listIterator();
        Option option = new OptionImpl(0, false);

        try {
            argument.processValues(commandLine, arguments, option);
        } catch (OptionException e) {
            fail("OptionException thrown: " + e.getMessage());
        }

        List<String> values = commandLine.getValues(option);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(null, ""));
    }

    @Test
    public void testGetPrefixes() {
        assertNotNull(argument.getPrefixes());
        assertEquals(0, argument.getPrefixes().size());
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = Arrays.asList("value1", "value2").listIterator();

        try {
            argument.process(commandLine, args);
        } catch (OptionException e) {
            fail("OptionException thrown: " + e.getMessage());
        }

        List<String> values = commandLine.getValues(argument);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetTriggers() {
        assertNotNull(argument.getTriggers());
        assertEquals(0, argument.getTriggers().size());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        try {
            argument.validate(commandLine, option);
        } catch (OptionException e) {
            fail("OptionException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);

        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("arg1 arg2", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);

        List<HelpLine> helpLines = argument.helpLines(0, helpSettings, null);

        assertNotNull(helpLines);
        assertEquals(1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        assertEquals(argument, helpLine.getOption());
        assertEquals(0, helpLine.getDepth());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "value";
        assertEquals(token, argument.stripBoundaryQuotes(token));

        token = "\"value\"";
        assertEquals("value", argument.stripBoundaryQuotes(token));
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        commandLine.addValue(option, "value");

        argument.defaults(commandLine);

        List<String> values = commandLine.getValues(option);

        assertNotNull(values);
        assertEquals(0, values.size());
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        argument.defaultValues(commandLine, option);

        List<String> values = commandLine.getValues(option);

        assertNull(values);
    }
}