import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    // Test case for addOptionGroup(OptionGroup group)
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOptionGroup(group));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }

    // Test case for addOption(String opt, String description)
    @Test
    public void testAddOptionWithoutArgument() {
        options.addOption("a", "apple", "Apple option");

        assertTrue(options.hasOption("a"));
        assertEquals("Apple option", options.getOption("a").getDescription());
    }

    // Test case for addOption(String opt, boolean hasArg, String description)
    @Test
    public void testAddOptionWithArgument() {
        options.addOption("f", true, "File option");

        assertTrue(options.hasOption("f"));
        assertEquals("File option", options.getOption("f").getDescription());
        assertTrue(options.getOption("f").hasArg());
    }

    // Test case for addOption(String opt, String longOpt, boolean hasArg, String description)
    @Test
    public void testAddOptionWithLongName() {
        options.addOption("f", "file", true, "File option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertEquals("File option", options.getOption("f").getDescription());
        assertEquals("File option", options.getOption("file").getDescription());
        assertTrue(options.getOption("file").hasArg());
    }

    // Test case for getOptionGroups()
    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup();
        OptionGroup group2 = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        group1.addOption(option1);
        group2.addOption(option2);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertEquals(2, options.getOptionGroups().size());
        assertTrue(options.getOptionGroups().contains(group1));
        assertTrue(options.getOptionGroups().contains(group2));
    }

    // Test case for hasOption(String opt)
    @Test
    public void testHasOption() {
        options.addOption("a", "apple");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertFalse(options.hasOption("b"));
    }

    // Test case for hasLongOption(String opt)
    @Test
    public void testHasLongOption() {
        options.addOption("a", "apple");

        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasLongOption("a"));
    }

    // Test case for hasShortOption(String opt)
    @Test
    public void testHasShortOption() {
        options.addOption("a", "apple");

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("apple"));
    }

    // Test case for getOption(String opt)
    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "apple");
        Option option2 = new Option("b", "banana");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertNull(options.getOption("c"));
    }

    // Test case for getMatchingOptions(String opt)
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple");
        Option option2 = new Option("abc", "app");
        Option option3 = new Option("b", "banana");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertTrue(options.getMatchingOptions("a").contains("apple"));
        assertEquals(1, options.getMatchingOptions("b").size());
        assertTrue(options.getMatchingOptions("b").contains("banana"));
    }

    // Test case for getOptions()
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", true, "Banana option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    // Test case for getRequiredOptions()
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "apple");
        Option option2 = new Option("b", "banana");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option2));
    }

}