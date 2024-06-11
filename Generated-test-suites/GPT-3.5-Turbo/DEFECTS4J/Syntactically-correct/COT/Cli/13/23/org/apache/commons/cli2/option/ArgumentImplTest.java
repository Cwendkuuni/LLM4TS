import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.OptionException;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
        assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
        assertEquals("", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", defaultValues, 0);
        assertEquals(defaultValues, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
        assertEquals(null, argument.getValidator());
    }

    @Test
    public void testProcessValues() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLineImpl();
            ListIterator<String> arguments = new ArrayList<String>().listIterator();
            ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
            argument.processValues(commandLine, arguments, null);
            assertTrue(commandLine.getValues(null).isEmpty());
        } catch (OptionException e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
        assertTrue(argument.canProcess(null, ""));
    }

    @Test
    public void testProcess() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLineImpl();
            ListIterator<String> arguments = new ArrayList<String>().listIterator();
            ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
            argument.process(commandLine, arguments);
            assertTrue(commandLine.getValues(argument).isEmpty());
        } catch (OptionException e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testValidate() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLineImpl();
            ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "", null, 0);
            argument.validate(commandLine);
            assertFalse(commandLine.hasErrors());
        } catch (OptionException e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "", null, 0);
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        assertEquals("arg <arg>", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "", null, 0);
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "", null, 0);
        assertEquals(2, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "", null, 0);
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "", null, 0);
        assertEquals("arg", argument.stripBoundaryQuotes("\"arg\""));
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "", null, 0);
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLineImpl();
            ArgumentImpl argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "", null, 0);
            argument.defaults(commandLine);
            assertTrue(commandLine.getValues(null).isEmpty());
        } catch (OptionException e) {
            fail("Should not throw any exception");
        }
    }

}