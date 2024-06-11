import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option");

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("option"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option");

        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> argumentValues = commandLine.getArgumentValues(option);

        assertEquals(2, argumentValues.size());
        assertTrue(argumentValues.contains("value1"));
        assertTrue(argumentValues.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option");
        List<String> defaultValues = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option");

        commandLine.setDefaultSwitch(option, true);

        assertEquals(true, commandLine.getDefaultSwitch(option));
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
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}

