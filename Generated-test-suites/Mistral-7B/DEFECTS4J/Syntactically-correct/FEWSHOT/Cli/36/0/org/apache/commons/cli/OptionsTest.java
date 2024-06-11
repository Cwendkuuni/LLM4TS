package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option-a");
        options.addOption(option);
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option-a"));
    }

    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();
        Option option = new Option("a", "option-a", true, "Description for option-a");
        options.addOption(option);
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option-a"));
    }

    @Test
    public void testAddOptionWithShortAndLong() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option-a");
        Option optionLong = new Option("long-option-a", "long-option-a", false, "Description for long-option-a");
        option.setLongOpt(optionLong.getKey());
        options.addOption(option);
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option-a"));
        assertEquals(option, options.getOption("long-option-a"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("group", "Group description");
        Option option = new Option("a", "option-a", false, "Description for option-a");
        optionGroup.addOption(option);
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option-a"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option-a");
        options.addOption(option);
        List<Option> optionsList = options.getOptions();
        assertEquals(option, optionsList.get(0));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option requiredOption = new Option("a", "option-a", true, "Description for option-a");
        options.addOption(requiredOption);
        List<Option> requiredOptionsList = options.getRequiredOptions();
        assertEquals(requiredOption, requiredOptionsList.get(0));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option-a");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option-a"));
        assertFalse(options.hasOption("non-existent-option"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option-a");
        Option optionLong = new Option("long-option-a", "long-option-a", false, "Description for long-option-a");
        option.setLongOpt(optionLong.getKey());
        options.addOption(option);
        assertTrue(options.hasLongOption("long-option-a"));
        assertFalse(options.hasLongOption("non-existent-long-option"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option-a");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("non-existent-short-option"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("group", "Group description");
        Option option = new Option("a", "option-a", false, "Description for option-a");
        optionGroup.addOption(option);
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        assertEquals(optionGroup, options.getOptionGroup(option));
    }
}