import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        // Initialize the command line
        commandLine = new MyWriteableCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "An option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "An option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertTrue(commandLine.hasValue(option));
        assertEquals("value", commandLine.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("o", "option", true, "An option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getArgumentValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "An option");
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        List<String> defaultValues = commandLine.getDefaultValues(option);
        assertEquals(2, defaultValues.size());
        assertEquals("default1", defaultValues.get(0));
        assertEquals("default2", defaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "A switch option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "A switch option");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");
        assertEquals("value1", commandLine.getProperty("key1"));
        assertEquals("value2", commandLine.getProperty("key2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("s", "switch", false, "A switch option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddValueToNonexistentOption() {
        Option option = new Option("o", "option", true, "An option");
        commandLine.addValue(option, "value");
        assertFalse(commandLine.hasOption(option));
    }

    private class MyWriteableCommandLine implements WriteableCommandLine {
        // Implement the interface methods here
    }

    // Add more test cases if necessary

}



// WriteableCommandLine JUnit 4 test cases go here

