import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        assertTrue(commandLine.getValues(option).contains("testValue"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        assertTrue(commandLine.getValues(option).contains("default1"));
        assertTrue(commandLine.getValues(option).contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription", false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("testOption"));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription", false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription", false);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("testProperty", "testValue");
        assertEquals("testValue", commandLine.getProperty("testProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        // Implement all methods for testing
    }

}