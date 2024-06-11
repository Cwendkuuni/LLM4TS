import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an option group
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "apple", false, "Apple option");
        group.addOption(option);
        options.addOptionGroup(group);
        
        assertEquals(1, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        // Test adding an option
        Options options = new Options();
        
        options.addOption("h", "help", false, "Help option");
        
        assertTrue(options.hasOption("h"));
    }

    @Test
    public void testGetOption() {
        // Test getting an option
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Verbose option");
        options.addOption(option);
        
        assertEquals(option, options.getOption("v"));
    }

    @Test
    public void testHasOption() {
        // Test checking if an option exists
        Options options = new Options();

        options.addOption("f", "force", false, "Force option");
        
        assertTrue(options.hasOption("f"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test getting the option group
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "output", false, "Output option");
        group.addOption(option);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option));
    }
}