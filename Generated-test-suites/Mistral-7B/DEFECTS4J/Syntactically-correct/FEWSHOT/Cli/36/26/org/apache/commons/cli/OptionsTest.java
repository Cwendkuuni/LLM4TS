package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testGroup", "test group", true, "test");
        Options options = new Options();
        options = options.addOptionGroup(optionGroup);

        assertEquals("testGroup", options.getOptionGroup(optionGroup).getName());
        assertTrue(options.hasOptionGroup("testGroup"));
        assertEquals(Collections.singletonList(optionGroup), options.getOptionGroups());
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testGroup", "test group", true, "test");
        Options options = new Options();
        options = options.addOptionGroup(optionGroup);

        Option option = new Option("t", "test", true, "Test option");
        Option option2 = new Option("s", "test2", true, "Test option 2");
        optionGroup.addOption(option);
        optionGroup.addOption(option2);

        assertSame(optionGroup, options.getOptionGroup(option));
        assertSame(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testHasOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testGroup", "test group", true, "test");
        Options options = new Options();
        options = options.addOptionGroup(optionGroup);

        assertTrue(options.hasOptionGroup("testGroup"));
        assertFalse(options.hasOptionGroup("nonexistentGroup"));
    }

    @Test
    public void testAddOptionWithShortOnly() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options = options.addOption(option);

        assertEquals("test", options.getOption(option.getKey()).getKey());
        assertEquals("Test option", options.getOption(option.getKey()).getLongOpt());
        assertFalse(options.getOption(option.getKey()).hasArg());
        assertEquals(Collections.singletonList(option), options.helpOptions());
        assertEquals(1, options.getRequiredOptions().size());
        assertSame(option, options.getRequiredOptions().get(0));
    }

    @Test
    public void testAddOptionWithShortAndLong() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        Option optionLong = new Option("test-opt", "Test long option", false, "Test option");
        options = options.addOption(option);
        options = options.addOption(optionLong);

        assertEquals("test", options.getOption(option.getKey()).getKey());
        assertEquals("Test option", options.getOption(option.getKey()).getLongOpt());
        assertFalse(options.getOption(option.getKey()).hasArg());
        assertEquals("Test long option", options.getOption(optionLong.getKey()).getLongOpt());
        assertFalse(options.getOption(optionLong.getKey()).hasArg());
        assertEquals(2, options.helpOptions().size());
        assertEquals(1, options.getRequiredOptions().size());
        assertSame(option, options.getRequiredOptions().get(0));
    }

    @Test
    public void testAddOptionWithShortAndLongWithArg() {
        Options options = new Options();
        Option option = new Option("t", "test", true, "Test option");
        Option optionLong = new Option("test-opt", "Test long option", true, "Test option");
        options = options.addOption(option);
        options = options.addOption(optionLong);

        assertEquals("test", options.getOption(option.getKey()).getKey());
        assertEquals("Test option", options.getOption(option.getKey()).getLongOpt());
        assertTrue(options.getOption(option.getKey()).hasArg());
        assertEquals("Test long option", options.getOption(optionLong.getKey()).getLongOpt());
        assertTrue(options.getOption(optionLong.getKey()).hasArg());
        assertEquals(2, options.helpOptions().size());
        assertEquals(1, options.getRequiredOptions().size());
        assertSame(option, options.getRequiredOptions().get(0));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("t", "test2", false, "Test option 2");
        Option option3 = new Option("long-t", "test3", false, "Test option 3");
        options = options.addOption(option1).addOption(option2).addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("t");
        List<String> expectedMatchingOptions = Arrays.asList("test1", "test2", "test3");
        assertEquals(expectedMatchingOptions, matchingOptions);

        List<String> matchingOptions2 = options.getMatchingOptions("longTest");
        List<String> expectedMatchingOptions2 = Arrays.asList("test3");
        assertEquals(expectedMatchingOptions2, matchingOptions2);

        List<String> matchingOptions3 = options.getMatchingOptions("nonexistent");
        List<String> expectedMatchingOptions3 = Collections.emptyList();
        assertEquals(expectedMatchingOptions3, matchingOptions3);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("t", "test2", false, "Test option 2");
        Option option3 = new Option("long-t", "test3", false, "Test option 3");
        options = options.addOption(option1).addOption(option2).addOption(option3);

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test1"));
        assertTrue(options.hasOption("test2"));
        assertTrue(options.hasOption("long-t"));
        assertTrue(options.hasOption("test3"));

        assertFalse(options.hasOption("nonexistent"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("t", "test2", false, "Test option 2");
        Option option3 = new Option("long-t", "test3", false, "Test option 3");
        options = options.addOption(option1).addOption(option2).addOption(option3);

        assertTrue(options.hasLongOption("test1"));
        assertTrue(options.hasLongOption("test2"));
        assertTrue(options.hasLongOption("test3"));

        assertFalse(options.hasLongOption("nonexistent"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("t", "test2", false, "Test option 2");
        Option option3 = new Option("long-t", "test3", false, "Test option 3");
        options = options.addOption(option1).addOption(option2).addOption(option3);

        assertTrue(options.hasShortOption("t"));
        assertTrue(options.hasShortOption("test1"));
        assertTrue(options.hasShortOption("test2"));

        assertFalse(options.hasShortOption("nonexistent"));
    }
}