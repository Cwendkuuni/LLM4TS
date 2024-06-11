import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        // Test adding an option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("f", "file", true, "file option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("file"));
        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption("x"));
    }

    @Test
    public void testAddValue() {
        // Test adding a value to an option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("f", "file", true, "file option");
        commandLine.addOption(option);
        commandLine.addValue(option, "file1.txt");
        assertEquals("file1.txt", commandLine.getValue(option));
    }

    @Test
    public void testGetValues() {
        // Test getting values for an option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("f", "file", true, "file option");
        commandLine.addOption(option);
        commandLine.addValue(option, "file1.txt");
        commandLine.addValue(option, "file2.txt");
        assertEquals(List.of("file1.txt", "file2.txt"), commandLine.getValues(option));
        assertEquals(List.of(), commandLine.getValues(new Option("x", "example", false, "example option")));
    }

    @Test
    public void testSetDefaultValues() {
        // Test setting default values for an option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("f", "file", true, "file option");
        commandLine.setDefaultValues(option, List.of("file1.txt", "file2.txt"));
        assertEquals(List.of("file1.txt", "file2.txt"), commandLine.getValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        // Test adding a switch value to an option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("v", "verbose", false, "verbose option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        commandLine.addSwitch(option, false); // should throw exception
    }

    @Test
    public void testSetDefaultSwitch() {
        // Test setting default switch value for an option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("v", "verbose", false, "verbose option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        // Test adding a property value to the command line
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");
        assertEquals("value1", commandLine.getProperty("key1"));
        assertEquals("value2", commandLine.getProperty("key2"));
        assertNull(commandLine.getProperty("key3"));
    }

    @Test
    public void testLooksLikeOption() {
        // Test checking if an argument looks like an option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-f"));
        assertTrue(commandLine.looksLikeOption("--file"));
        assertFalse(commandLine.looksLikeOption("file.txt"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    // Implement the methods of the WriteableCommandLine interface
}