import org.junit.*;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        
        // Add an option
        Option option = new Option("option", "o", false);
        commandLine.addOption(option);
        
        // Check if the option is added correctly
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("option"));
        assertEquals(option, commandLine.getOption("o"));
    }
    
    @Test
    public void testAddValue() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        
        // Add a value to an option
        Option option = new Option("option", "o", false);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        // Check if the values are added correctly
        assertEquals(2, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
    }
    
    @Test
    public void testAddSwitch() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        
        // Add a switch to an option
        Option option = new Option("option", "o", false);
        commandLine.addSwitch(option, true);
        
        // Check if the switch is added correctly
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetValues() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        
        // Add values to an option
        Option option = new Option("option", "o", false);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.setDefaultValue(option, Collections.singletonList("default1"));
        
        // Check if the values are returned correctly
        assertEquals(2, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
        
        // Check if the default values are returned correctly
        assertEquals(1, commandLine.getValues(option, Collections.singletonList("default2")).size());
        assertEquals("default1", commandLine.getValues(option, Collections.singletonList("default2")).get(0));
    }
    
    @Test
    public void testGetSwitch() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        
        // Add a switch to an option
        Option option = new Option("option", "o", false);
        commandLine.addSwitch(option, true);
        commandLine.setDefaultSwitch(option, false);
        
        // Check if the switch is returned correctly
        assertTrue(commandLine.getSwitch(option, null));
        
        // Check if the default switch is returned correctly
        assertFalse(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        
        // Add a property to an option
        Option option = new Option("option", "o", false);
        commandLine.addProperty(option, "property", "value");
        
        // Check if the property is returned correctly
        assertEquals("value", commandLine.getProperty(option, "property", null));
        
        // Check if the default value is returned correctly
        assertEquals("default", commandLine.getProperty(option, "nonexistent", "default"));
    }
    
    @Test
    public void testLookLikeOption() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        
        // Check if a trigger looks like an option
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        // Create a new WriteableCommandLineImpl with a root option
        Option rootOption = new Option("root");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.singletonList("arg1"));
        
        // Add an option
        Option option = new Option("option", "o", false);
        commandLine.addOption(option);
        
        // Check if the command line is converted to string correctly
        assertEquals("arg1", commandLine.toString());
    }
}