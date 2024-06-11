package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("test", "Test Group");
        Options options = new Options();
        options.addOptionGroup(group);
        Options result = options.addOptionGroup(group);
        assertEquals(options, result);
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("test1", "Test Group 1");
        OptionGroup group2 = new OptionGroup("test2", "Test Group 2");
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        List<OptionGroup> groups = options.getOptionGroups();
        assertIterableEquals(Collections.singletonList(group1), groups);
        groups = options.getOptionGroups();
        assertIterableEquals(Collections.singletonList(group2), groups);
        groups = options.getOptionGroups();
        assertIterableEquals(Collections.unmodifiableList(List.of(group1, group2)), groups);
    }

    @Test
    public void testAddOptionWithShortOpt() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with short opt");
        options.addOption(option);
        Option result = options.getOption("o");
        assertEquals(option, result);
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        Option option = new Option("opt", "Option with long opt", false, "Option with long opt");
        options.addOption(option);
        Option result = options.getOption("opt");
        assertEquals(option, result);
    }

    @Test
    public void testAddOptionWithShortAndLongOpt() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with short and long opt");
        option.setLongOpt("long-opt");
        options.addOption(option);
        Option result = options.getOption("o");
        assertEquals(option, result);
        Option resultLong = options.getOption("long-opt");
        assertEquals(option, resultLong);
    }

    @Test
    public void testAddOptionWithShortAndLongOptAndHasArg() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option with short and long opt and argument");
        option.setLongOpt("long-opt");
        options.addOption(option);
        Option result = options.getOption("o");
        assertEquals(option, result);
        Option resultLong = options.getOption("long-opt");
        assertEquals(option, resultLong);
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with short opt");
        options.addOption(option);
        assertTrue(options.hasShortOption("o"));
        assertTrue(options.hasShortOption("O"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("opt", "Option with long opt", false, "Option with long opt");
        options.addOption(option);
        assertTrue(options.hasLongOption("opt"));
        assertFalse(options.hasLongOption("Opt"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with short opt");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("opt"));
        assertFalse(options.hasOption("Op"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with short opt");
        options.addOption(option);
        Option result = options.getOption("o");
        assertEquals(option, result);
        Option resultLong = options.getOption("opt");
        assertEquals(option, resultLong);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        Option option3 = new Option("o3", "opt3", false, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> result = options.getMatchingOptions("o");
        assertIterableEquals(List.of("o1", "o2", "o3"), result);
        result = options.getMatchingOptions("opt");
        assertIterableEquals(List.of("opt1", "opt2"), result);
        result = options.getMatchingOptions("Opt");
        assertIterableEquals(Collections.emptyList(), result);
        result = options.getMatchingOptions("opt3");
        assertIterableEquals(Collections.singletonList("opt3"), result);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        Option option3 = new Option("o3", "opt3", false, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        String result = options.toString();
        String expected = "[ Options: [ short {o=o1, o=o2, o=o3}] [ long {opt1=o1, opt2=o2, opt3=o3}] ]";
        assertEquals(expected, result);
    }
}