package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testOptionsWithEmptyShortOpts() {
        Options options = new Options();
        assertNull(options.getOptions());
        assertEquals(Collections.emptyList(), options.helpOptions());
    }

    @Test
    public void testOptionsWithSingleShortOpt() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        assertEquals(Collections.singletonList(option), options.helpOptions());
    }

    @Test
    public void testOptionsWithSingleShortOptWithArg() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with argument");
        options.addOption(option);
        assertEquals(Collections.singletonList(option), options.helpOptions());
    }

    @Test
    public void testOptionsWithSingleShortOptWithoutArg() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        assertEquals(Collections.singletonList(option), options.helpOptions());
    }

    @Test
    public void testOptionsWithMultipleShortOpts() {
        Options options = new Options();
        Option option1 = new Option("o", "option1");
        Option option2 = new Option("o", "option2");
        options.addOption(option1);
        options.addOption(option2);
        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());
        assertEquals(option1, optionsList.get(0));
        assertEquals(option2, optionsList.get(1));
    }

    @Test
    public void testOptionsWithMultipleShortOptsWithSameLetterAndDifferentLongOpt() {
        Options options = new Options();
        Option option1 = new Option("o", "option1");
        Option option2 = new Option("o", "option2");
        options.addOption(option1);
        options.addOption(option2);
        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());
        assertNotSame(option1, option2);
    }

    @Test
    public void testOptionsWithDuplicateShortOpt() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        assertThrows(IllegalArgumentException.class, () -> options.addOption(option));
    }

    @Test
    public void testOptionsWithLongOpt() {
        Options options = new Options();
        Option option = new Option("l", "longOption", true, "A long option");
        options.addOption(option);
        assertEquals(Collections.singletonList(option), options.helpOptions());
    }

    @Test
    public void testOptionsWithLongOptWithArg() {
        Options options = new Options();
        Option option = new Option("l", "longOption", true, "A long option with argument");
        options.addOption(option);
        assertEquals(Collections.singletonList(option), options.helpOptions());
    }

    @Test
    public void testOptionsWithLongOptWithoutArg() {
        Options options = new Options();
        Option option = new Option("l", "longOption");
        options.addOption(option);
        assertEquals(Collections.singletonList(option), options.helpOptions());
    }

    @Test
    public void testOptionsWithMultipleLongOpts() {
        Options options = new Options();
        Option option1 = new Option("l", "longOption1");
        Option option2 = new Option("l", "longOption2");
        options.addOption(option1);
        options.addOption(option2);
        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());
        assertEquals(option1, optionsList.get(0));
        assertEquals(option2, optionsList.get(1));
    }

    @Test
    public void testOptionsWithMultipleShortOptsWithSameLetterAndSameLongOpt() {
        Options options = new Options();
        Option option1 = new Option("o", "option");
        Option option2 = new Option("o", "option");
        options.addOption(option1);
        options.addOption(option2);
        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());
        assertNotSame(option1, option2);
    }

    @Test
    public void testOptionsWithMultipleShortOptsAndLongOptWithSameLetter() {
        Options options = new Options();
        Option option1 = new Option("o", "option1");
        Option option2 = new Option("o", "option2");
        Option option3 = new Option("o", "longOption");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<Option> optionsList = options.helpOptions();
        assertEquals(3, optionsList.size());
        assertNotSame(option1, option2);
        assertNotSame(option1, option3);
        assertNotSame(option2, option3);
    }

    @Test
    public void testOptionsWithOptionGroup() {
        OptionGroup group = new OptionGroup("group", "This is a group");
        Option option = new Option("g", "groupOption", true, "An option in the group");
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);
        List<Option> optionsList = options.helpOptions();
        assertEquals(1, optionsList.size());
        Option groupOption = optionsList.get(0);
        assertEquals(group, groupOption.getParent());
        assertEquals(option, groupOption);
    }

    @Test
    public void testOptionsWithMultipleOptionGroups() {
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        Option option = new Option("g", "groupOption", true, "An option in a group");
        group1.addOption(option);
        group2.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        List<Option> optionsList = options.helpOptions();
        assertEquals(1, optionsList.size());
        Option groupOption = optionsList.get(0);
        assertNull(groupOption.getParent());
        assertEquals(option, groupOption);
    }

    @Test
    public void testOptionsWithRequiredOption() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        options.addOption("requiredOption", true, "A required option");
        assertThrows(IllegalArgumentException.class, () -> options.addOption("requiredOption"));
        assertThrows(IllegalArgumentException.class, () -> options.addOption(option, "requiredOption"));
    }

    @Test
    public void testOptionsWithMultipleRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption("requiredOption1", true, "A required option 1");
        assertFalse(options.hasOption("requiredOption1"));
        options.addOption("requiredOption2", true, "A required option 2");
        assertFalse(options.hasOption("requiredOption2"));
        options.addOption("requiredOption1", true, "A required option 1");
        assertTrue(options.hasOption("requiredOption1"));
        assertFalse(options.hasOption("requiredOption2"));
        options.addOption("requiredOption2", true, "A required option 2");
        assertTrue(options.hasOption("requiredOption1"));
        assertTrue(options.hasOption("requiredOption2"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        Option retrievedOption = options.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("l", "longOption");
        options.addOption(option);
        Option retrievedOption = options.getOption("longOption");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetOptionWithMissingName() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        assertNull(options.getOption("missingOption"));
    }

    @Test
    public void testGetOptionWithMissingNameAndMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        options.addOption(option1);
        options.addOption(option2);
        assertNull(options.getOption("missingOption"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("l", "longOption1");
        Option option2 = new Option("l", "longOption2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("l");
        assertEquals(2, matchingOptions.size());
        assertEquals(option1.getLongOpt(), matchingOptions.get(0));
        assertEquals(option2.getLongOpt(), matchingOptions.get(1));
    }

    @Test
    public void testGetMatchingOptionsWithEmptyString() {
        Options options = new Options();
        Option option1 = new Option("l", "longOption1");
        Option option2 = new Option("l", "longOption2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testGetMatchingOptionsWithPartialName() {
        Options options = new Options();
        Option option1 = new Option("l1", "longOption1");
        Option option2 = new Option("l2", "longOption2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("l");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains(option1.getLongOpt()));
        assertTrue(matchingOptions.contains(option2.getLongOpt()));
    }

    @Test
    public void testGetMatchingOptionsWithPartialNameAndMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("l1", "longOption1");
        Option option2 = new Option("l2", "longOption2");
        Option option3 = new Option("l3", "longOption3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> matchingOptions = options.getMatchingOptions("l");
        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains(option1.getLongOpt()));
        assertTrue(matchingOptions.contains(option2.getLongOpt()));
        assertTrue(matchingOptions.contains(option3.getLongOpt()));
    }

    @Test
    public void testGetMatchingOptionsWithLongName() {
        Options options = new Options();
        Option option1 = new Option("l1", "longOption1");
        Option option2 = new Option("l2", "longOption2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("longOption");
        assertEquals(1, matchingOptions.size());
        assertEquals(option1.getLongOpt(), matchingOptions.get(0));
    }

    @Test
    public void testGetMatchingOptionsWithLongNameAndMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("l1", "longOption1");
        Option option2 = new Option("l2", "longOption2");
        Option option3 = new Option("l3", "longOption3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> matchingOptions = options.getMatchingOptions("longOption");
        assertEquals(1, matchingOptions.size());
        assertEquals(option1.getLongOpt(), matchingOptions.get(0));
    }

    @Test
    public void testGetMatchingOptionsWithLongNameAndMissingOption() {
        Options options = new Options();
        Option option1 = new Option("l1", "longOption1");
        Option option2 = new Option("l2", "longOption2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("longMissingOption");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        String expected = "[ Options: [ short {o=>Option(o, null, false, An option)} ] [ long {} ]]";
        assertEquals(expected, options.toString());
    }

    @Test
    public void testToStringWithShortAndLongOption() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        options.addOption(option1);
        options.addOption(option2);
        String expected = "[ Options: [ short {o1=>Option(o1, null, false, Option 1)}, o=>Option(o, null, false, An option) ], long {} ]]";
        assertEquals(expected, options.toString());
    }
}