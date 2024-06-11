package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testOptionsEmpty() {
        Options options = new Options();
        assertTrue(options.getOptions().isEmpty());
        assertTrue(options.getRequiredOptions().isEmpty());
    }

    @Test
    public void testOptionsAddSingleShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option A");
        options.addOption(option);

        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option optionFromList = optionsList.get(0);
        assertEquals(option, optionFromList);

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddSingleLongOption() {
        Options options = new Options();
        Option option = new Option("--option-b", "option-b", false, "Description for option B");
        options.addOption(option);

        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option optionFromList = optionsList.get(0);
        assertEquals(option, optionFromList);

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddSingleShortOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", "option-a", true, "Description for option A with argument");
        options.addOption(option);

        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option optionFromList = optionsList.get(0);
        assertEquals(option, optionFromList);

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddSingleLongOptionWithArg() {
        Options options = new Options();
        Option option = new Option("--option-c", "option-c", true, "Description for option C with argument");
        options.addOption(option);

        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option optionFromList = optionsList.get(0);
        assertEquals(option, optionFromList);

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddOptionWithGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group for options");
        Option option = new Option("g", "option-g", false, "Description for option G");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);

        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option optionFromList = optionsList.get(0);
        assertEquals(option, optionFromList);

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddGroupWithOption() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group for options");
        Option option = new Option("g", "option-g", false, "Description for option G");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);

        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option optionFromList = optionsList.get(0);
        assertEquals(option, optionFromList);

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddMultipleOptions() {
        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "Description for option A");
        Option optionB = new Option("b", "option-b", false, "Description for option B");
        options.addOption(optionA);
        options.addOption(optionB);

        List<Option> optionsList = options.getOptions();
        assertEquals(2, optionsList.size());

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddMultipleOptionsWithArg() {
        Options options = new Options();
        Option optionA = new Option("a", "option-a", true, "Description for option A with argument");
        Option optionB = new Option("b", "option-b", true, "Description for option B with argument");
        options.addOption(optionA);
        options.addOption(optionB);

        List<Option> optionsList = options.getOptions();
        assertEquals(2, optionsList.size());

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsAddMultipleGroupsWithOptions() {
        Options options = new Options();
        OptionGroup groupA = new OptionGroup("groupA", "Group A for options");
        Option optionA = new Option("a", "option-a", false, "Description for option A");
        groupA.addOption(optionA);
        OptionGroup groupB = new OptionGroup("groupB", "Group B for options");
        Option optionB = new Option("b", "option-b", false, "Description for option B");
        groupB.addOption(optionB);
        options.addOptionGroup(groupA);
        options.addOptionGroup(groupB);

        List<Option> optionsList = options.getOptions();
        assertEquals(2, optionsList.size());

        List<String> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testOptionsGetOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option A");
        options.addOption(option);

        Option optionFromOptions = options.getOption("a");
        assertEquals(option, optionFromOptions);

        Option optionFromOptionsNull = options.getOption("z");
        assertNull(optionFromOptionsNull);
    }

    @Test
    public void testOptionsGetMatchingOptions() {
        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "Description for option A");
        Option optionB = new Option("b", "option-b", false, "Description for option B");
        Option optionC = new Option("c", "option-c", false, "Description for option C");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(1, matchingOptions.size());
        assertEquals(optionA.getKey(), matchingOptions.get(0));

        List<String> matchingOptionsNull = options.getMatchingOptions("z");
        assertTrue(matchingOptionsNull.isEmpty());

        List<String> matchingOptionsPartial = options.getMatchingOptions("c");
        assertEquals(1, matchingOptionsPartial.size());
        assertEquals(optionC.getKey(), matchingOptionsPartial.get(0));
    }

    @Test
    public void testOptionsHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option A");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("z"));
    }

    @Test
    public void testOptionsHasLongOption() {
        Options options = new Options();
        Option option = new Option("--option-b", "option-b", false, "Description for option B");
        options.addOption(option);

        assertTrue(options.hasLongOption("--option-b"));
        assertFalse(options.hasLongOption("--option-c"));
    }

    @Test
    public void testOptionsHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "Description for option A");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("z"));
    }

    @Test
    public void testOptionsGetOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group for options");
        Option option = new Option("g", "option-g", false, "Description for option G");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);

        OptionGroup optionGroupFromOptions = options.getOptionGroup(option);
        assertEquals(optionGroup, optionGroupFromOptions);

        OptionGroup optionGroupFromOptionsNull = options.getOptionGroup(new Option("z", "option-z", false, "Option Z"));
        assertNull(optionGroupFromOptionsNull);
    }
}