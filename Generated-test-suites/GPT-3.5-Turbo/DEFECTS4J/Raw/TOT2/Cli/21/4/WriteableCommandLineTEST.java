import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValue("test"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        assertEquals(List.of("default1", "default2"), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option", false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option", false);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty("test", "property1"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "test option");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }
}