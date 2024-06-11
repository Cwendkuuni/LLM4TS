package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("t", "test", true, "test option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("apple", null, false, "description");
        Option option2 = new Option("banana", null, false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getMatchingOptions("app").contains("apple"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("t", "test", true, "test option");

        options.addOption(option);

        assertTrue(options.hasOption("t"));
        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("t", "test", true, "test option");

        options.addOption(option);

        assertTrue(options.hasLongOption("test"));
        assertFalse(options.hasLongOption("invalid"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("t", "test", true, "test option");

        options.addOption(option);

        assertTrue(options.hasShortOption("t"));
        assertFalse(options.hasShortOption("invalid"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "apple", false, "description");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("t", "test", true, "test option");

        options.addOption(option);

        assertNotNull(options.toString());
    }
}