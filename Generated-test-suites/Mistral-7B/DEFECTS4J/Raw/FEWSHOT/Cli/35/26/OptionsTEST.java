package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
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
        OptionGroup group = new OptionGroup();
        group.setName("Test Group");
        group.setRequired(false);

        Options options = new Options();
        options.addOptionGroup(group);

        assertEquals("Test Group", options.getOptionGroups().iterator().next().getName());
        assertTrue(options.hasOptionGroup("Test Group"));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup();
        group1.setName("Test Group 1");
        group1.setRequired(false);

        OptionGroup group2 = new OptionGroup();
        group2.setName("Test Group 2");
        group2.setRequired(true);

        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.size() == 2);
        assertSame(group1, optionGroups.get(0));
        assertSame(group2, optionGroups.get(1));
    }

    @Test
    public void testAddOptionWithShortNameNoArg() {
        Options options = new Options();
        options.addOption('a', "option-a", false, "Description for option A");

        assertTrue(options.hasOption('a'));
        assertEquals("Description for option A", options.getOption('a').getDescription());
        assertEquals(false, options.getOption('a').hasArg());
    }

    @Test
    public void testAddOptionWithShortNameArg() {
        Options options = new Options();
        options.addOption('a', "option-a", true, "Description for option A");

        assertTrue(options.hasOption('a'));
        assertEquals("Description for option A", options.getOption('a').getDescription());
        assertEquals(true, options.getOption('a').hasArg());
    }

    @Test
    public void testAddOptionWithShortNameAndLongNameNoArg() {
        Options options = new Options();
        options.addOption('a', "option-a", false, "Description for option A");
        options.addOption("long-option-a", "Description for long option A");

        assertTrue(options.hasOption('a'));
        assertTrue(options.hasOption("long-option-a"));
        assertEquals("Description for option A", options.getOption('a').getDescription());
        assertEquals("Description for long option A", options.getOption("long-option-a").getDescription());
    }

    @Test
    public void testAddOptionWithShortNameAndLongNameArg() {
        Options options = new Options();
        options.addOption('a', "option-a", true, "Description for option A");
        options.addOption("long-option-a", true, "Description for long option A");

        assertTrue(options.hasOption('a'));
        assertTrue(options.hasOption("long-option-a"));
        assertEquals("Description for option A", options.getOption('a').getDescription());
        assertEquals(true, options.getOption('a').hasArg());
        assertEquals(true, options.getOption("long-option-a").hasArg());
    }

    @Test
    public void testAddOptionWithSameShortNameDifferentLongNames() {
        Options options = new Options();
        options.addOption('a', "option-a-1", true, "Description for option A 1");
        options.addOption('a', "option-a-2", false, "Description for option A 2");

        assertTrue(options.hasOption('a'));
        assertTrue(options.hasOption("option-a-1"));
        assertTrue(options.hasOption("option-a-2"));
        assertTrue(options.getOption('a').isAllowedMultiple());
        assertFalse(options.getOption("option-a-1").isAllowedMultiple());
        assertFalse(options.getOption("option-a-2").isAllowedMultiple());
    }

    @Test
    public void testAddOptionWithSameLongNameDifferentShortNames() {
        Options options = new Options();
        options.addOption('a', "option-a", true, "Description for option A");
        options.addOption('b', "option-a", false, "Description for option A (short name B)");

        assertTrue(options.hasOption('a'));
        assertTrue(options.hasOption('b'));
        assertFalse(options.getOption('a').isAllowedMultiple());
        assertFalse(options.getOption('b').isAllowedMultiple());
    }

    @Test
    public void testAddOptionWithDuplicateShortNameAndLongName() {
        Options options = new Options();
        options.addOption('a', "option-a", true, "Description for option A");
        try {
            options.addOption('a', "option-a", true, "Description for option A (duplicate)");
            fail("Expected IllegalArgumentException: Option specification for name 'a' is already defined.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddOptionWithDuplicateShortNameDifferentLongNames() {
        Options options = new Options();
        options.addOption('a', "option-a-1", true, "Description for option A 1");
        try {
            options.addOption('a', "option-a-2", true, "Description for option A 2");
            fail("Expected IllegalArgumentException: Option specification for name 'a' with different long options is already defined.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option('a', "option-a", true, "Description for option A");
        options.addOption(option);

        Option retrievedOption = options.getOption('a');
        assertEquals(option, retrievedOption);

        Option retrievedOptionByLongName = options.getOption("option-a");
        assertEquals(option, retrievedOptionByLongName);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNull() {
        Options options = new Options();
        options.getOption(null);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option opt1 = new Option('o', "option1", true, "Option 1");
        Option opt2 = new Option('o', "option2", true, "Option 2");
        options.addOption(opt1);
        options.addOption(opt2);

        List<String> matchingOptions = options.getMatchingOptions("o");
        assertEquals(Arrays.asList("option1", "option2"), matchingOptions);
    }

    @Test
    public void testGetMatchingOptionsEmpty() {
        Options options = new Options();

        List<String> matchingOptions = options.getMatchingOptions("x");
        assertTrue(matchingOptions.isEmpty());
    }

    @Test
    public void testGetMatchingOptionsPartial() {
        Options options = new Options();
        Option opt1 = new Option('a', "action", true, "Option to perform an action");
        Option opt2 = new Option('b', "backup", true, "Option to create a backup");
        options.addOption(opt1);
        options.addOption(opt2);

        List<String> matchingOptions = options.getMatchingOptions("ac");
        assertEquals(Arrays.asList("action"), matchingOptions);
    }

    @Test
    public void testGetMatchingOptionsPartialNoMatch() {
        Options options = new Options();
        Option opt1 = new Option('a', "action", true, "Option to perform an action");
        Option opt2 = new Option('b', "backup", true, "Option to create a backup");
        options.addOption(opt1);
        options.addOption(opt2);

        List<String> matchingOptions = options.getMatchingOptions("xx");
        assertTrue(matchingOptions.isEmpty());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option opt1 = new Option('a', "action", true, "Option to perform an action");
        Option opt2 = new Option('b', "backup", true, "Option to create a backup");
        options.addOption(opt1);
        options.addOption(opt2);

        assertTrue(options.hasOption('a'));
        assertTrue(options.hasOption('b'));
        assertTrue(options.hasOption("action"));
        assertTrue(options.hasOption("backup"));
        assertTrue(options.hasOption("action"));
    }

    @Test
    public void testHasOptionFalse() {
        Options options = new Options();
        assertTrue(options.hasOption(null));
        assertFalse(options.hasOption(""));
        assertFalse(options.hasOption("x"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option opt1 = new Option('a', "action", true, "Option to perform an action");
        Option opt2 = new Option('b', "backup", true, "Option to create a backup");
        options.addOption(opt1);
        options.addOption(opt2);

        assertTrue(options.hasLongOption("action"));
        assertTrue(options.hasLongOption("backup"));
        assertFalse(options.hasLongOption(""));
        assertFalse(options.hasLongOption("x"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option opt1 = new Option('a', "action", true, "Option to perform an action");
        Option opt2 = new Option('b', "backup", true, "Option to create a backup");
        options.addOption(opt1);
        options.addOption(opt2);

        assertTrue(options.hasShortOption('a'));
        assertTrue(options.hasShortOption('b'));
        assertFalse(options.hasShortOption(''));
        assertFalse(options.hasShortOption('x'));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option opt1 = new Option('a', "action", true, "Option to perform an action");
        Option opt2 = new Option('b', "backup", true, "Option to create a backup");
        options.addOption(opt1);
        options.addOption(opt2);

        String expected = "[ Options: [ short {a={Option name=action, optionClass=class org.apache.commons.cli.Option, description=Option to perform an action}}] [ long {}]]";

        assertEquals(expected, options.toString());
    }
}