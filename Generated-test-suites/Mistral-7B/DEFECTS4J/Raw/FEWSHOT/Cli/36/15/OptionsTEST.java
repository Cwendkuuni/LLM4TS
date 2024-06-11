package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test Group");
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        assertEquals(1, options.getOptionGroups().size());
        assertEquals("TestGroup", options.getOptionGroups().iterator().next().getName());
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("Group1", "Group 1");
        OptionGroup optionGroup2 = new OptionGroup("Group2", "Group 2");
        Options options = new Options();
        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);
        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option test");
        options.addOption(option);
        assertEquals(1, options.getOptions().size());
        assertEquals("o", options.getOptions().get(0).getKey());
    }

    @Test
    public void testAddOptionWithValue() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option test");
        options.addOption(option);
        assertEquals(1, options.getOptions().size());
        assertEquals("o", options.getOptions().get(0).getKey());
        assertTrue(options.getOptions().get(0).hasArg());
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("l", "longOpt", false, "Long option test");
        options.addOption(option);
        assertEquals(1, options.getOptions().size());
        assertEquals("longOpt", options.getOptions().get(0).getLongOpt());
    }

    @Test
    public void testAddOptionGroupWithOptions() {
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test Group");
        Option option1 = new Option("o1", "opt1", false, "Option 1 test");
        Option option2 = new Option("o2", "opt2", false, "Option 2 test");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option test");
        options.addOption(option);
        assertEquals(option, options.getOption("o"));
        assertEquals(option, options.getOption("opt"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1 test");
        Option option2 = new Option("o2", "opt2", false, "Option 2 test");
        Option option3 = new Option("o3", "opt3", false, "Option 3 test");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> matchingOptions = options.getMatchingOptions("o");
        assertEquals(2, matchingOptions.size());
        assertEquals("o1", matchingOptions.get(0));
        assertEquals("o", matchingOptions.get(1)); // short options don't include leading hyphen
        List<String> matchingOptions2 = options.getMatchingOptions("opt2");
        assertEquals(1, matchingOptions2.size());
        assertEquals("o2", matchingOptions2.get(0));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option test");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("opt"));
        assertFalse(options.hasOption("unknown"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("l", "longOpt", false, "Long option test");
        options.addOption(option);
        assertTrue(options.hasLongOption("longOpt"));
        assertFalse(
                options.hasLongOption("unknownLongOpt"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option test");
        options.addOption(option);
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("unknown"));
    }
}