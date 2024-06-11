import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an option group
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        // Test adding a basic option
        Options options = new Options();
        Option option = new Option("f", "file", true, "File option");
        
        options.addOption(option);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("file"));
        assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testGetOption() {
        // Test getting a specific option
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Verbose option");

        options.addOption(option);

        assertEquals(option, options.getOption("v"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting matching options
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "First option");
        Option option2 = new Option("p", "option2", false, "Second option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("option").size());
    }

    @Test
    public void testRemoveOption() {
        // Test removing an option
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");

        options.addOption(option);
        boolean optionExistsBeforeRemove = options.hasOption("t");

        options.removeOption("t");
        boolean optionExistsAfterRemove = options.hasOption("t");

        assertTrue(optionExistsBeforeRemove);
        assertFalse(optionExistsAfterRemove);
    }

}