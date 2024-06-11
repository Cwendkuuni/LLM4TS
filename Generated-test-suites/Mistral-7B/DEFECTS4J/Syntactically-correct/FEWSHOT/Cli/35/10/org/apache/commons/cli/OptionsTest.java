package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("TestGroup", "Test Group description", true);
        Options options = new Options();
        Options result = options.addOptionGroup(group);
        assertEquals(options, result);
        assertEquals(Collections.singleton(group), options.getOptionGroups());
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("TestGroup1", "Test Group 1 description", true);
        OptionGroup group2 = new OptionGroup("TestGroup2", "Test Group 2 description", true);
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        List<OptionGroup> expected = new ArrayList<>(Arrays.asList(group1, group2));
        assertEquals(expected, options.getOptionGroups());
    }

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();
        options.addOption("a", "Option A short description");
        Option option = options.getOption("a");
        assertEquals("a", option.getKey());
        assertEquals("Option A short description", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionWithShortNameAndArgument() {
        Options options = new Options();
        options.addOption("a", "Option A short description", true, "arg");
        Option option = options.getOption("a");
        assertEquals("a", option.getKey());
        assertEquals("Option A short description", option.getDescription());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A long description");
        Option option = options.getOption("optionA");
        assertEquals("a", option.getKey());
        assertEquals("optionA", option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertEquals("Option A long description", option.getDescription());
    }

    @Test
    public void testAddOptionWithLongNameAndArgument() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "arg", "Option A long description");
        Option option = options.getOption("optionA");
        assertEquals("a", option.getKey());
        assertEquals("optionA", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals("Option A long description", option.getDescription());
    }

    @Test
    public void testGetOptionWithShortName() {
        Options options = new Options();
        Option option = new Option("a", "Option A short description");
        options.addOption(option);
        Option result = options.getOption("a");
        assertEquals(option, result);
    }

    @Test
    public void testGetOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A long description");
        options.addOption(option);
        Option result = options.getOption("optionA");
        assertEquals(option, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateOptionWithShortName() {
        Options options = new Options();
        Option option1 = new Option("a", "Option A short description");
        Option option2 = new Option("a", "Option A short description");
        options.addOption(option1);
        options.addOption(option2);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateOptionWithLongName() {
        Options options = new Options();
        Option option1 = new Option("a", "optionA", false, "Option A long description");
        Option option2 = new Option("a", "optionA", false, "Option A long description");
        options.addOption(option1);
        options.addOption(option2);
    }

    @Test
    public void testGetOptionWithNonExistentShortName() {
        Options options = new Options();
        assertNull(options.getOption("nonExistent"));
    }

    @Test
    public void testGetOptionWithNonExistentLongName() {
        Options options = new Options();
        assertNull(options.getOption("nonExistent:long"));
    }

    @Test
    public void testAddRequiredOptionWithShortName() {
        Options options = new Options();
        Option option = new Option("a", "Option A short description");
        options.addOption(option);
        List<String> requiredOptions = options.getRequiredOptions();
        assertEquals(Collections.singleton("a"), requiredOptions);
    }

    @Test
    public void testAddRequiredOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("optionA", "Option A long description");
        options.addOption(option);
        List<String> requiredOptions = options.getRequiredOptions();
        assertEquals(Collections.singleton("optionA"), requiredOptions);
    }

    @Test
    public void testAddOptionGroupWithRequiredOptions() {
        OptionGroup group = new OptionGroup("TestGroup", "Test Group description", true);
        Option option1 = new Option("a", "Option A short description");
        Option option2 = new Option("b", "Option B short description");
        group.addOption(option1);
        group.addOption(option2);
        Options options = new Options();
        options.addOptionGroup(group);
        List<String> requiredOptions = options.getRequiredOptions();
        List<String> expected = new ArrayList<>(Arrays.asList("a", "b"));
        assertEquals(expected, requiredOptions);
    }
}