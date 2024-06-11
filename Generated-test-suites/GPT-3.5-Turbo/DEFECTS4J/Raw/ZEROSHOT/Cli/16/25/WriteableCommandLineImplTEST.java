import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setUp() {
        Option rootOption = new Option("root", "Root option");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        
        Assert.assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        argument.setPreferredName("arg1");
        
        Object value = "value";
        commandLine.addValue(argument, value);
        
        List values = commandLine.getValues(argument, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "Test option");
        boolean value = true;
        commandLine.addSwitch(option, value);
        
        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals(value, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        
        commandLine.addProperty(property, value);
        
        Assert.assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("test", "Test option");
        String property = "property";
        String value = "value";
        
        commandLine.addProperty(option, property, value);
        
        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test", "Test option");
        
        commandLine.addOption(option);
        
        Assert.assertEquals(option, commandLine.getOption("test"));
    }
    
    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        argument.setPreferredName("arg1");
        
        Object value1 = "value1";
        Object value2 = "value2";
        
        commandLine.addValue(argument, value1);
        commandLine.addValue(argument, value2);
        
        List values = commandLine.getValues(argument, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument();
        argument.setPreferredName("arg1");
        
        Object value1 = "value1";
        Object value2 = "value2";
        
        commandLine.addValue(argument, value1);
        commandLine.addValue(argument, value2);
        
        List values = commandLine.getUndefaultedValues(argument);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "Test option");
        
        boolean value = true;
        
        commandLine.addSwitch(option, value);
        
        Assert.assertEquals(value, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "property";
        String defaultValue = "defaultValue";
        
        Assert.assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("test", "Test option");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        
        List properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains(property1));
        Assert.assertTrue(properties.contains(property2));
    }
    
    @Test
    public void testGetPropertiesWithDefaultOption() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        
        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);
        
        List properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains(property1));
        Assert.assertTrue(properties.contains(property2));
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "--test";
        
        Assert.assertTrue(commandLine.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        String expected = "arg1";
        
        Argument argument = new Argument();
        argument.setPreferredName("arg1");
        
        commandLine.addValue(argument, expected);
        
        Assert.assertEquals(expected, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option = new Option("test", "Test option");
        
        commandLine.addOption(option);
        
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test", "Test option");
        
        commandLine.addOption(option);
        
        Assert.assertEquals(1, commandLine.getOptionTriggers().size());
        Assert.assertTrue(commandLine.getOptionTriggers().contains("test"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "Test option");
        
        List defaults = new ArrayList();
        defaults.add("default");
        
        commandLine.setDefaultValues(option, defaults);
        
        Assert.assertEquals(defaults, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "Test option");
        
        Boolean defaultSwitch = true;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        Assert.assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        List arguments = new ArrayList();
        arguments.add("arg1");
        
        Option rootOption = new Option("root", "Root option");
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(rootOption, arguments);
        
        Assert.assertEquals(arguments, commandLineImpl.getNormalised());
    }
}