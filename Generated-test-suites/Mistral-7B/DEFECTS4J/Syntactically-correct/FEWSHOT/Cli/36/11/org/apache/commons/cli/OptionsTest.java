package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test group");
        Options options = new Options();
        options = options.addOptionGroup(optionGroup);
        assertEquals(optionGroup, options.getOptionGroup(optionGroup));
        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("testGroup1", "Test group 1");
        OptionGroup optionGroup2 = new OptionGroup("testGroup2", "Test group 2");
        Options options = new Options();
        options = options.addOptionGroup(optionGroup1);
        options = options.addOptionGroup(optionGroup2);
        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(2, optionGroups.size());
        assertTrue(optionGroups.contains(optionGroup1));
        assertTrue(optionGroups.contains(optionGroup2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options = options.addOption(option);
        Option optionFromOptions = options.getOption("t");
        assertEquals(option, optionFromOptions);
        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
    }

    @Test
    public void testAddOptionWithDescription() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option with description");
        options = options.addOption(option);
        Option optionFromOptions = options.getOption("t");
        assertEquals(option, optionFromOptions);
        assertEquals("Test option with description", optionFromOptions.getDescription());
    }

    @Test
    public void testAddOptionWithLongOption() {
        Options options = new Options();
        Option option = new Option("t", null, false, "Test option with long name");
        options = options.addOption(option);
        Option optionFromOptions = options.getOption("--test");
        assertEquals(option, optionFromOptions);
        assertTrue(options.hasLongOption("--test"));
    }

    @Test
    public void testAddOptionWithLongAndShortOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option with long and short name");
        options = options.addOption(option);
        Option optionFromOptions = options.getOption("t");
        assertEquals(option, optionFromOptions);
        Option optionFromLong = options.getOption("--test");
        assertEquals(option, optionFromLong);
        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
        assertTrue(options.hasLongOption("--test"));
        assertTrue(options.hasLongOption("--t"));
    }

    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();
        Option option = new Option("t", null, true, "Test option with argument");
        options = options.addOption(option);
        assertEquals(1, options.getOptions().size());
        assertTrue(options.hasOption("t"));
    }

    @Test
    public void testAddOptionWithRequiredArgument() {
        Options options = new Options();
        Option option = new Option("t", null, true, "Test option with required argument");
        options.addOption(option);
        List<Object> requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(option, requiredOptions.get(0));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options = options.addOption(option);
        Option optionFromOptions = options.getOption("t");
        assertEquals(option, optionFromOptions);
    }

    @Test
    public void testGetOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("t", null, false, "Test option with long name");
        options = options.addOption(option);
        Option optionFromOptions = options.getOption("--test");
        assertEquals(option, optionFromOptions);
    }

    @Test
    public void testGetOptionWithEmptyName() {
        Options options = new Options();
        Option option = new Option(null, null, false, "Test option with empty name");
        options = options.addOption(option);
        Option optionFromOptions = options.getOption("");
        assertEquals(option, optionFromOptions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options = options.addOption(option);
        options = options.addOption(option);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("t1", null, false, "Test option 1");
        Option option2 = new Option("t2", null, false, "Test option 2");
        Option option3 = new Option("t", null, false, "Test option common prefix");
        options = options.addOption(option1);
        options = options.addOption(option2);
        options = options.addOption(option3);
        List<String> matchingOptions = options.getMatchingOptions("t");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("--t"));
        assertTrue(matchingOptions.contains("t"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options = options.addOption(option);
        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("t", null, false, "Test option with long name");
        options = options.addOption(option);
        assertTrue(options.hasLongOption("--test"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options = options.addOption(option);
        assertTrue(options.hasShortOption("t"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("t1", null, false, "Test option 1");
        Option option2 = new Option("t2", null, false, "Test option 2");
        Option option3 = new Option("t", null, false, "Test option with long name");
        options = options.addOption(option1);
        options = options.addOption(option2);
        options = options.addOption(option3);
        String toString = options.toString();
        assertTrue(toString.contains("short ["));
        assertTrue(toString.contains("long "));
    }
}