import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Create Options object and OptionGroup
        Options options = new Options();
        OptionGroup group = new OptionGroup();

        // Add options to OptionGroup
        Option option1 = new Option("a", "option1", false, "Option 1");
        group.addOption(option1);

        // Add OptionGroup to Options
        options.addOptionGroup(group);

        // Assertion
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        // Create Options object
        Options options = new Options();

        // Add option
        Option option = new Option("a", "option", true, "This is an option");
        options.addOption(option);

        // Assertion
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Create Options object
        Options options = new Options();

        // Add options
        Option option1 = new Option("aa", "option1", false, "Option 1");
        Option option2 = new Option("ab", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        // Get matching options
        List<String> matchingOptions = options.getMatchingOptions("a");

        // Assertion
        assertTrue(matchingOptions.contains("aa"));
        assertTrue(matchingOptions.contains("ab"));
    }

    @Test
    public void testHasOption() {
        // Create Options object
        Options options = new Options();

        // Add option
        Option option = new Option("a", "option", true, "This is an option");
        options.addOption(option);

        // Assertion
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        // Create Options object
        Options options = new Options();

        // Add option
        Option option = new Option("a", "option", true, "This is an option");
        options.addOption(option);

        // Assertion
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("longOption"));
    }

    @Test
    public void testHasShortOption() {
        // Create Options object
        Options options = new Options();

        // Add option
        Option option = new Option("a", "option", true, "This is an option");
        options.addOption(option);

        // Assertion
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        // Create Options object and OptionGroup
        Options options = new Options();
        OptionGroup group = new OptionGroup();

        // Add options to OptionGroup
        Option option1 = new Option("a", "option1", false, "Option 1");
        group.addOption(option1);

        // Add OptionGroup to Options
        options.addOptionGroup(group);

        // Assertion
        assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        // Create Options object
        Options options = new Options();

        // Add options
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        // Assertion
        assertEquals("[ Options: [ short {a=Option 1, b=Option 2} ] [ long {} ]", options.toString());
    }
}