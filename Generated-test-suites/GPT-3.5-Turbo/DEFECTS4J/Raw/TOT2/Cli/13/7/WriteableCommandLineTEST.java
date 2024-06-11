import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value");
        assertEquals("value", cmdLine.getValue(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmdLine.setDefaultValues(option, List.of("default1", "default2"));
        assertEquals(List.of("default1", "default2"), cmdLine.getValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.hasSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option", true, "");
        cmdLine.setDefaultSwitch(option, false);
        assertFalse(cmdLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        cmdLine.addProperty("key", "value");
        assertEquals("value", cmdLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        assertTrue(cmdLine.looksLikeOption("-test"));
    }
}