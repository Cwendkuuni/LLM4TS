package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test normal case
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Test option");
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(1, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        // Test normal case
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testGetOption() {
        // Test long option
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertEquals(option, options.getOption("option"));

        // Test short option
        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test partial matching
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Test option 1");
        Option option2 = new Option("p", "option2", false, "Test option 2");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getMatchingOptions("o").size());
    }

    @Test
    public void testHasOption() {
        // Test existing option
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("o"));

        // Test non-existing option
        assertFalse(options.hasOption("x"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test existing option group
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Test option");
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option));

        // Test non-existing option group
        Option option2 = new Option("p", "option2", false, "Test option 2");
        assertNull(options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        assertNotNull(options.toString());
    }
}