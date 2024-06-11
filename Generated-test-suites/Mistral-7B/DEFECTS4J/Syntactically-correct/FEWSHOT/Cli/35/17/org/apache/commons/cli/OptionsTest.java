package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroupWithNullOptionGroup() {
        Options options = new Options();
        assertNull(options.addOptionGroup(null));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
        Options newOptions = options.addOptionGroup(optionGroup);
        assertEquals(options, newOptions);
        assertEquals(Collections.singleton(optionGroup), options.getOptionGroups());
    }

    @Test
    public void testAddOptionGroupWithDuplicateName() {
        Options options = new Options();
        OptionGroup optionGroup1 = new OptionGroup("testGroup", "Test Group 1");
        OptionGroup optionGroup2 = new OptionGroup("testGroup", "Test Group 2");
        options.addOptionGroup(optionGroup1);
        Options newOptions = options.addOptionGroup(optionGroup2);
        assertEquals(options, newOptions);
        assertEquals(Collections.singleton(optionGroup1), options.getOptionGroups());
    }

    @Test
    public void testAddOptionWithNullDescription() {
        Options options = new Options();
        assertNull(options.addOption("t", ""));
    }

    @Test
    public void testAddOptionWithEmptyDescription() {
        Options options = new Options();
        Option option = new Option("t", "", false, "");
        Option newOption = options.addOption(option);
        assertEquals(option, newOption);
        assertEquals("", newOption.getDescription());
    }

    @Test
    public void testAddOptionWithSingleCharacterShortOption() {
        Options options = new Options();
        Option option = new Option("t", "Test Option");
        Option newOption = options.addOption(option);
        assertEquals(option, newOption);
        assertEquals("Test Option", newOption.getDescription());
        assertEquals("t", newOption.getKey());
        assertEquals(false, newOption.hasArg());
        assertEquals(false, newOption.isRequired());
        assertEquals(Collections.emptyList(), newOption.getValues());
        assertEquals(Collections.singleton(option), options.helpOptions());
    }

    @Test
    public void testAddOptionWithSingleCharacterShortOptionRequireValue() {
        Options options = new Options();
        Option option = new Option("t", "Test Option", true, "TestValue");
        Option newOption = options.addOption(option);
        assertEquals(option, newOption);
        assertEquals("Test Option", newOption.getDescription());
        assertEquals("t", newOption.getKey());
        assertEquals(true, newOption.hasArg());
        assertEquals(true, newOption.isRequired());
        assertEquals(Collections.singletonList("TestValue"), newOption.getValues());
        assertEquals(Collections.singleton(option), options.helpOptions());
    }

    @Test
    public void testAddOptionWithSingleCharacterShortOptionNoValue() {
        Options options = new Options();
        Option option = new Option("t", "Test Option");
        Option newOption = options.addOption(option);
        assertEquals(option, newOption);
        assertEquals("Test Option", newOption.getDescription());
        assertEquals("t", newOption.getKey());
        assertEquals(false, newOption.hasArg());
        assertEquals(false, newOption.isRequired());
        assertEquals(Collections.emptyList(), newOption.getValues());
        assertEquals(Collections.singleton(option), options.helpOptions());
    }

    @Test
    public void testAddOptionWithShortOptionAndLongOption() {
        Options options = new Options();
        Option shortOption = new Option("t", "Test Short Option");
        Option longOption = new Option("testOption", "Test Long Option");
        Option newOption = options.addOption(shortOption);
        Option newLongOption = options.addOption(longOption);
        assertEquals(newOption, newLongOption);
        assertEquals("Test Short Option", newOption.getDescription());
        assertEquals("t", newOption.getKey());
        assertEquals("testOption", newLongOption.getKey());
        assertEquals(newOption, newLongOption);
        assertEquals(false, newOption.hasArg());
        assertEquals(false, newOption.isRequired());
        assertEquals(false, newLongOption.hasArg());
        assertEquals(false, newLongOption.isRequired());
        assertEquals(Collections.emptyList(), newOption.getValues());
        assertEquals(Collections.emptyList(), newLongOption.getValues());
        assertEquals(Collections.singleton(shortOption, longOption), options.helpOptions());
    }

    @Test
    public void testAddOptionWithShortOptionAndLongOptionValue() {
        Options options = new Options();
        Option shortOption = new Option("t", "Test Short Option");
        Option longOption = new Option("testOption", "Test Long Option", true, "TestValue");
        Option newOption = options.addOption(shortOption);
        Option newLongOption = options.addOption(longOption);
        assertEquals(newOption, newLongOption);
        assertEquals("Test Short Option", newOption.getDescription());
        assertEquals("t", newOption.getKey());
        assertEquals("testOption", newLongOption.getKey());
        assertEquals(newOption, newLongOption);
        assertEquals(true, newOption.hasArg());
        assertEquals(false, newOption.isRequired());
        assertEquals(true, newLongOption.hasArg());
        assertEquals(true, newLongOption.isRequired());
        assertEquals(Collections.singletonList("TestValue"), newLongOption.getValues());
        assertEquals(Collections.emptyList(), newOption.getValues());
        assertEquals(Collections.singleton(shortOption, longOption), options.helpOptions());
    }

    @Test
    public void testAddOptionWithShortOptionAndLongOptionNoValue() {
        Options options = new Options();
        Option shortOption = new Option("t", "Test Short Option");
        Option longOption = new Option("testOption", "Test Long Option");
        Option newOption = options.addOption(shortOption);
        Option newLongOption = options.addOption(longOption);
        assertEquals(newOption, newLongOption);
        assertEquals("Test Short Option", newOption.getDescription());
        assertEquals("t", newOption.getKey());
        assertEquals("testOption", newLongOption.getKey());
        assertEquals(newOption, newLongOption);
        assertEquals(false, newOption.hasArg());
        assertEquals(false, newOption.isRequired());
        assertEquals(false, newLongOption.hasArg());
        assertEquals(false, newLongOption.isRequired());
        assertEquals(Collections.emptyList(), newOption.getValues());
        assertEquals(Collections.emptyList(), newLongOption.getValues());
        assertEquals(Collections.singleton(shortOption, longOption), options.helpOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option shortOption = new Option("t", "Test Short Option");
        Option longOption = new Option("testOption", "Test Long Option");
        options.addOption(shortOption);
        options.addOption(longOption);
        assertEquals(options.helpOptions(), options.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("t", "Test Option", true, "TestValue");
        options.addOption(option);
        assertEquals(Collections.singletonList(option), options.getRequiredOptions());
    }

    @Test
    public void testGetOptionWithNullOpt() {
        Options options = new Options();
        assertNull(options.getOption(null));
    }

    @Test
    public void testGetOptionWithShortOption() {
        Options options = new Options();
        Option option = new Option("t", "Test Option");
        options.addOption(option);
        assertEquals(option, options.getOption("t"));
    }

    @Test
    public void testGetOptionWithLongOption() {
        Options options = new Options();
        Option option = new Option("testOption", "Test Option");
        options.addOption(option);
        assertEquals(option, options.getOption("testOption"));
    }

    @Test
    public void testGetOptionWithNonExistentOption() {
        Options options = new Options();
        assertNull(options.getOption("nonExistentOption"));
    }

    @Test
    public void testGetMatchingOptionsWithPartialMatch() {
        Options options = new Options();
        Option option1 = new Option("testOpt1", "Test Option 1");
        Option option2 = new Option("testopt2", "Test Option 2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> optionsWithPartialMatch = options.getMatchingOptions("testopt");
        assertEquals(Collections.singletonList("testopt1", "testopt2"), optionsWithPartialMatch);
    }

    @Test
    public void testHasOptionWithShortOption() {
        Options options = new Options();
        Option option = new Option("t", "Test Option");
        options.addOption(option);
        assertTrue(options.hasOption("t"));
    }

    @Test
    public void testHasOptionWithLongOption() {
        Options options = new Options();
        Option option = new Option("testOption", "Test Option");
        options.addOption(option);
        assertTrue(options.hasOption("testOption"));
    }

    @Test
    public void testHasOptionWithNonExistentOption() {
        Options options = new Options();
        assertFalse(options.hasOption("nonExistentOption"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("t", "Test Option");
        options.addOption(option);
        assertTrue(options.hasShortOption("t"));
    }

    @Test
    public void testHasShortOptionWithNonExistentOption() {
        Options options = new Options();
        assertFalse(options.hasShortOption("nonExistentOption"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("testOption", "Test Option");
        options.addOption(option);
        assertTrue(options.hasLongOption("testOption"));
    }

    @Test
    public void testHasLongOptionWithNonExistentOption() {
        Options options = new Options();
        assertFalse(options.hasLongOption("nonExistentOption"));
    }

    @Test
    public void testGetOptionGroupWithNullOpt() {
        Options options = new Options();
        assertNull(options.getOptionGroup(null));
    }

    @Test
    public void testGetOptionGroupWithExistingOption() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
        Option option = new Option("testOption", "Test Option", false, "TestValue");
        option.setOptionGroup(optionGroup);
        options.addOption(option);
        assertEquals(optionGroup, options.getOptionGroup(option));
    }

    @Test
    public void testGetOptionGroupWithNonExistentOption() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
        Option option = new Option("testOption", "Test Option", false, "TestValue");
        options.addOption(option);
        assertNull(options.getOptionGroup(new Option("nonExistentOption", "Non Existent Option")));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option shortOption = new Option("t", "Test Short Option");
        Option longOption = new Option("testOption", "Test Long Option");
        options.addOption(shortOption);
        options.addOption(longOption);
        assertEquals("[ Options: [ short {t=Test Short Option} ] [ long {testOption=Test Long Option} ]]", options.toString());
    }
}