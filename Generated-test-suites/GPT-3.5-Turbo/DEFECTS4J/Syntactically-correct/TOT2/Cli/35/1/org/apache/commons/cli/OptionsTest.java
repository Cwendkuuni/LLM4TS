import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an OptionGroup
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption_short() {
        // Test adding an option with only a short name
        Options options = new Options();
        options.addOption("a", "Option A");
        
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOption_long() {
        // Test adding an option with a long name
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option A");
        
        assertTrue(options.hasLongOption("alpha"));
    }

    @Test
    public void testGetOption() {
        // Test retrieving an option by name
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option A");
        
        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("alpha", option.getLongOpt());
    }

    @Test
    public void testHasOption() {
        // Test checking if an option exists
        Options options = new Options();
        options.addOption("a", "Option A");
        
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test getting the OptionGroup of an option
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "alpha", false, "Option A");
        
        group.addOption(option);
        
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option));
    }

}