import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        // Test getPreferredName method
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ':', '\0', null, "--", null, 0);
        // Test getInitialSeparator method
        assertEquals(':', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', ',', null, "--", null, 0);
        // Test getSubsequentSeparator method
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--consume", null, 0);
        // Test getConsumeRemaining method
        assertEquals("--consume", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", Collections.singletonList("default"), 0);
        // Test getDefaultValues method
        assertEquals(Collections.singletonList("default"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        // Create ArgumentImpl object
        Validator validator = new Validator();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', validator, "--", null, 0);
        // Test getValidator method
        assertEquals(validator, argument.getValidator());
    }

    @Test
    public void testGetMaximum() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 5, '\0', '\0', null, "--", null, 0);
        // Test getMaximum method
        assertEquals(5, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 2, 3, '\0', '\0', null, "--", null, 0);
        // Test getMinimum method
        assertEquals(2, argument.getMinimum());
    }

    @Test
    public void testGetTriggers() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        // Test getTriggers method
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testProcess() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        // Create WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLine();
        // Create ListIterator object
        ListIterator args = Collections.singletonList("value").listIterator();
        // Test process method
        try {
            argument.process(commandLine, args);
        } catch (OptionException e) {
            // Catch OptionException
        }
        assertEquals("value", commandLine.getValue(argument));
    }

    @Test
    public void testCanProcess() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        // Create WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLine();
        // Test canProcess method
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void testValidate() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 2, 3, '\0', '\0', null, "--", null, 0);
        // Create WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        // Test validate method
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            // Catch OptionException
        }
    }

    @Test
    public void testStripBoundaryQuotes() {
        // Create ArgumentImpl object
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        // Test stripBoundaryQuotes method
        assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
        assertEquals("value", argument.stripBoundaryQuotes("value"));
    }
}