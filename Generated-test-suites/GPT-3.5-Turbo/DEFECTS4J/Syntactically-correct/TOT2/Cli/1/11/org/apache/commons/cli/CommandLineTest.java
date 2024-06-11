import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        
        assertFalse(commandLine.hasOption("test"));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionValue("test"));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionValues("test"));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionObject("test"));
    }
    
    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        
        assertNotNull(commandLine.getArgList());
    }
    
    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        
        commandLine.addArg("test");
        assertEquals(1, commandLine.getArgList().size());
    }
    
    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("test", "test option");
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption("test"));
    }
    
    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("test", "test option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.iterator().hasNext());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("test", "test option");
        commandLine.addOption(option);
        
        assertNotNull(commandLine.getOptions());
    }
}