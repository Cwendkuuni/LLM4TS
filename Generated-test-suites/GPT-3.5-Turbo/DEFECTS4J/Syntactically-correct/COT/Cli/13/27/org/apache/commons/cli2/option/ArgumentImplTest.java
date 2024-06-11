import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() throws Exception {
        argument = new ArgumentImpl("arg", "Description", 1, 5, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        // Test basic functionality
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator args = Arrays.asList("value1", "value2").listIterator();
        Option option = new OptionImpl(0, false);
        argument.processValues(commandLine, args, option);
        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
        
        // Test consuming remaining arguments
        commandLine = new WriteableCommandLineImpl();
        args = Arrays.asList("value1", "--", "value2", "value3").listIterator();
        argument.processValues(commandLine, args, option);
        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
        assertEquals("value3", commandLine.getValues(option).get(2));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(null, "test"));
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
    public void testValidate() {
        // Test minimum and maximum values
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        argument.validate(commandLine);
        
        // Test missing values
        commandLine = new WriteableCommandLineImpl();
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            assertEquals("Missing values for argument: arg", e.getMessage());
        }
        
        // Test unexpected value
        commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            assertEquals("Unexpected value for argument: arg: value3", e.getMessage());
        }
        
        // Test validator
        Validator validator = new Validator() {
            public void validate(List values) throws InvalidArgumentException {
                if (values.contains("invalid")) {
                    throw new InvalidArgumentException("Invalid value");
                }
            }
        };
        argument = new ArgumentImpl("arg", "Description", 1, 5, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, validator, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
        commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "valid");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            assertEquals("Unexpected value for argument: arg: Invalid value: invalid", e.getMessage());
        }
    }

    @Test
    public void testGenerateUsage() {
        // Test basic usage
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        Comparator comp = null;
        argument.appendUsage(buffer, helpSettings, comp);
        assertEquals("arg", buffer.toString());
        
        // Test optional argument
        buffer = new StringBuffer();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        argument.appendUsage(buffer, helpSettings, comp);
        assertEquals("[arg]", buffer.toString());
        
        // Test numbered argument
        buffer = new StringBuffer();
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        argument.appendUsage(buffer, helpSettings, comp);
        assertEquals("arg1", buffer.toString());
        
        // Test bracketed argument
        buffer = new StringBuffer();
        helpSettings.remove(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        argument.appendUsage(buffer, helpSettings, comp);
        assertEquals("<arg>", buffer.toString());
        
        // Test multiple values
        argument = new ArgumentImpl("arg", "Description", 1, Integer.MAX_VALUE, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
        buffer = new StringBuffer();
        helpSettings.remove(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        argument.appendUsage(buffer, helpSettings, comp);
        assertEquals("arg ...", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Description", argument.getDescription());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(5, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        assertEquals("test", argument.stripBoundaryQuotes("\"test\""));
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        // Test null default values
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        argument.defaults(commandLine);
        assertNull(commandLine.getDefaultValues(option));
        
        // Test default values
        List<String> defaultValues = Arrays.asList("default1", "default2");
        argument = new ArgumentImpl("arg", "Description", 1, 5, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, defaultValues, 0);
        commandLine = new WriteableCommandLineImpl();
        argument.defaults(commandLine);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

}