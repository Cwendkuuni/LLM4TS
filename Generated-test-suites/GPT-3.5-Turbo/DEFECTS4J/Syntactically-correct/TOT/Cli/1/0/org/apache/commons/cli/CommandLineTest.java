import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {
    
    // Test case for hasOption(String opt) method
    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));
        commandLine.addOption(new Option("b"));
        
        // Typical use case
        assertTrue(commandLine.hasOption("a"));
        
        // Edge case: empty option
        assertFalse(commandLine.hasOption(""));
        
        // Error scenario: option does not exist
        assertFalse(commandLine.hasOption("c"));
    }
    
    // Test case for hasOption(char opt) method
    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));
        commandLine.addOption(new Option("b"));
        
        // Typical use case
        assertTrue(commandLine.hasOption('a'));
        
        // Edge case: empty option
        assertFalse(commandLine.hasOption(' '));
        
        // Error scenario: option does not exist
        assertFalse(commandLine.hasOption('c'));
    }
    
    // Test case for getOptionObject(String opt) method
    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", true));
        commandLine.addOption(new Option("b", 10, true));
        
        // Typical use case
        assertEquals("Test", commandLine.getOptionObject("a"));
        
        // Edge case: option does not exist
        assertNull(commandLine.getOptionObject("c"));
    }
    
    // Test case for getOptionObject(char opt) method
    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", true));
        commandLine.addOption(new Option("b", 10, true));
        
        // Typical use case
        assertEquals("Test", commandLine.getOptionObject('a'));
        
        // Edge case: option does not exist
        assertNull(commandLine.getOptionObject('c'));
    }
    
    // Test case for getOptionValue(String opt) method
    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "value"));
        commandLine.addOption(new Option("b", true, "default"));
        
        // Typical use case
        assertEquals("value", commandLine.getOptionValue("a"));
        
        // Edge case: option does not exist
        assertNull(commandLine.getOptionValue("c"));
    }
    
    // Test case for getOptionValue(char opt) method
    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "value"));
        commandLine.addOption(new Option("b", true, "default"));
        
        // Typical use case
        assertEquals("value", commandLine.getOptionValue('a'));
        
        // Edge case: option does not exist
        assertNull(commandLine.getOptionValue('c'));
    }
    
    // Test case for getOptionValues(String opt) method
    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "value1", "value2"));
        commandLine.addOption(new Option("b", true, "default"));
        
        // Typical use case
        assertArrayEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues("a"));
        
        // Edge case: option does not exist
        assertNull(commandLine.getOptionValues("c"));
    }
    
    // Test case for getOptionValues(char opt) method
    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "value1", "value2"));
        commandLine.addOption(new Option("b", true, "default"));
        
        // Typical use case
        assertArrayEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues('a'));
        
        // Edge case: option does not exist
        assertNull(commandLine.getOptionValues('c'));
    }
    
    // Test case for getOptionValue(String opt, String defaultValue) method
    @Test
    public void testGetOptionValueDefault() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "value"));
        commandLine.addOption(new Option("b", true, "default"));
        
        // Typical use case
        assertEquals("value", commandLine.getOptionValue("a", "default"));
        
        // Edge case: option does not exist
        assertEquals("default", commandLine.getOptionValue("c", "default"));
    }
    
    // Test case for getOptionValue(char opt, String defaultValue) method
    @Test
    public void testGetOptionValueCharDefault() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "value"));
        commandLine.addOption(new Option("b", true, "default"));
        
        // Typical use case
        assertEquals("value", commandLine.getOptionValue('a', "default"));
        
        // Edge case: option does not exist
        assertEquals("default", commandLine.getOptionValue('c', "default"));
    }
    
    // Test case for getArgs() method
    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        // Typical use case
        assertArrayEquals(new String[] {"arg1", "arg2"}, commandLine.getArgs());
    }
    
    // Test case for getArgList() method
    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        // Typical use case
        assertEquals(2, commandLine.getArgList().size());
    }
    
    // Test case for addOption(Option opt) method
    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        
        // Typical use case
        commandLine.addOption(new Option("a"));
        assertTrue(commandLine.hasOption("a"));
        
        // Edge case: option with null key
        commandLine.addOption(new Option(null));
        assertTrue(commandLine.hasOption(""));
    }
    
    // Test case for iterator() method
    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));
        commandLine.addOption(new Option("b"));
        
        // Typical use case
        assertEquals(2, commandLine.iterator().size());
    }
    
    // Test case for getOptions() method
    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));
        commandLine.addOption(new Option("b"));
        
        // Typical use case
        assertEquals(2, commandLine.getOptions().length);
    }
}