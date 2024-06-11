import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        assertEquals(List.of("default1", "default2"), commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitches().containsKey(option));
        assertTrue(commandLine.getSwitches().get(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitches().containsKey(option));
        assertTrue(commandLine.getDefaultSwitches().get(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("key", "value");
        assertEquals("value", commandLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--long-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    // Edge cases and exception handling scenarios would be tested here

}