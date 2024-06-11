import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test adding an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        Option option = new Option("testOption", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Test adding a value to an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        Option option = new Option("testOption", "Test Option");
        commandLine.addValue(option, "testValue");
        assertEquals("testValue", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        // Test adding a switch to an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        Option option = new Option("testOption", "Test Option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        // Test getting a property for an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        Option option = new Option("testOption", "Test Option");
        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option, "property1", ""));
    }

    @Test
    public void testLooksLikeOption() {
        // Test checking if a trigger looks like an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        assertTrue(commandLine.looksLikeOption("-testOption"));
    }

}