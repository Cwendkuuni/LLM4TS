
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        
        // Perform
        commandLine.addOption(option);
        
        // Assert
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        String value = "testValue";
        
        // Perform
        commandLine.addValue(option, value);
        
        // Assert
        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        boolean value = true;
        
        // Perform
        commandLine.addSwitch(option, value);
        
        // Assert
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }
    
    @Test
    public void testHasOption() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        
        // Perform
        boolean hasOption = commandLine.hasOption(option);
        
        // Assert
        assertFalse(hasOption);
    }
    
    @Test
    public void testGetOption() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        commandLine.addOption(option);
        
        // Perform
        Option retrievedOption = commandLine.getOption("testOption");
        
        // Assert
        assertNotNull(retrievedOption);
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        
        // Perform
        List<String> values = commandLine.getValues(option, null);
        
        // Assert
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        String value = "testValue";
        commandLine.addValue(option, value);
        
        // Perform
        List<String> values = commandLine.getUndefaultedValues(option);
        
        // Assert
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        
        // Perform
        Boolean switchValue = commandLine.getSwitch(option, Boolean.TRUE);
        
        // Assert
        assertNotNull(switchValue);
        assertTrue(switchValue);
    }
    
    @Test
    public void testGetProperty() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        
        // Perform
        String retrievedValue = commandLine.getProperty(property);
        
        // Assert
        assertNotNull(retrievedValue);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testAddProperty() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        String property = "testProperty";
        String value = "testValue";
        
        // Perform
        commandLine.addProperty(option, property, value);
        
        // Assert
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertNotNull(retrievedValue);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testGetProperties() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        
        // Perform
        Set<String> properties = commandLine.getProperties(option);
        
        // Assert
        assertNotNull(properties);
        assertTrue(properties.contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String trigger = "-h";
        
        // Perform
        boolean looksLikeOption = commandLine.looksLikeOption(trigger);
        
        // Assert
        assertTrue(looksLikeOption);
    }
    
    @Test
    public void testToString() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String arg1 = "arg1";
        String arg2 = "arg2";
        commandLine.addArgument(arg1);
        commandLine.addArgument(arg2);
        
        // Perform
        String stringValue = commandLine.toString();
        
        // Assert
        assertEquals(arg1 + " " + arg2, stringValue);
    }
    
    @Test
    public void testGetOptions() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        // Perform
        List<Option> options = commandLine.getOptions();
        
        // Assert
        assertNotNull(options);
        assertEquals(0, options.size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        commandLine.addOption(option);
        
        // Perform
        Set<String> triggers = commandLine.getOptionTriggers();
        
        // Assert
        assertNotNull(triggers);
        assertEquals(1, triggers.size());
    }
    
    @Test
    public void testSetDefaultValues() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("defaultValue");
        
        // Perform
        commandLine.setDefaultValues(option, defaultValues);
        
        // Assert
        List<String> values = commandLine.getValues(option, null);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("testOption", "This is a test option");
        Boolean defaultSwitch = Boolean.TRUE;
        
        // Perform
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        // Assert
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertNotNull(switchValue);
        assertEquals(defaultSwitch, switchValue);
    }
    
    @Test
    public void testGetNormalised() {
        // Prepare
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String arg1 = "arg1";
        String arg2 = "arg2";
        commandLine.addArgument(arg1);
        commandLine.addArgument(arg2);
        
        // Perform
        List<String> normalised = commandLine.getNormalised();
        
        // Assert
        assertNotNull(normalised);
        assertEquals(2, normalised.size());
        assertEquals(arg1, normalised.get(0));
        assertEquals(arg2, normalised.get(1));
    }
}
