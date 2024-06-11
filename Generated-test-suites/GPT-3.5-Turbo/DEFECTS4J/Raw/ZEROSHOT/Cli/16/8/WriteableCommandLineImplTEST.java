import static org.junit.Assert.*;
import org.apache.commons.cli2.option.DefaultOptionBuilder;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.valueOf(value), switchValue);
    }
    
    @Test
    public void testHasOption() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Option retrievedOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean retrievedSwitchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.valueOf(value), retrievedSwitchValue);
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testAddProperty() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String property = "testProperty";
        String defaultValue = "defaultValue";
        String retrievedValue = commandLine.getProperty(property, defaultValue);
        assertEquals(defaultValue, retrievedValue);
    }
    
    @Test
    public void testGetProperties() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String property1 = "testProperty1";
        String value1 = "testValue1";
        String property2 = "testProperty2";
        String value2 = "testValue2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        Set properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }
    
    @Test
    public void testGetPropertiesWithoutOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        Set properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        String optionTrigger = "-test";
        assertTrue(commandLine.looksLikeOption(optionTrigger));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        String expected = "arg1 arg2";
        String actual = commandLine.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = DefaultOptionBuilder.buildOption("test1");
        Option option2 = DefaultOptionBuilder.buildOption("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = DefaultOptionBuilder.buildOption("test1");
        Option option2 = DefaultOptionBuilder.buildOption("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains(option1.getPreferredName()));
        assertTrue(triggers.contains(option2.getPreferredName()));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        List defaults = Arrays.asList("value1", "value2");
        commandLine.setDefaultValue(option, defaults);
        List defaultValues = commandLine.getValues(option, null);
        assertEquals(defaults, defaultValues);
        commandLine.setDefaultValue(option, null);
        defaultValues = commandLine.getValues(option, null);
        assertNull(defaultValues);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Boolean defaultSwitch = true;
        commandLine.setDefaultValue(option, defaultSwitch);
        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, retrievedDefaultSwitch);
        commandLine.setDefaultValue(option, null);
        retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        assertNull(retrievedDefaultSwitch);
    }
    
    @Test
    public void testGetNormalised() {
        Option option = DefaultOptionBuilder.buildOption("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}