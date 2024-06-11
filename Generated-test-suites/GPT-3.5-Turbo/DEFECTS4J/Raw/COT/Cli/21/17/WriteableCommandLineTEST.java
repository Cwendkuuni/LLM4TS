import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("help", "Displays help information");
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "Specifies a name option");
        Object value = "John";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("numbers", "Specifies a list of numbers");
        commandLine.addValue(option, 1);
        commandLine.addValue(option, 2);
        commandLine.addValue(option, 3);
        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(3, undefaultedValues.size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("colors", "Specifies a list of colors");
        List<String> defaultValues = List.of("red", "green", "blue");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("verbose", "Enables verbose mode");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("debug", "Enables debug mode");
        commandLine.setDefaultSwitch(option, true);
        boolean defaultSwitch = commandLine.getSwitch(option);
        assertTrue(defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("config", "Specifies a configuration file");
        String property = "path";
        String value = "/path/to/config.properties";
        commandLine.addProperty(option, property, value);
        String propertyValue = commandLine.getProperty(option, property);
        assertEquals(value, propertyValue);
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "logging.level";
        String value = "info";
        commandLine.addProperty(property, value);
        String propertyValue = commandLine.getProperty(property);
        assertEquals(value, propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-h";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("verbose", "Enables verbose mode");
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        assertEquals(option, currentOption);
    }

}