package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group1", "Test group 1");
        Options options = new Options();
        options = options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(group));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("group1", "Test group 1");
        OptionGroup group2 = new OptionGroup("group2", "Test group 2");
        Options options = new Options();
        options = options.addOptionGroup(group1);
        options = options.addOptionGroup(group2);
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group1));
        assertTrue(optionGroups.contains(group2));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "opt", false, "Test option");
        Options options = new Options();
        options = options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("opt"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "opt", false, "Test option");
        Options options = new Options();
        options = options.addOption(option);
        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "opt", false, "Test option");
        Options options = new Options();
        options = options.addOption(option);
        assertTrue(options.hasLongOption("opt"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", false, "Test option");
        Options options = new Options();
        options = options.addOption(option);
        Option actual = options.getOption("opt");
        assertSame(option, actual);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o1", "opt1", false, "Test option 1");
        Option option2 = new Option("o2", "opt2", false, "Test option 2");
        Option option3 = new Option("o3", "opt3", false, "Test option 3");
        Options options = new Options();
        options = options.addOption(option1);
        options = options.addOption(option2);
        options = options.addOption(option3);
        List<String> expected = new ArrayList<>();
        expected.add("o2");
        expected.add("o3");
        List<String> actual = options.getMatchingOptions("o");
        assertEquals(expected, actual);
    }

    @Test
    public void testHasOptionWithGroup() {
        Option option = new Option("g", "group", false, "Test group");
        OptionGroup group = new OptionGroup("group1", "Test group 1");
        group.addOption(option);
        Options options = new Options();
        options = options.addOptionGroup(group);
        assertTrue(options.hasOption("g"));
        assertTrue(options.hasOption("group"));
    }

    @Test
    public void testGetOptionWithGroup() {
        Option option = new Option("g", "group", false, "Test group");
        OptionGroup group = new OptionGroup("group1", "Test group 1");
        group.addOption(option);
        Options options = new Options();
        options = options.addOptionGroup(group);
        Option actual = options.getOption("group");
        assertSame(option, actual);
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o1", "opt1", true, "Test option 1");
        Option option2 = new Option("o2", "opt2", true, "Test option 2");
        Options options = new Options();
        options = options.addOption(option1);
        options = options.addOption(option2);
        List<Option> expected = new ArrayList<>();
        expected.add(option1);
        expected.add(option2);
        List<Option> actual = options.getRequiredOptions();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "opt1", false, "Test option 1");
        Option option2 = new Option("o2", "opt2", false, "Test option 2");
        Options options = new Options();
        options = options.addOption(option1);
        options = options.addOption(option2);
        List<Option> expected = new ArrayList<>();
        expected.add(option1);
        expected.add(option2);
        List<Option> actual = options.getOptions();
        assertEquals(expected, actual);
    }
}