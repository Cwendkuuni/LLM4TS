import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {
    WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("verbose", "v", false, "enable verbose output");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("verbose"));
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("input", "i", true, "specify input file");
        commandLine.addOption(option);

        Object value = "input.txt";
        commandLine.addValue(option, value);

        assertTrue(commandLine.hasOption("input"));
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("names", "n", true, "specify names");
        commandLine.addOption(option);

        Object value1 = "John";
        Object value2 = "Doe";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        assertTrue(commandLine.hasOption("names"));
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("numbers", "num", true, "specify numbers");
        commandLine.addOption(option);

        List<Object> defaultValues = Arrays.asList(1, 2, 3);
        commandLine.setDefaultValues(option, defaultValues);

        assertTrue(commandLine.hasOption("numbers"));
        List<Object> values = commandLine.getValues(option);
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("debug", "d", true, "enable debug mode");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption("debug"));
        assertEquals(true, commandLine.getSwitch(option));

        // Adding the same switch value again should throw an IllegalStateException
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("help", "h", false, "display help message");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.hasOption("help"));
        assertEquals(true, commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String propertyName = "version";
        String propertyValue = "1.0";
        commandLine.addProperty(propertyName, propertyValue);

        String retrievedValue = commandLine.getProperty(propertyName);
        assertEquals(propertyValue, retrievedValue);
    }

    @Test
    public void testLooksLikeOption() {
        String argument1 = "-v";
        String argument2 = "--verbose";
        String argument3 = "input.txt";

        assertTrue(commandLine.looksLikeOption(argument1));
        assertTrue(commandLine.looksLikeOption(argument2));
        assertFalse(commandLine.looksLikeOption(argument3));
    }
}