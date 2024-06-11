import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private List arguments;
    
    @Before
    public void setUp() {
        option = new Option("testOption", "Test Option");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Object value = "testValue";
        commandLine.addValue(option, value);
        List valueList = (List) commandLine.getValues(option, null);
        assertEquals(value, valueList.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
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
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        commandLine.setDefaultValues(option, defaults);
        commandLine.addValue(option, value);
        List valueList = commandLine.getValues(option, null);
        assertEquals(defaults, valueList);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Object value = "testValue";
        commandLine.addValue(option, value);
        List valueList = commandLine.getUndefaultedValues(option);
        assertEquals(value, valueList.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
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
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testGetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        commandLine.setDefaultValues(option, defaults);
        List valueList = commandLine.getValues(option, null);
        assertEquals(defaults, valueList);
    }
    
    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties();
        assertNotNull(properties);
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "testTrigger";
        boolean result = commandLine.looksLikeOption(trigger);
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        arguments.add(arg1);
        arguments.add(arg2);
        String result = commandLine.toString();
        assertEquals(arg1 + " " + arg2, result);
    }
    
    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertNotNull(options);
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        Set triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains(option.getPreferredName()));
    }
    
    @Test
    public void testSetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        commandLine.setDefaultValues(option, defaults);
        List valueList = commandLine.getValues(option, null);
        assertEquals(defaults, valueList);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }
    
    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertNotNull(normalised);
    }
}