import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import java.util.List;

public class WriteableCommandLineTest {
    @Test
    public void testAddOption() {
        // Test case 1: Add an option to the command line
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "option description");
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        // Test case 2: Add a value to an option in the command line
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "option description");
        Object value = "value";
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testSetDefaultValues() {
        // Test case 3: Set default values for an option in the command line
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "option description");
        List<Object> defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getDefaultValues(option);
        assertTrue(values.containsAll(defaultValues));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsException() {
        // Test case 4: Add a switch value to an option that has already been added
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "option description");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true); // This should not throw an exception
        commandLine.addSwitch(option, false); // This should throw an IllegalStateException
    }

    @Test
    public void testSetDefaultSwitch() {
        // Test case 5: Set default state for a switch in the command line
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "option description");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getDefaultSwitch(option);
        assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testAddProperty() {
        // Test case 6: Add a property value to the command line
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        String propertyValue = commandLine.getProperty(property);
        assertEquals(value, propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        // Test case 7: Test if an argument looks like an option trigger
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-option";
        boolean result = commandLine.looksLikeOption(argument);
        assertTrue(result);
    }
}