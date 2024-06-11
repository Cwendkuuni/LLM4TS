package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an option group to Options
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        // Test adding an option to Options
        Options options = new Options();
        Option option = new Option("c", "option3", false, "Option 3");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testHasOption() {
        // Test checking if an option exists in Options
        Options options = new Options();
        Option option = new Option("d", "option4", false, "Option 4");

        options.addOption(option);

        assertTrue(options.hasOption("d"));
        assertFalse(options.hasOption("e"));
    }

    @Test
    public void testGetOption() {
        // Test retrieving an option from Options
        Options options = new Options();
        Option option = new Option("e", "option5", true, "Option 5");

        options.addOption(option);

        assertEquals(option, options.getOption("e"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting options matching a partial name
        Options options = new Options();
        Option option1 = new Option("f", "option6", false, "Option 6");
        Option option2 = new Option("fg", "option7", false, "Option 7");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("f").size());
        assertEquals(2, options.getMatchingOptions("fg").size());
    }

    @Test
    public void testGetOptionGroup() {
        // Test retrieving the OptionGroup of an option
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("h", "option8", true, "Option 8");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }
}