import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an OptionGroup to Options
        // Edge case: OptionGroup is required
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("a", "first", false, "First option"));
        optionGroup.addOption(new Option("b", "second", false, "Second option"));
        optionGroup.setRequired(true);
        
        options.addOptionGroup(optionGroup);
        assertTrue(options.getRequiredOptions().contains(optionGroup));
    }

    @Test
    public void testAddOption() {
        // Test adding an Option to Options
        // Typical case: Adding an option with short and long name
        Options options = new Options();
        Option option = new Option("f", "file", true, "File option");
        
        options.addOption(option);
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testRetrieveOption() {
        // Test retrieving an Option from Options
        // Error scenario: Option does not exist
        Options options = new Options();
        Option option = options.getOption("test");
        
        assertNull(option);
    }

    @Test
    public void testAddShortOption() {
        // Test adding a short-name Option to Options
        // Edge case: Option does not have an argument
        Options options = new Options();
        options.addOption("v", "Verbose output");
        
        assertTrue(options.hasOption("v"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting matching options from Options
        // Typical case: Partial name match
        Options options = new Options();
        options.addOption("v", "verbose", true, "Verbose output");
        options.addOption("f", "file", true, "File option");
        options.addOption("o", "output", true, "Output file");
        
        assertEquals(2, options.getMatchingOptions("f").size());
        assertTrue(options.getMatchingOptions("fi").contains("file"));
    }

    @Test
    public void testHasLongOption() {
        // Test checking if Options has a long option
        // Error scenario: Long option does not exist
        Options options = new Options();
        assertFalse(options.hasLongOption("verbose"));
    }

    @Test
    public void testHasShortOption() {
        // Test checking if Options has a short option
        // Typical case: Short option exists
        Options options = new Options();
        options.addOption("o", "output", true, "Output file");
        
        assertTrue(options.hasShortOption("o"));
    }
}