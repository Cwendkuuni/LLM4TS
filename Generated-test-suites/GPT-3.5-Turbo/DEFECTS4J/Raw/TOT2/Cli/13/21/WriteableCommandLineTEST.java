import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        commandLine.addOption(option);
        Object value = "testValue";
        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValue(option));
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        List<Object> defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);

        assertTrue(commandLine.getDefaultValues(option).containsAll(defaultValues));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitchValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String propertyName = "testProperty";
        String propertyValue = "testValue";

        commandLine.addProperty(propertyName, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(propertyName));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-o";

        assertTrue(commandLine.looksLikeOption(argument));

        String argument2 = "noOption";
        assertFalse(commandLine.looksLikeOption(argument2));
    }
}