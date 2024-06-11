import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 1, 3, '\0', '\0', null, "--", null, 0);
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
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = argument.stripBoundaryQuotes("\"token\"");
        assertEquals("token", token);
    }

    @Test
    public void testStripBoundaryQuotes_NoQuotes() {
        String token = argument.stripBoundaryQuotes("token");
        assertEquals("token", token);
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("value1", "value2", "value3").listIterator();
        Option option = new OptionImpl(0, false);

        try {
            argument.processValues(commandLine, arguments, option);
        } catch (OptionException e) {
            fail("OptionException should not have been thrown");
        }

        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
        assertEquals("value3", commandLine.getValues(option).get(2));
    }

    @Test
    public void testProcessValues_InvalidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("-option", "value1", "value2", "value3").listIterator();
        Option option = new OptionImpl(0, false);

        try {
            argument.processValues(commandLine, arguments, option);
        } catch (OptionException e) {
            fail("OptionException should not have been thrown");
        }

        assertEquals(0, commandLine.getValues(option).size());
        assertEquals("-option", arguments.previous());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(argument.canProcess(commandLine, "arg"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        commandLine.addValue(option, "value");

        try {
            argument.validate(commandLine, option);
        } catch (OptionException e) {
            fail("OptionException should not have been thrown");
        }

        assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testValidate_MissingValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        try {
            argument.validate(commandLine, option);
            fail("OptionException should have been thrown");
        } catch (OptionException e) {
            assertEquals(ResourceConstants.ARGUMENT_MISSING_VALUES, e.getMessageKey());
        }
    }

    @Test
    public void testValidate_TooManyValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        commandLine.addValue(option, "extraValue");

        try {
            argument.validate(commandLine, option);
            fail("OptionException should have been thrown");
        } catch (OptionException e) {
            assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, e.getMessageKey());
            assertEquals("extraValue", e.getMessageArgs()[0]);
        }
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        argument.defaults(commandLine);

        assertNull(commandLine.getValues(option));
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        argument.defaultValues(commandLine, option);

        assertNull(commandLine.getValues(option));
    }
}