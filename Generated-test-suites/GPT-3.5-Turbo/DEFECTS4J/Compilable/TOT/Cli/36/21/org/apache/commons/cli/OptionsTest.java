package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("add"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("delete"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("add"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("delete"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        options.addOption(option1);
        options.addOption(option2);
        options.getOptionGroup(option1).setRequired(true);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.hasOption("a"));
        assertTrue(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("delete"));
        assertNull(options.getOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        Option option3 = new Option("add1", "addition", true, "Additional option");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("d").size());
        assertEquals(0, options.getMatchingOptions("b").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");

        assertFalse(options.hasOption("a"));
        assertFalse(options.hasOption("delete"));

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("add"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("delete"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");

        assertFalse(options.hasLongOption("add"));
        assertFalse(options.hasLongOption("delete"));

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("add"));
        assertTrue(options.hasLongOption("delete"));
        assertFalse(options.hasLongOption("b"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");

        assertFalse(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("d"));

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("d"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("b", "b", false, "B")));
    }
}