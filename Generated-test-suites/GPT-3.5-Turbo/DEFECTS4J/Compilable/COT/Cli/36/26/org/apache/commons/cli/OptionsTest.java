package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", true, "Add option");
        Option option2 = new Option("d", "delete", true, "Delete option");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "add", true, "Add option");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "add", true, "Add option");
        Option option2 = new Option("d", "delete", true, "Delete option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("--add"));
        assertEquals(option2, options.getOption("d"));
        assertEquals(option2, options.getOption("--delete"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "add", true, "Add option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("--add"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "add", true, "Add option");
        options.addOption(option);

        assertTrue(options.hasLongOption("add"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "add", true, "Add option");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }
}