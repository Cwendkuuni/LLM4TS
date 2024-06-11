import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        ListIterator arguments = Arrays.asList("value1", "value2").listIterator();
        Option option = new OptionImpl(0, false);
        argument.processValues(commandLine, arguments, option);
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option));
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "arg";
        assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        ListIterator args = Arrays.asList("value1", "value2").listIterator();
        try {
            argument.process(commandLine, args);
        } catch (OptionException e) {
            fail("Exception should not be thrown");
        }
        assertEquals(Arrays.asList("value1"), commandLine.getValues(argument));
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, 'i', '\0', null, "--", null, 0);
        assertEquals('i', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', 's', null, "--", null, 0);
        assertEquals('s', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", Arrays.asList("default1"), 0);
        assertEquals(Arrays.asList("default1"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Validator validator = new Validator() {
            public void validate(List values) throws InvalidArgumentException {

            }
        };
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', validator, "--", null, 0);
        assertEquals(validator, argument.getValidator());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        Option option = new OptionImpl(0, false);
        commandLine.addValue(option, "value");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, '\0', '\0', null, "--", null, 0);
        argument.appendUsage(buffer, Collections.EMPTY_SET, null);
        assertEquals("name", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        List<HelpLine> helpLines = argument.helpLines(0, Collections.EMPTY_SET, null);
        assertEquals(1, helpLines.size());
        assertEquals(argument, helpLines.get(0).getArgument());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, '\0', '\0', null, "--", null, 0);
        assertEquals(2, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, '\0', '\0', null, "--", null, 0);
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl requiredArgument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertTrue(requiredArgument.isRequired());

        ArgumentImpl optionalArgument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertFalse(optionalArgument.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", Arrays.asList("default"), 0);
        Option option = new OptionImpl(0, false);
        argument.defaults(commandLine);
        assertEquals(Arrays.asList("default"), commandLine.getValues(option));
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        argument.defaultValues(commandLine, null);
        assertEquals(Collections.emptyList(), commandLine.getValues(argument));
    }

}