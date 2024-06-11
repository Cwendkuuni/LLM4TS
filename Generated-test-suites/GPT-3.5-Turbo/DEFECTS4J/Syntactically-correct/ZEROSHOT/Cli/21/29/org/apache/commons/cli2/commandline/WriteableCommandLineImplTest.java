import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Collections;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private List arguments;
    
    @Before
    public void setUp() {
        option = new Option("testOption");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(value, switchValue);
    }
    
    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        Object value = "testValue";
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.addValue(option, value);
        List retrievedValues = commandLine.getValues(option, defaultValues);
        assertTrue(retrievedValues.contains(value));
        assertTrue(retrievedValues.containsAll(defaultValues));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Object value = "testValue";
        commandLine.addValue(option, value);
        List retrievedValues = commandLine.getUndefaultedValues(option);
        assertTrue(retrievedValues.contains(value));
    }
    
    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
        switchValue = commandLine.getSwitch(option, null);
        assertNull(switchValue);
    }
    
    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        Set properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        String retrievedValue = commandLine.getProperty(option, property, defaultValue);
        assertEquals(defaultValue, retrievedValue);
    }
    
    @Test
    public void testGetProperties() {
        String property = "testProperty";
        commandLine.addProperty(option, property, "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
        assertEquals(2, properties.size());
    }
    
    @Test
    public void testGetPropertiesAll() {
        String property = "testProperty";
        commandLine.addProperty(option, property, "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties();
        assertTrue(properties.contains(property));
        assertEquals(2, properties.size());
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";
        boolean looksLikeOption = commandLine.looksLikeOption(trigger);
        assertTrue(looksLikeOption);
        trigger = "testTrigger";
        looksLikeOption = commandLine.looksLikeOption(trigger);
        assertFalse(looksLikeOption);
    }
    
    @Test
    public void testToString() {
        String argument1 = "arg1";
        String argument2 = "arg2";
        arguments.add(argument1);
        arguments.add(argument2);
        String expected = argument1 + " " + argument2;
        assertEquals(expected, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains(option.getPreferredName()));
    }
    
    @Test
    public void testSetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List retrievedDefaults = commandLine.getValues(option, null);
        assertEquals(defaults, retrievedDefaults);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }
    
    @Test
    public void testGetNormalised() {
        String argument1 = "arg1";
        String argument2 = "arg2";
        arguments.add(argument1);
        arguments.add(argument2);
        List normalised = commandLine.getNormalised();
        assertTrue(normalised.contains(argument1));
        assertTrue(normalised.contains(argument2));
    }
}