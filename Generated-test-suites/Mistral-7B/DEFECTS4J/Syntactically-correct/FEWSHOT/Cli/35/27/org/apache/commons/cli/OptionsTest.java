package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test Group Options");
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        assertNotNull(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("TestGroup1", "Test Group 1 Options");
        OptionGroup optionGroup2 = new OptionGroup("TestGroup2", "Test Group 2 Options");

        Options options = new Options();
        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();

        assertTrue(optionGroups.size() == 2);
        assertTrue(optionGroups.contains(optionGroup1));
        assertTrue(optionGroups.contains(optionGroup2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option option1 = new Option("o", "option1", false, "Option 1 Description");
        Option option2 = new Option("l", "long-option", true, "Long Option Description");

        options.addOption(option1);
        options.addOption(option2);

        List<Option> optionsList = options.helpOptions();

        assertEquals(2, optionsList.size());
        assertTrue(optionsList.contains(option1));
        assertTrue(optionsList.contains(option2));
    }

    @Test
    public void testAddShortOption() {
        Options options = new Options();

        Option option = new Option('o', "option", false, "Option Description");

        options.addOption(option);

        List<Option> optionsList = options.helpOptions();

        assertEquals(1, optionsList.size());
        assertTrue(optionsList.get(0).isSet());
        assertTrue(optionsList.get(0).getOpt() == 'o');
    }

    @Test
    public void testAddLongOption() {
        Options options = new Options();

        Option option = new Option(null, "long-option", false, "Long Option Description");

        options.addOption(option);

        List<Option> optionsList = options.helpOptions();

        assertEquals(1, optionsList.size());
        assertFalse(optionsList.get(0).isSet());
        assertEquals("long-option", optionsList.get(0).getLongOpt());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option option = new Option('o', "option", false, "Option Description");

        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("nonexistent"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        Option option = new Option(null, "long-option", false, "Long Option Description");
        options.addOption(option);

        assertTrue(options.hasLongOption("long-option"));
        assertFalse(options.hasLongOption("nonexistent"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        Option option = new Option('o', "option", false, "Option Description");
        options.addOption(option);

        assertTrue(options.hasShortOption('o'));
        assertFalse(options.hasShortOption('n'));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option option = new Option('o', "option", false, "Option Description");
        options.addOption(option);

        Option retrievedOption = options.getOption("option");
        Option retrievedOptionWithShort = options.getOption('o');

        assertEquals(option, retrievedOption);
        assertEquals(option, retrievedOptionWithShort);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option option1 = new Option("o1", "option1", false, "Option 1 Description");
        Option option2 = new Option("o2", "option2", false, "Option 2 Description");
        Option option3 = new Option("o1", "option1Long", false, "Option 1 Long Description");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("o1");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("option1"));
        assertTrue(matchingOptions.contains("option1Long"));
    }
}