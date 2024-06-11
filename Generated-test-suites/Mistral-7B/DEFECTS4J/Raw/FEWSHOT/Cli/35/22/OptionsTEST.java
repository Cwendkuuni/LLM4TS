package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        Options options = new Options();
        options.addOptionGroup(group);
        Collection<OptionGroup> groups = options.getOptionGroups();
        assertTrue(groups.contains(group));
        assertEquals(1, groups.size());
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("testGroup1", "Test Group 1");
        OptionGroup group2 = new OptionGroup("testGroup2", "Test Group 2");
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        Collection<OptionGroup> groups = options.getOptionGroups();
        assertEquals(2, groups.size());
        assertTrue(groups.contains(group1));
        assertTrue(groups.contains(group2));
    }

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();
        options.addOption("o", "option", true, "Test Option");
        List<Option> optionsList = options.helpOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o", option.getKey());
        assertEquals("option", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Test Option", option.getDescription());
    }

    @Test
    public void testAddOptionWithShortNameAndLongName() {
        Options options = new Options();
        options.addOption("o", "option", true, "Test Option");
        options.addOption("long-option", "option", true, "Test Long Option");
        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        assertEquals("o", option1.getKey());
        assertEquals("option", option1.getLongOpt());
        assertTrue(option1.hasArg());
        assertEquals("Test Option", option1.getDescription());

        Option option2 = optionsList.get(1);
        assertEquals("long-option", option2.getKey());
        assertEquals("option", option2.getLongOpt());
        assertTrue(option2.hasArg());
        assertEquals("Test Long Option", option2.getDescription());
    }

    @Test
    public void testAddOptionWithShortNameAndValue() {
        Options options = new Options();
        options.addOption("o", "option", 1, "Test Option");
        List<Option> optionsList = options.helpOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o", option.getKey());
        assertNull(option.getLongOpt());
        assertEquals(1, option.getValnum());
        assertEquals("Test Option", option.getDescription());
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        options.addOption("--long-option", "Test Long Option", false, "Test Long Option");
        List<Option> optionsList = options.helpOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertNull(option.getKey());
        assertEquals("--long-option", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Test Long Option", option.getDescription());
    }

    @Test
    public void testAddOptionWithShortNameAndLongNameAndValue() {
        Options options = new Options();
        options.addOption("o", "option", 1, "Test Option");
        options.addOption("--long-option", "Test Long Option", false, "Test Long Option");
        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        assertEquals("o", option1.getKey());
        assertNull(option1.getLongOpt());
        assertEquals(1, option1.getValnum());
        assertEquals("Test Option", option1.getDescription());

        Option option2 = optionsList.get(1);
        assertNull(option2.getKey());
        assertEquals("--long-option", option2.getLongOpt());
        assertFalse(option2.hasArg());
        assertEquals("Test Long Option", option2.getDescription());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption("o", true, "Test Option");
        List<Option> requiredOptions = options.getRequiredOptions();
        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("o");
        assertEquals(expectedKeys, requiredOptions);
    }

    @Test
    public void testGetRequiredOptionsWithOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        group.addOption("g", true, "Test Option Group");
        Options options = new Options();
        options.addOptionGroup(group);
        List<Option> requiredOptions = options.getRequiredOptions();
        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("g");
        assertEquals(expectedKeys, requiredOptions);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("o", "Test Option");
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("--option"));
        assertFalse(options.hasOption("x"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("--long-option", "Test Long Option");
        assertTrue(options.hasLongOption("--long-option"));
        assertFalse(options.hasLongOption("--another-option"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("o", "Test Option");
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("x"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "Test Option");
        options.addOption(option);
        Option retrievedOption = options.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionNull() {
        Options options = new Options();
        options.getOption(null);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("option1", "Test Option 1");
        options.addOption("option2", "Test Option 2");
        options.addOption("option3", "Test Option 3");

        List<String> matchingOptions = options.getMatchingOptions("option");
        List<String> expectedMatchingOptions = new ArrayList<>();
        expectedMatchingOptions.add("option1");
        expectedMatchingOptions.add("option2");
        expectedMatchingOptions.add("option3");
        assertEquals(expectedMatchingOptions, matchingOptions);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("o", "Test Option");
        options.addOption("--long-option", "Test Long Option");
        String expectedString = "[ Options: [ short {o=} ] [ long {--long-option=} ]]";
        assertEquals(expectedString, options.toString());
    }
}