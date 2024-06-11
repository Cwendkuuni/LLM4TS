import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;
    private PropertyOption propertyOption;
    
    @Before
    public void setUp() {
        option = new Option();
        argument = new Argument();
        propertyOption = new PropertyOption();
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertNotNull(commandLine.getOptionTriggers());
    }
    
    @Test
    public void testAddValue() {
        commandLine.addValue(argument, "value1");
        List<Object> values = (List<Object>) commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean bool = commandLine.getSwitch(option, null);
        assertEquals(true, bool);
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("trigger"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }
    
    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        List<String> values = (List<String>) commandLine.getValues(option, defaultValues);
        assertEquals(defaultValues, values);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(argument, "value1");
        List<String> values = (List<String>) commandLine.getUndefaultedValues(argument);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));
        assertEquals("value", commandLine.getProperty(propertyOption, "property", "value"));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty(propertyOption, "property", "value");
        Set<String> properties = commandLine.getProperties(propertyOption);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property"));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        assertEquals("defaultValue", commandLine.getProperty(propertyOption, "property", "defaultValue"));
    }
    
    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties();
        assertNotNull(properties);
    }
    
    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("trigger"));
        option.addHandler(null);
        assertTrue(commandLine.looksLikeOption("trigger"));
    }
    
    @Test
    public void testToString() {
        commandLine.addValue(argument, "value1");
        assertEquals("value1", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertNotNull(options);
    }
    
    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertNotNull(optionTriggers);
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertNotNull(normalised);
    }
}