import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test adding an option to the options list
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        // Test adding a value to the values map
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertTrue(commandLine.getValues(option, null).contains("value"));
    }

    @Test
    public void testAddSwitch() {
        // Test adding a switch to the switches map
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        // Test checking if an option is present in the options list
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Test getting an option using its trigger
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        // Test getting values for an option
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        // Test getting the value of a switch
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        // Test adding a property to the properties map
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty("key", "value");
        assertEquals("value", commandLine.getProperty("key", null));
    }

    @Test
    public void testLooksLikeOption() {
        // Test checking if a trigger looks like an option
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertTrue(commandLine.looksLikeOption("-t"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        // Test converting the command line to a string
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.toString());
    }
}