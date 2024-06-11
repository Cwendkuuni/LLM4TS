package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("test", "Test options", true);
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        assertNotNull(options.getOptionGroups());
        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("test1", "Test options 1", true);
        OptionGroup optionGroup2 = new OptionGroup("test2", "Test options 2", true);
        Options options = new Options();
        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);
        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(2, optionGroups.size());
        assertTrue(optionGroups.contains(optionGroup1));
        assertTrue(optionGroups.contains(optionGroup2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", "Option A");
        assertTrue(options.hasOption("a"));
        assertEquals("Option A", options.getOption("a").getLongOpt());
        assertNull(options.getOption("a").getLongOpt());

        options.addOption("a", "Option A", true, "aValue");
        assertTrue(options.hasOption("a"));
        assertEquals("Option A", options.getOption("a").getLongOpt());
        assertEquals("aValue", options.getOption("a").getValue());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        options.addOption("a", "Option A");
        options.addOption("long-option-a", "Option A with long name");
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("long-option-a"));
        assertEquals("Option A", options.getOption("a").getLongOpt());
        assertEquals("Option A with long name", options.getOption("long-option-a").getLongOpt());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", "Option A");
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("a", "Option A");
        options.addOption("long-option-a", "Option A with long name");
        assertTrue(options.hasLongOption("long-option-a"));
        assertFalse(options.hasLongOption("b"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("a", "Option A");
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "Option A");
        options.addOption(option);
        Option retrievedOption = options.getOption("a");
        assertEquals(option, retrievedOption);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNullOptionName() {
        Options options = new Options();
        options.getOption(null);
    }
}