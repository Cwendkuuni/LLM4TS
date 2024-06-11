package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @org.junit.Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "test option");
        Option option2 = new Option("b", "banana", true, "test option");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "apple", false, "test option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "apple", false, "test option");
        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "apple", false, "test option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "apple", false, "test option");
        options.addOption(option);

        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasLongOption("banana"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "apple", false, "test option");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "test option");
        Option option2 = new Option("b", "banana", true, "test option");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "test option");
        Option option2 = new Option("b", "banana", true, "test option");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option{ opt = a, longOpt = apple, hasArg = false, description = test option }} ] [ long {apple=Option{ opt = a, longOpt = apple, hasArg = false, description = test option }} ]";
        assertEquals(expected, options.toString());
    }
}