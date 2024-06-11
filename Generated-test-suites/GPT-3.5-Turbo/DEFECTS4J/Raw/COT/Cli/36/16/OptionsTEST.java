
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    // Test addOptionGroup method
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("h", "help", false, "Display help");
        Option option2 = new Option("v", "version", false, "Display version");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    // Test addOption method with short name and description only
    @Test
    public void testAddOptionShortNameDescription() {
        Options options = new Options();
        
        options.addOption("h", "Display help");
        
        assertTrue(options.hasOption("h"));
        assertNull(options.getLongOption("h"));
        assertNotNull(options.getOption("h"));
    }
    
    // Test addOption method with short name, hasArg, and description
    @Test
    public void testAddOptionShortNameHasArgDescription() {
        Options options = new Options();
        
        options.addOption("o", true, "Output file");
        
        assertTrue(options.hasOption("o"));
        assertNull(options.getLongOption("o"));
        assertNotNull(options.getOption("o"));
        assertTrue(options.getOption("o").hasArg());
    }
    
    // Test addOption method with short name, long name, hasArg, and description
    @Test
    public void testAddOptionShortNameLongNameHasArgDescription() {
        Options options = new Options();
        
        options.addOption("o", "output", true, "Output file");
        
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("output"));
        assertNotNull(options.getOption("o"));
        assertNotNull(options.getLongOption("output"));
        assertTrue(options.getOption("o").hasArg());
    }
    
    // Test getMatchingOptions method
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        options.addOption("o1", "output1", true, "Output file 1");
        options.addOption("o2", "output2", true, "Output file 2");
        options.addOption("o3", "output3", true, "Output file 3");
        
        assertEquals(0, options.getMatchingOptions("x").size());
        assertEquals(3, options.getMatchingOptions("o").size());
        assertEquals(2, options.getMatchingOptions("ou").size());
    }
    
    // Test hasOption method
    @Test
    public void testHasOption() {
        Options options = new Options();
        
        options.addOption("o", "output", true, "Output file");
        
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("output"));
        assertFalse(options.hasOption("x"));
    }
    
    // Test getOptionGroup method
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        Option option = new Option("v", "verbose", false, "Enable verbose logging");
        group.addOption(option);
        
        options.addOptionGroup(group);
        
        assertSame(group, options.getOptionGroup(option));
    }
    
    // Test toString method
    @Test
    public void testToString() {
        Options options = new Options();
        
        options.addOption("h", "help", false, "Display help");
        options.addOption("v", "verbose", false, "Enable verbose logging");
        
        String expected = "[ Options: [ short {h=Option(opt: h, longOpt: help, hasArg: false, description: Display help), v=Option(opt: v, longOpt: verbose, hasArg: false, description: Enable verbose logging)} ] [ long {help=Option(opt: h, longOpt: help, hasArg: false, description: Display help), verbose=Option(opt: v, longOpt: verbose, hasArg: false, description: Enable verbose logging)} ]";
        
        assertEquals(expected, options.toString());
    }
    
}
