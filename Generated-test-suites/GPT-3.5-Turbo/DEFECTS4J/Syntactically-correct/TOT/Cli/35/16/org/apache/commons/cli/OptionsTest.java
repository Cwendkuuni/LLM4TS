import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    // Testing addOptionGroup() method
    public void testAddOptionGroup() {
        Options options = new Options();

        // Create an option group with two options
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("a", false, "Option A"));
        optionGroup.addOption(new Option("b", false, "Option B"));

        // Add the option group to the options
        options.addOptionGroup(optionGroup);

        // Verify that the option group is added correctly
        assertEquals(1, options.getOptionGroups().size());
        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    // Testing addOption() method with short name only
    public void testAddOptionWithShortNameOnly() {
        Options options = new Options();

        // Add an option with the short name only
        options.addOption("a", "Option A");

        // Verify that the option is added correctly
        assertTrue(options.hasOption("a"));

        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasLongOpt());
    }

    @Test
    // Testing addOption() method with short name and hasArg flag
    public void testAddOptionWithArg() {
        Options options = new Options();

        // Add an option that requires an argument
        options.addOption("a", true, "Option A");

        // Verify that the option is added correctly
        assertTrue(options.hasOption("a"));

        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    // Testing addOption() method with short and long names
    public void testAddOptionWithShortAndLongNames() {
        Options options = new Options();

        // Add an option with short and long names
        options.addOption("a", "optionA", false, "Option A");

        // Verify that the option is added correctly
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));

        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("optionA", option.getLongOpt());
        assertEquals("Option A", option.getDescription());
    }

    @Test
    // Testing getOptions() method
    public void testGetOptions() {
        Options options = new Options();

        // Add multiple options to the options
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        // Verify that the options are retrieved correctly
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().stream().anyMatch(option ->
            option.getOpt().equals("a") && option.getDescription().equals("Option A")
        ));
        assertTrue(options.getOptions().stream().anyMatch(option ->
            option.getOpt().equals("b") && option.getDescription().equals("Option B")
        ));
    }

    @Test
    // Testing getRequiredOptions() method
    public void testGetRequiredOptions() {
        Options options = new Options();

        // Add multiple required options to the options
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        // Verify that the required options are retrieved correctly
        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("a"));
        assertTrue(options.getRequiredOptions().contains("b"));
    }

    @Test
    // Testing getOption() method
    public void testGetOption() {
        Options options = new Options();

        // Add an option to the options
        options.addOption("a", false, "Option A");

        // Verify that the option is retrieved correctly
        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getDescription());
    }

    @Test
    // Testing getMatchingOptions() method
    public void testGetMatchingOptions() {
        Options options = new Options();

        // Add multiple options to the options with long names
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        // Verify that the matching options are retrieved correctly
        assertEquals(2, options.getMatchingOptions("option").size());
        assertTrue(options.getMatchingOptions("option").contains("optionA"));
        assertTrue(options.getMatchingOptions("option").contains("optionB"));
    }

    @Test
    // Testing hasOption() method
    public void testHasOption() {
        Options options = new Options();

        // Add an option to the options
        options.addOption("a", false, "Option A");

        // Verify that the option is identified correctly
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    // Testing hasLongOption() method
    public void testHasLongOption() {
        Options options = new Options();

        // Add an option to the options with long name
        options.addOption("a", "optionA", false, "Option A");

        // Verify that the option is identified correctly
        assertTrue(options.hasLongOption("optionA"));
        assertFalse(options.hasLongOption("optionB"));
    }

    @Test
    // Testing hasShortOption() method
    public void testHasShortOption() {
        Options options = new Options();

        // Add an option to the options with short name
        options.addOption("a", "optionA", false, "Option A");

        // Verify that the option is identified correctly
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    // Testing getOptionGroup() method
    public void testGetOptionGroup() {
        Options options = new Options();

        // Create an option group with two options
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", false, "Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);

        // Add the option group to the options
        options.addOptionGroup(optionGroup);

        // Verify that the option group is retrieved correctly
        assertEquals(optionGroup, options.getOptionGroup(optionA));
        assertEquals(optionGroup, options.getOptionGroup(optionB));
    }

    @Test
    // Testing toString() method
    public void testToString() {
        Options options = new Options();

        // Add multiple options to the options
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        // Verify that the options are represented correctly as a string
        assertEquals("[ Options: [ short {a=Option A, b=Option B} ] [ long {optionA=null, optionB=null} ]", options.toString());
    }
}