package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionShort() {
        options.addOption("a");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals("a", option.getKey());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionShortWithDescription() {
        options.addOption("a", "Option A");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals("a", option.getKey());
        assertEquals("Option A", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionShortWithDescriptionAndArg() {
        options.addOption("a", true, "Option A");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals("a", option.getKey());
        assertEquals("Option A", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOptionShortWithValue() {
        options.addOption("a", "Option A");
        Option option = new Option("a", "Option A");
        options.addOption(option);
        assertEquals(2, options.getOptions().size());
        assertEquals(option, options.getOptions().get(1));
    }

    @Test
    public void testAddOptionLong() {
        options.addOption("--optionA", "Option A");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals("--optionA", option.getLongOpt());
        assertEquals("Option A", option.getDescription());
        assertNull(option.getKey());
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionShortAndLong() {
        options.addOption("a", "Option A");
        options.addOption("--optionA", "Option A");
        assertEquals(2, options.getOptions().size());
        Option shortOption = options.getOptions().get(0);
        Option longOption = options.getOptions().get(1);
        assertEquals("a", shortOption.getKey());
        assertEquals("--optionA", longOption.getLongOpt());
        assertEquals("Option A", shortOption.getDescription());
        assertEquals("Option A", longOption.getDescription());
        assertNull(shortOption.getLongOpt());
        assertNull(longOption.getKey());
        assertFalse(shortOption.hasArg());
        assertFalse(longOption.hasArg());
    }

    @Test
    public void testAddOptionShortAndLongWithArg() {
        options.addOption("a", true, "Option A");
        options.addOption("--optionA", true, "Option A");
        assertEquals(2, options.getOptions().size());
        Option shortOption = options.getOptions().get(0);
        Option longOption = options.getOptions().get(1);
        assertEquals("a", shortOption.getKey());
        assertEquals("--optionA", longOption.getLongOpt());
        assertEquals("Option A", shortOption.getDescription());
        assertEquals("Option A", longOption.getDescription());
        assertTrue(shortOption.hasArg());
        assertTrue(longOption.hasArg());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("Group", "Group options");
        options.addOptionGroup(group);
        assertEquals(1, options.getOptions().size());
        assertEquals(0, group.getOptions().size());
        assertSame(options, group.getParent());
    }

    @Test
    public void testAddOptionToGroup() {
        OptionGroup group = new OptionGroup("Group", "Group options");
        Option option = new Option("a", "Option A");
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(2, options.getOptions().size());
        assertEquals(1, group.getOptions().size());
        assertEquals(option, group.getOptions().get(0));
    }

    @Test
    public void testGetRequiredOptionsEmpty() {
        assertEquals(Collections.emptyList(), options.getRequiredOptions());
    }

    @Test
    public void testGetRequiredOptionsOne() {
        options.addOption("a");
        options.addOption("a", true, "Option A");
        assertEquals(Arrays.asList("a"), options.getRequiredOptions());
    }

    @Test
    public void testGetRequiredOptionsMultiple() {
        OptionGroup group = new OptionGroup("Group", "Group options");
        group.setRequired(true);
        options.addOptionGroup(group);
        options.addOption("a");
        assertEquals(Arrays.asList("a"), options.getRequiredOptions());
        assertEquals(Arrays.asList(group), options.getOptionGroups());
    }

    @Test
    public void testGetMatchingOptionsEmpty() {
        assertEquals(Collections.emptyList(), options.getMatchingOptions(""));
        assertEquals(Collections.emptyList(), options.getMatchingOptions("a"));
    }

    @Test
    public void testGetMatchingOptionsSingle() {
        options.addOption("--optionA");
        List<String> matchingOptions = options.getMatchingOptions("optionA");
        assertEquals(1, matchingOptions.size());
        assertEquals("--optionA", matchingOptions.get(0));
    }

    @Test
    public void testGetMatchingOptionsMultiple() {
        options.addOption("--optionA");
        options.addOption("--optionB");
        List<String> matchingOptions = options.getMatchingOptions("option");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.containsAll(Arrays.asList("--optionA", "--optionB")));
    }

    @Test
    public void testHasOptionEmpty() {
        assertFalse(options.hasOption(""));
        assertFalse(options.hasOption("a"));
    }

    @Test
    public void testHasOptionShort() {
        options.addOption("a");
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption(""));
    }

    @Test
    public void testHasOptionLong() {
        options.addOption("--optionA");
        assertTrue(options.hasOption("--optionA"));
        assertFalse(options.hasOption(""));
    }

    @Test
    public void testHasOptionValue() {
        options.addOption("a", "A");
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption(""));
        assertFalse(options.hasOption("B"));
    }

    @Test
    public void testHasLongOptionEmpty() {
        assertFalse(options.hasLongOption(""));
        assertFalse(options.hasLongOption("--optionA"));
    }

    @Test
    public void testHasLongOptionTrue() {
        options.addOption("--optionA");
        assertTrue(options.hasLongOption("--optionA"));
        assertFalse(options.hasLongOption(""));
    }

    @Test
    public void testHasShortOptionEmpty() {
        assertFalse(options.hasShortOption(""));
        assertFalse(options.hasShortOption("a"));
    }

    @Test
    public void testHasShortOptionTrue() {
        options.addOption("a");
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption(""));
    }

    @Test
    public void testGetOptionGroupEmpty() {
        assertNull(options.getOptionGroup(""));
    }

    @Test
    public void testGetOptionGroupPresent() {
        OptionGroup group = new OptionGroup("Group", "Group options");
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup("Group"));
        assertNull(options.getOptionGroup(""));
    }

    @Test
    public void testToString() {
        options.addOption("a");
        options.addOption("--optionA");
        assertEquals("[ Options: [ short {a=}, --optionA=], long {} ]", options.toString());
    }
}