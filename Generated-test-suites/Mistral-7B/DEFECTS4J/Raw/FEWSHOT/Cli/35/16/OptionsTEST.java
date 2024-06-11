package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option opt1 = new Option("a", "optionA", false, "Description for option A");
        Option opt2 = new Option("b", "optionB", true, "Description for option B");
        Option opt3 = new Option("c", null, false, "Description for option C");
        Option opt4 = new Option("d", "longOptionD", false, "Description for long option D");

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        options.addOption(opt4);

        assertEquals(4, options.getOptions().size());
        assertEquals(opt1, options.getOption("a"));
        assertEquals(opt2, options.getOption("b"));
        assertEquals(opt3, options.getOption("c"));
        assertEquals(opt4, options.getOption("d"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        Option opt1 = new Option("g1a", null, false, "Option A in group 1");
        Option opt2 = new Option("g1b", null, true, "Option B in group 1");
        group1.addOption(opt1);
        group1.addOption(opt2);

        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        Option opt3 = new Option("g2a", null, false, "Option A in group 2");
        Option opt4 = new Option("g2b", null, false, "Option B in group 2");
        group2.addOption(opt3);
        group2.addOption(opt4);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        List<OptionGroup> groups = options.getOptionGroups();
        assertEquals(2, groups.size());
        assertEquals(group1, options.getOptionGroup(group1.getKey()));
        assertEquals(group2, options.getOptionGroup(group2.getKey()));
        assertEquals(opt1, group1.getOption(opt1.getKey()));
        assertEquals(opt2, group1.getOption(opt2.getKey()));
        assertEquals(opt3, group2.getOption(opt3.getKey()));
        assertEquals(opt4, group2.getOption(opt4.getKey()));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option opt1 = new Option("a", "optionA", false, "Description for option A");
        Option opt2 = new Option("b", "optionB", true, "Description for option B");

        options.addOption(opt1);
        options.addOption(opt2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
        assertFalse(options.hasLongOption("a"));
        assertFalse(options.hasLongOption("c"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option opt1 = new Option("opt", "option1");
        Option opt2 = new Option("opt1", "option1");
        Option opt3 = new Option("opt2", "option2");

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        List<String> matchingOptions = options.getMatchingOptions("opt1");
        assertEquals(1, matchingOptions.size());
        assertEquals("opt1", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("opt");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("opt"));
        assertTrue(matchingOptions.contains("opt1"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option opt1 = new Option("a", "optionA", false, "Description for option A");
        Option opt2 = new Option("b", "optionB", true, "Description for option B");

        options.addOption(opt1);
        options.addOption(opt2);

        Option optRet1 = options.getOption("a");
        Option optRet2 = options.getOption("b");

        assertEquals(opt1, optRet1);
        assertEquals(opt2, optRet2);
    }

    @Test
    public void testToString() {
        Options options = new Options();

        Option opt1 = new Option("a", "optionA", false, "Description for option A");
        Option opt2 = new Option("b", "optionB", true, "Description for option B");

        options.addOption(opt1);
        options.addOption(opt2);

        String toString = options.toString();
        assertTrue(toString.contains("[ Options: [ short"));
        assertTrue(toString.contains("[ long"));
    }
}