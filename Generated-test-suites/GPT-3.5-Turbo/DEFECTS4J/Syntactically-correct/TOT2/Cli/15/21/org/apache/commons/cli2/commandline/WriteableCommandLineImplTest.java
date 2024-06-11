import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test adding an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Test adding a value to an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "Test Option");
        commandLine.addValue(option, "testValue");
        assertEquals("testValue", commandLine.getValues(option).get(0));
    }

    @Test
    public void testAddSwitch() {
        // Test adding a switch to an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("switch", "Switch Option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        // Test getting a property value
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        String property = "testProperty";
        commandLine.addProperty(property, "testValue");
        assertEquals("testValue", commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        // Test adding a property to an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("property", "Property Option");
        commandLine.addProperty(option, "testProperty", "testValue");
        assertEquals("testValue", commandLine.getProperty(option, "testProperty", ""));
    }

}