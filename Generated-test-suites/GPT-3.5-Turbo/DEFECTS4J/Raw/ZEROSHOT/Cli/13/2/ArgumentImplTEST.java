import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", Collections.singletonList("default"), 0);
        assertEquals(Collections.singletonList("default"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertNull(argument.getValidator());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 3, '\0', '\0', null, "--", null, 0);
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 2, 3, '\0', '\0', null, "--", null, 0);
        assertEquals(2, argument.getMinimum());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("token", argument.stripBoundaryQuotes("\"token\""));
        assertEquals("token", argument.stripBoundaryQuotes("\"token"));
        assertEquals("token", argument.stripBoundaryQuotes("token\""));
        assertEquals("token", argument.stripBoundaryQuotes("token"));
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument1 = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertFalse(argument1.isRequired());
        
        ArgumentImpl argument2 = new ArgumentImpl("arg", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertTrue(argument2.isRequired());
    }

    @Test
    public void testProcessValues() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 2, '\0', ',', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1,value2").listIterator();
        Option option = new OptionImpl(0, false);
        try {
            argument.processValues(commandLine, arguments, option);
            assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option));
        } catch (OptionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "value";
        assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void testProcess() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 2, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "value2").listIterator();
        try {
            argument.process(commandLine, arguments);
            assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(argument));
        } catch (OptionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testValidate() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 2, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        argument.defaults(commandLine);
        assertEquals(Arrays.asList("default"), commandLine.getValues(option));
    }
}