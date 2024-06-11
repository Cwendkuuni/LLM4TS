import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValue("test"));
    }

    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> argumentValues = commandLine.getArgumentValues(option);
        assertTrue(argumentValues.contains("value"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        List<String> defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-test"));
    }
}