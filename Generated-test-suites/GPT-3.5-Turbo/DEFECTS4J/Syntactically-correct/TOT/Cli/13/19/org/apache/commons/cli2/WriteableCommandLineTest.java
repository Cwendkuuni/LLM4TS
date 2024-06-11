import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "An option");

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "An option");

        commandLine.addValue(option, "value");

        assertTrue(commandLine.hasValue("o"));
        assertEquals("value", commandLine.getValue("o"));
    }

    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "An option");

        commandLine.addValue(option, "value");

        assertTrue(commandLine.getArgumentValues(option).contains("value"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "An option");
        List<String> defaultValues = List.of("default1", "default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_duplicateSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "A switch");

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "A switch");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasSwitch("o"));
        assertTrue(commandLine.getSwitch("o"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "A switch");

        commandLine.setDefaultSwitch(option, true);

        assertEquals(true, commandLine.getDefaultSwitch("o"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addProperty("key", "value");

        assertTrue(commandLine.hasProperty("key"));
        assertEquals("value", commandLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}