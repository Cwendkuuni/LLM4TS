import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        // Test addOption method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        // Test addValue method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Test getUndefaultedValues method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        // Test setDefaultValues method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("default1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("default2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        // Test addSwitch method with IllegalStateException
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitchNoException() {
        // Test addSwitch method without IllegalStateException
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertFalse(commandLine.addSwitch(option, false));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Test setDefaultSwitch method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.isSwitchSet(option));
    }

    @Test
    public void testAddProperty() {
        // Test addProperty method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option, "property1"));
    }

    @Test
    public void testAddDefaultProperty() {
        // Test addDefaultProperty method
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty(null, "property1"));
    }

    @Test
    public void testLooksLikeOption() {
        // Test looksLikeOption method
        WriteableCommandLine commandLine = new CommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-test"));
        assertTrue(commandLine.looksLikeOption("--test"));
    }

    @Test
    public void testGetCurrentOption() {
        // Test getCurrentOption method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        // Test setCurrentOption method
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("test", "test option");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }
}