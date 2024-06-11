import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        
        // Typical use case
        cmd.addOption(new Option("a", "apple", true, "This is an apple"));
        assertTrue(cmd.hasOption("a"));
        
        // Edge case: option not present
        assertFalse(cmd.hasOption("b"));
        
        // Error scenario: null input
        assertFalse(cmd.hasOption(null));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        
        // Typical use case
        cmd.addOption(new Option("f", "file", true, "Path to the file"));
        cmd.addOption(new Option("v", "verbose", false, "Enable verbose mode"));
        cmd.addOption(new Option("n", "number", true, "Number of items"));
        cmd.addOption(new Option("h", "help", false, "Show help"));
        cmd.addOption(new Option("e", "extra", true, "Extra option"));
        
        cmd.addArg("input.txt");
        cmd.addArg("output.txt");
        
        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertNull(cmd.getOptionValue("v"));
        assertNull(cmd.getOptionValue("n"));
        assertNull(cmd.getOptionValue("h"));
        assertNull(cmd.getOptionValue("e"));
        
        // Edge case: Option not present
        assertNull(cmd.getOptionValue("x"));
        
        // Error scenario: null input
        assertNull(cmd.getOptionValue(null));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        
        // Typical use case
        cmd.addOption(new Option("o", "output", true, "Output file"));
        cmd.addOption(new Option("v", "verbose", false, "Enable verbose mode"));
        
        cmd.addArg("file1.txt");
        cmd.addArg("file2.txt");
        
        String[] values = cmd.getOptionValues("o");
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("file1.txt", values[0]);
        
        values = cmd.getOptionValues("v");
        assertNull(values);
        
        // Edge case: Option not present
        assertNull(cmd.getOptionValues("x"));
        
        // Error scenario: null input
        assertNull(cmd.getOptionValues(null));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        
        // Typical use case
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        
        String[] args = cmd.getArgs();
        assertNotNull(args);
        assertEquals(3, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        assertEquals("arg3", args[2]);
        
        // Edge case: No args
        cmd = new CommandLine();
        args = cmd.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }
    
    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        
        // Typical use case
        Option option = new Option("o", "output", true, "Output file");
        cmd.addOption(option);
        
        Option[] options = cmd.getOptions();
        assertNotNull(options);
        assertEquals(1, options.length);
        assertEquals(option, options[0]);
        
        // Edge case: Adding duplicate option
        cmd.addOption(option);
        options = cmd.getOptions();
        assertNotNull(options);
        assertEquals(1, options.length);
        
        // Error scenario: Adding null option
        cmd.addOption(null);
        options = cmd.getOptions();
        assertNotNull(options);
        assertEquals(1, options.length);
    }

}