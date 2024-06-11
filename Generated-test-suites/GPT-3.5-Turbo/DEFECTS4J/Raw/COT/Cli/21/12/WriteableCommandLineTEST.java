import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.options.contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.values.contains(value) && commandLine.optionValues.containsKey(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        List defaultValue = new List();
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(defaultValue, values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        List defaultValues = new List();
        commandLine.setDefaultValues(option, defaultValues);
        assertTrue(commandLine.defaultValues.contains(option) && commandLine.defaultValueMap.containsKey(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        boolean value = true;
        commandLine.addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.defaultSwitches.containsKey(option) && commandLine.defaultSwitches.get(option) == defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.properties.containsKey(option) && commandLine.properties.get(option).containsKey(property) && commandLine.properties.get(option).get(property) == value);
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertTrue(commandLine.defaultProperties.containsKey(property) && commandLine.defaultProperties.get(property) == value);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-option";
        assertTrue(commandLine.looksLikeOption(argument));
    }
}