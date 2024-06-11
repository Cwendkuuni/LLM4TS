
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Argument();
        Object value = new Object();
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        assertFalse(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        Option retrievedOption = commandLine.getOption("trigger");
        assertNull(retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Argument();
        List<Object> defaultValues = new ArrayList<>();
        Object value = new Object();
        defaultValues.add(value);
        commandLine.addValue(option, value);
        List<Object> retrievedValues = commandLine.getValues(option, defaultValues);
        assertEquals(1, retrievedValues.size());
        assertEquals(value, retrievedValues.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Argument();
        Object value = new Object();
        commandLine.addValue(option, value);
        List<Object> retrievedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, retrievedValues.size());
        assertEquals(value, retrievedValues.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addSwitch(option, false);
        Boolean retrievedSwitch = commandLine.getSwitch(option, true);
        assertFalse(retrievedSwitch);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String property = "property";
        String value = "value";
        commandLine.addProperty(new PropertyOption(), property, value);
        String retrievedProperty = commandLine.getProperty(property);
        assertEquals(value, retrievedProperty);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String property = "property";
        String value = "value";
        commandLine.addProperty(new PropertyOption(), property, value);
        String retrievedProperty = commandLine.getProperty(property);
        assertEquals(value, retrievedProperty);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String property = "property";
        String defaultValue = "default";
        String retrievedProperty = commandLine.getProperty(new PropertyOption(), property, defaultValue);
        assertEquals(defaultValue, retrievedProperty);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
    }

    @Test
    public void testGetAllProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        Set<String> properties = commandLine.getProperties();
        assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String argument = "argument";
        commandLine.getNormalised().add(argument);
        String str = commandLine.toString();
        assertEquals(argument, str);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String trigger = "trigger";
        commandLine.addOption(option);
        Set<String> triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains(trigger));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        List<Object> defaults = new ArrayList<>();
        Object value = new Object();
        defaults.add(value);
        commandLine.setDefaultValues(option, defaults);
        List<Object> retrievedDefaults = commandLine.getValues(option, null);
        assertEquals(1, retrievedDefaults.size());
        assertEquals(value, retrievedDefaults.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean retrievedDefault = commandLine.getSwitch(option, null);
        assertEquals(defaultValue, retrievedDefault);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String argument = "argument";
        commandLine.getNormalised().add(argument);
        List<String> normalised = commandLine.getNormalised();
        assertTrue(normalised.contains(argument));
    }
}
