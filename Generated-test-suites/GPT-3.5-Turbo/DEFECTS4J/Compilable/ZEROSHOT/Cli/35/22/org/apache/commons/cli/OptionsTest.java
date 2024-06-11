package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOption("a") == option1);
        assertTrue(options.getOption("b") == option2);
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getOption("a") == option);

        options.addOption("b", "option2", false, "description");

        assertTrue(options.getOptions().size() == 2);
        assertTrue(options.getOption("b").getLongOpt() == "option2");
        assertFalse(options.getOption("b").hasArg());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOption("a") == option1);
        assertTrue(options.getOption("b") == option2);
        assertTrue(options.getOption("c") == null);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("a", "apple", true, "description");
        options.addOption("b", "banana", false, "description");
        options.addOption("c", "carrot", true, "description");
        options.addOption("d", "date", false, "description");

        assertTrue(options.getMatchingOptions("a").size() == 1);
        assertTrue(options.getMatchingOptions("b").size() == 1);

        options.addOption("au", "aubergine", true, "description");
        options.addOption("ap", "applePie", false, "description");

        assertTrue(options.getMatchingOptions("a").size() == 3);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option");

        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("option2"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option");

        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroup(option1) == group);
        assertTrue(options.getOptionGroup(option2) == group);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {a=Option a <arg>}", options.toString());
    }
    
}