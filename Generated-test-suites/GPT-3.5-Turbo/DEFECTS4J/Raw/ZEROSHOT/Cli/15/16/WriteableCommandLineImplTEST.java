import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setUp() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        Option option3 = new Option("option3");
        Argument arg1 = new Argument();
        Argument arg2 = new Argument();
        
        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(arg1, arg2));
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        Object value = "testValue";
        
        commandLine.addValue(option, value);
        
        List values = commandLine.getValues(option, null);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        boolean value = true;
        
        commandLine.addSwitch(option, value);
        
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertNotNull(switchValue);
        assertEquals(value, switchValue);
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        
        assertFalse(commandLine.hasOption(option));
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        
        assertNull(commandLine.getOption(option.getPreferredName()));
        
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        List defaultValues = Arrays.asList("defaultValue");
        
        List values = commandLine.getValues(option, defaultValues);
        assertNotNull(values);
        assertEquals(defaultValues, values);
        
        Object value = "testValue";
        commandLine.addValue(option, value);
        
        values = commandLine.getValues(option, defaultValues);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        
        List values = commandLine.getUndefaultedValues(option);
        assertNotNull(values);
        assertTrue(values.isEmpty());
        
        Object value = "testValue";
        commandLine.addValue(option, value);
        
        values = commandLine.getUndefaultedValues(option);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        Boolean defaultValue = true;
        
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        assertNotNull(switchValue);
        assertEquals(defaultValue, switchValue);
        
        boolean value = false;
        commandLine.addSwitch(option, value);
        
        switchValue = commandLine.getSwitch(option, defaultValue);
        assertNotNull(switchValue);
        assertEquals(value, switchValue);
    }
    
    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        Option option = new Option("testOption");
        
        assertNull(commandLine.getProperty(property));
        
        commandLine.addProperty(option, property, value);
        
        assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        Option option = new Option("testOption");
        
        commandLine.addProperty(option, property, value);
        
        assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        Option option = new Option("testOption");
        
        assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
        
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        
        assertEquals(value, commandLine.getProperty(option, property, defaultValue));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("testOption");
        
        assertTrue(commandLine.getProperties().isEmpty());
        
        String property1 = "testProperty1";
        String value1 = "testValue1";
        commandLine.addProperty(option, property1, value1);
        
        Set properties = commandLine.getProperties(option);
        assertNotNull(properties);
        assertEquals(1, properties.size());
        assertTrue(properties.contains(property1));
        
        String property2 = "testProperty2";
        String value2 = "testValue2";
        commandLine.addProperty(option, property2, value2);
        
        properties = commandLine.getProperties(option);
        assertNotNull(properties);
        assertEquals(2, properties.size());
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "-testTrigger";
        assertTrue(commandLine.looksLikeOption(trigger));
        
        trigger = "--testTrigger";
        assertTrue(commandLine.looksLikeOption(trigger));
        
        trigger = "testTrigger";
        assertFalse(commandLine.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        String arg1 = "argument1";
        String arg2 = "argument2";
        
        assertEquals(arg1 + " " + arg2, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertNotNull(options);
        assertEquals(3, options.size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        assertNotNull(triggers);
        assertEquals(3, triggers.size());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List defaultValues = Arrays.asList("defaultValue");
        
        commandLine.setDefaultValues(option, defaultValues);
        
        List values = commandLine.getValues(option, null);
        assertNotNull(values);
        assertEquals(defaultValues, values);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        Boolean defaultSwitch = true;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertNotNull(switchValue);
        assertEquals(defaultSwitch, switchValue);
    }
    
    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertNotNull(normalised);
        assertEquals(2, normalised.size());
    }
}