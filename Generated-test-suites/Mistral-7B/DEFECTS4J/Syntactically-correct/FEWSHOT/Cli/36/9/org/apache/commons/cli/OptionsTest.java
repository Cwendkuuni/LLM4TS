package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setLongOpt("group");
        optionGroup.setDescription("Option group");

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        assertEquals(1, options.getOptionGroups().size());
        assertEquals("group", options.getOptionGroups().iterator().next().getLongOpt());
        assertEquals("Option group", options.getOptionGroups().iterator().next().getDescription());
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup();
        optionGroup1.setLongOpt("group1");
        optionGroup1.setDescription("Option group 1");

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setLongOpt("group2");
        optionGroup2.setDescription("Option group 2");

        Options options = new Options();
        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);

        List<OptionGroup> optionGroups = options.getOptionGroups();

        assertEquals(2, optionGroups.size());
        assertEquals("group1", optionGroups.get(0).getLongOpt());
        assertEquals("Option group 1", optionGroups.get(0).getDescription());
        assertEquals("group2", optionGroups.get(1).getLongOpt());
        assertEquals("Option group 2", optionGroups.get(1).getDescription());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", "a", false, "Option A");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("a")); // short and long names are considered the same
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("a", "longA", false, "Option A with long name");

        assertTrue(options.hasLongOption("longA"));
        assertFalse(options.hasLongOption("shortA"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A with short name");

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "longA", false, "Option A with long name");
        Options options = new Options();
        options.addOption(option);

        assertEquals(option, options.getOption("longA"));
        assertEquals(option, options.getOption("a"));
        assertNull(options.getOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("a");
        options.addOption("ab");
        options.addOption("abc");

        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(2, matchingOptions.size());
        assertEquals("a", matchingOptions.get(0));
        assertEquals("ab", matchingOptions.get(1));

        List<String> emptyMatchingOptions = options.getMatchingOptions("xyz");
        assertEquals(0, emptyMatchingOptions.size());
    }
}