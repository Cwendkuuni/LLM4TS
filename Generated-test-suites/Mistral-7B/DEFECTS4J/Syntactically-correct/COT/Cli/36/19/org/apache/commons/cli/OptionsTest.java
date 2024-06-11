package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

/**
 * OptionsTest class for testing the Options class in the Apache Commons CLI library.
 */
public class OptionsTest {

    /**
     * Test the addOption method when an option with a short name is added.
     */
    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);
        assertEquals("OptionA", options.getOptions().get(0).getLongOpt());
        assertEquals("a", options.getOptions().get(0).getKey());
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));
    }

    /**
     * Test the addOption method when an option with a long name is added.
     */
    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("longOptionA", "LongOptionA", false, "Description for long option A");
        options.addOption(option);
        assertEquals("longOptionA", options.getOptions().get(0).getLongOpt());
        assertEquals("longOptionA", options.getOptions().get(0).getKey());
        assertTrue(options.hasOption("longOptionA"));
        assertFalse(options.hasOption("a"));
    }

    /**
     * Test the addOption method when an option with a short name and value is added.
     */
    @Test
    public void testAddOptionWithShortNameAndValue() {
        Options options = new Options();
        Option option = new Option("b", "optionB", true, "Description for option B");
        options.addOption(option);
        assertEquals("optionB", options.getOptions().get(0).getLongOpt());
        assertEquals("b", options.getOptions().get(0).getKey());
        assertEquals("", options.getOptions().get(0).getArgName());
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("optionB"));
    }

    /**
     * Test the addOption method when an option with a short name and value is added.
     */
    @Test
    public void testAddOptionWithShortNameAndValueArg() {
        Options options = new Options();
        Option option = new Option("c", "optionC", true, "Description for option C")
                .argName("value");
        options.addOption(option);
        assertEquals("optionC", options.getOptions().get(0).getLongOpt());
        assertEquals("c", options.getOptions().get(0).getKey());
        assertEquals("value", options.getOptions().get(0).getArgName());
        assertTrue(options.hasOption("c"));
        assertTrue(options.hasOption("optionC"));
    }

    /**
     * Test the addOptionGroup method with an OptionGroup that has no required options.
     */
    @Test
    public void testAddOptionGroupWithNoRequiredOptions() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group1", "Group 1", true);
        Option option = new Option("g1", "optionG1", false, "Description for option G1");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);
        assertEquals(1, options.getOptions().size());
        assertEquals(1, options.getOptionGroups().size());
        assertTrue(options.hasOption("g1"));
        assertFalse(options.getRequiredOptions().isEmpty());
    }

    /**
     * Test the addOptionGroup method with an OptionGroup that has required options.
     */
    @Test
    public void testAddOptionGroupWithRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("r1", "optionR1", false, "Description for option R1");
        Option option2 = new Option("r2", "optionR2", false, "Description for option R2");
        OptionGroup optionGroup = new OptionGroup("group1", "Group 1", true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        assertEquals(3, options.getOptions().size());
        assertEquals(1, options.getOptionGroups().size());
        assertTrue(options.hasOption("r1"));
        assertTrue(options.hasOption("r2"));
        assertEquals(option1, options.getOptions().get(1));
        assertEquals(option2, options.getOptions().get(2));
    }

    /**
     * Test the getOptionGroups method.
     */
    @Test
    public void testGetOptionGroups() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group1", "Group 1", true);
        Option option = new Option("g1", "optionG1", false, "Description for option G1");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);
        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(1, optionGroups.size());
        assertEquals(optionGroup, optionGroups.get(0));
    }

    /**
     * Test the getRequiredOptions method.
     */
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("r1", "optionR1", false, "Description for option R1");
        Option option2 = new Option("r2", "optionR2", false, "Description for option R2");
        Option option3 = new Option("r3", "optionR3", false, "Description for option R3");
        OptionGroup optionGroup = new OptionGroup("group1", "Group 1", true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        options.addOption(option3);
        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(0, requiredOptions.size());
    }

    /**
     * Test the hasOption method with a valid option.
     */
    @Test
    public void testHasOptionWithValidOption() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));
    }

    /**
     * Test the hasOption method with an invalid option.
     */
    @Test
    public void testHasOptionWithInvalidOption() {
        Options options = new Options();
        assertFalse(options.hasOption("b"));
        assertFalse(options.hasOption("invalidOption"));
    }

    /**
     * Test the hasLongOption method with a valid long option.
     */
    @Test
    public void testHasLongOptionWithValidOption() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);
        assertTrue(options.hasLongOption("optionA"));
        assertFalse(options.hasLongOption("invalidOption"));
    }

    /**
     * Test the hasShortOption method with a valid short option.
     */
    @Test
    public void testHasShortOptionWithValidOption() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("invalidOption"));
    }

    /**
     * Test the getOption method with a valid option key.
     */
    @Test
    public void testGetOptionWithValidKey() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);
        Option retrievedOption = options.getOption("a");
        assertEquals(option, retrievedOption);
    }

    /**
     * Test the getOption method with an invalid option key.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetOptionWithInvalidKey() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);
        options.getOption("invalidOption");
    }

    /**
     * Test the getMatchingOptions method with a valid partial option key.
     */
    @Test
    public void testGetMatchingOptionsWithValidPartialKey() {
        Options options = new Options();
        Option option1 = new Option("option1", "Option 1", false, "Description for option 1");
        Option option2 = new Option("option2", "Option 2", false, "Description for option 2");
        Option option3 = new Option("option3", "Option 3", false, "Description for option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> matchingOptions = options.getMatchingOptions("option");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("option1"));
        assertTrue(matchingOptions.contains("option2"));
        assertFalse(matchingOptions.contains("option3"));
    }
}