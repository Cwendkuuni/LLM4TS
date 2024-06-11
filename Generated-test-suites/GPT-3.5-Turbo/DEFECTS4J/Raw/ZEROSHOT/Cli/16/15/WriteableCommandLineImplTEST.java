import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option rootOption;
    private List arguments;
    private ResourceHelper resourceHelper;
    
    @Before
    public void setUp() {
        rootOption = new Option("root", "Root Option", Option.NO_ARGUMENT, "");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        resourceHelper = ResourceHelper.getResourceHelper();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
        assertEquals(option, commandLine.getOption("option1"));
        assertEquals(option, commandLine.getOption("Option 1"));
    }
    
    @Test
    public void testAddValue() {
        Option argument = Argument.newArgument("arg1").setValueTypeName("String");
        String value1 = "value1";
        
        commandLine.addValue(argument, value1);
        
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals(value1, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        
        assertFalse(commandLine.hasOption(option));
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("option1"));
        assertEquals(option, commandLine.getOption("Option 1"));
    }
    
    @Test
    public void testGetValues() {
        Option option = Argument.newArgument("arg1").setValueTypeName("String");
        
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(values, commandLine.getValues(option, null));
        assertEquals(values, commandLine.getValues(option, Arrays.asList("default1")));
        assertEquals(values, commandLine.getValues(option, Arrays.asList("default1", "default2")));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = Argument.newArgument("arg1").setValueTypeName("String");
        
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        
        assertNull(commandLine.getSwitch(option, null));
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        String property = "property1";
        String value = "value1";
        
        assertNull(commandLine.getProperty(property));
        
        Option option = new PropertyOption();
        commandLine.addProperty(option, property, value);
        
        assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testAddProperty() {
        String property = "property1";
        String value = "value1";
        Option option = new PropertyOption();
        
        commandLine.addProperty(option, property, value);
        
        assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        String property = "property1";
        String value = "value1";
        
        assertNotNull(commandLine.getProperty(property, null, null));
        
        Option option = new PropertyOption();
        commandLine.addProperty(option, property, value);
        
        assertEquals(value, commandLine.getProperty(property, null, null));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        String property1 = "property1";
        String property2 = "property2";
        String value1 = "value1";
        String value2 = "value2";
        
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        
        Set<String> propertySet = commandLine.getProperties(option);
        assertEquals(2, propertySet.size());
        assertTrue(propertySet.contains(property1));
        assertTrue(propertySet.contains(property2));
    }
    
    @Test
    public void testGetPropertiesWithoutOption() {
        Set<String> propertySet = commandLine.getProperties();
        assertNotNull(propertySet);
        assertTrue(propertySet.isEmpty());
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "-option1";
        
        assertTrue(commandLine.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        commandLine.getNormalised().add("arg1");
        commandLine.getNormalised().add("arg2");
        
        assertEquals("arg1 arg2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        Option option2 = new Option("option2", "Option 2", Option.NO_ARGUMENT, "");
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        Option option2 = new Option("option2", "Option 2", Option.NO_ARGUMENT, "");
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = Argument.newArgument("arg1").setValueTypeName("String");
        List<String> defaultValues = Arrays.asList("default1", "default2");
        
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
        
        commandLine.setDefaultValues(option, null);
        assertEquals(Collections.EMPTY_LIST, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option1", "Option 1", Option.NO_ARGUMENT, "");
        
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
        
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");
        
        commandLine.getNormalised().addAll(normalised);
        
        assertEquals(normalised, commandLine.getNormalised());
    }
}