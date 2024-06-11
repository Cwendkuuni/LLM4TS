import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        // Test adding an OptionGroup
        OptionGroup group = new OptionGroup("testGroup");
        Option option1 = new Option("a", "alpha", false, "Alpha option");
        group.addOption(option1);
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        // Test adding an option with short name and no argument
        options.addOption("h", "Help option");
        assertTrue(options.hasOption("h"));
        
        // Test adding an option with short and long name, requiring an argument
        options.addOption("v", "verbose", true, "Verbose option");
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("verbose"));
    }

    @Test
    public void testGetOption() {
        // Test retrieving an option by short name
        Option option = new Option("o", "output", true, "Output option");
        options.addOption(option);
        assertEquals(option, options.getOption("o"));

        // Test retrieving an option by long name
        assertEquals(option, options.getOption("output"));
    }

    @Test
    public void testMatchingOptions() {
        // Test getting matching options for partial name
        options.addOption("f", "force", false, "Force option");
        options.addOption("f1", "file", true, "File option");
        
        assertEquals(2, options.getMatchingOptions("f").size());
        assertEquals(1, options.getMatchingOptions("f1").size());
    }

    @Test
    public void testHasOption() {
        // Test checking if option exists by short name
        options.addOption("c", "count", false, "Count option");
        assertTrue(options.hasOption("c"));

        // Test checking if option exists by long name
        options.addOption("o", "output", true, "Output option");
        assertTrue(options.hasOption("output"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test getting option group for an option
        OptionGroup group = new OptionGroup("testGroup");
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }
    
    // Additional test methods for edge cases and exceptions can be added here

}