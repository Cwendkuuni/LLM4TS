import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValue(option));
    }

    @Test
    public void testDefaultValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        List defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "prop";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-test";
        assertTrue(commandLine.looksLikeOption(argument));
    }
}