package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test group");
        Options options = new Options();
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(group));
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        assertEquals(Arrays.asList(group1, group2), options.getOptionGroups());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", "Option A");
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("a")); // short and long names are equal
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("a", "--long-a", false, "Option A long");
        assertTrue(options.hasLongOption("--long-a"));
        assertFalse(options.hasLongOption("--unknown-long"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A short");
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("a")); // short and long names are equal
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "--long-a", false, "Option A long");
        Options options = new Options();
        options.addOption(option);
        assertSame(option, options.getOption("--long-a"));
        assertSame(option, options.getOption("a"));
        assertNull(options.getOption("--unknown"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", false, "Option 2");
        options.addOption("option3", false, "Option 3 with long name");

        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(Arrays.asList("option1", "option3"), matchingOptions);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("a", false, "Option A short");
        options.addOption("b", "--long-b", false, "Option B long");
        assertEquals("[ Options: [ short {a=} ] [ long {b=} ] ]", options.toString());
    }
}