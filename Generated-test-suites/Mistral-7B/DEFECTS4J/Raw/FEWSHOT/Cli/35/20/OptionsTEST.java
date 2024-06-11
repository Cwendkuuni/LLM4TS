package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        Options options = new Options();

        options.addOptionGroup(group);
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        OptionGroup group2 = new OptionGroup("group2", "Group 2");

        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(2, optionGroups.size());
        assertTrue(optionGroups.contains(group1));
        assertTrue(optionGroups.contains(group2));
    }

    @Test
    public void testGetOptionGroupByOption() {
        Option opt = new Option("t", "test", true, "Test option");
        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        group.addOption(opt);
        Options options = new Options().addOptionGroup(group);

        OptionGroup optionGroup = options.getOptionGroup(opt);
        assertEquals(group, optionGroup);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("t", true, "Test option");

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("--test"));
        assertFalse(options.hasOption("u"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("t", true, "Test option");
        options.addOption("--test", true, "Test option long");

        assertTrue(options.hasLongOption("--test"));
        assertFalse(options.hasLongOption("--nonexistent"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("t", true, "Test option");

        assertTrue(options.hasShortOption("t"));
        assertFalse(options.hasShortOption("u"));
    }

    @Test
    public void testGetOption() {
        Option opt = new Option("t", "test", true, "Test option");
        Options options = new Options().addOption(opt);

        Option result = options.getOption("t");
        assertEquals(opt, result);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("test1", true, "Test option 1");
        options.addOption("test2", true, "Test option 2");
        options.addOption("test3", true, "Test option 3");

        List<String> matchingOptions = options.getMatchingOptions("test");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("test1"));
        assertTrue(matchingOptions.contains("test2"));
    }
}