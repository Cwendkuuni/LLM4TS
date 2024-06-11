import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an OptionGroup
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("opt1", "desc1"));
        group.addOption(new Option("opt2", "desc2"));
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption_short() {
        // Test adding a short option without argument
        Options options = new Options();
        options.addOption("h", "Help");
        
        assertTrue(options.hasOption("h"));
        assertFalse(options.hasLongOption("Help"));
    }

    @Test
    public void testAddOption_long() {
        // Test adding a long option with argument
        Options options = new Options();
        options.addOption("verbose", "v", true, "Verbose output");
        
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasLongOption("verbose"));
    }

    @Test
    public void testGetOptions() {
        // Test retrieving all options
        Options options = new Options();
        options.addOption("h", "Help");
        options.addOption("v", false, "Version");
        
        assertEquals(2, options.getOptions().size());
    }

    // Add more test cases for other methods in the Options class

}