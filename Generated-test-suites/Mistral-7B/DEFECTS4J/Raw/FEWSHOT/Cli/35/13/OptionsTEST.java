package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertIterableEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test group");

        Options options = new Options();
        Options result = options.addOptionGroup(optionGroup);

        assertEquals(options, result);
        assertEquals(1, options.getOptionGroups().size());
        assertSame(optionGroup, options.getOptionGroups().iterator().next());
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("testGroup1", "Test group 1");
        OptionGroup optionGroup2 = new OptionGroup("testGroup2", "Test group 2");

        Options options = new Options();
        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);

        List<OptionGroup> optionGroups = options.getOptionGroups();

        assertEquals(2, optionGroups.size());
        assertSame(optionGroup1, optionGroups.get(0));
        assertSame(optionGroup2, optionGroups.get(1));
    }

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");
        Option opt2 = new Option("o", "option2", false, "Short option 2");

        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> optionsList = options.helpOptions();
        List<Option> expectedOptions = Arrays.asList(opt1, opt2);

        assertIterableEquals(expectedOptions, optionsList);
    }

    @Test
    public void testAddOptionWithShortNameAndDescription() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");

        options.addOption(opt1);

        List<Option> optionsList = options.helpOptions();
        List<Option> expectedOptions = Arrays.asList(opt1);

        assertIterableEquals(expectedOptions, optionsList);
    }

    @Test
    public void testAddOptionWithShortNameAndArgument() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", true, "Short option 1 with argument");

        options.addOption(opt1);

        List<Option> optionsList = options.helpOptions();
        List<Option> expectedOptions = Arrays.asList(opt1);

        assertIterableEquals(expectedOptions, optionsList);
    }

    @Test
    public void testAddOptionWithShortNameLongName() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");
        Option opt2 = new Option("longOption1", "Long option 1", false, "Long option 1");

        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> optionsList = options.helpOptions();
        List<Option> expectedOptions = Arrays.asList(opt1, opt2);

        assertIterableEquals(expectedOptions, optionsList);
    }

    @Test
    public void testAddOptionWithShortNameLongNameAndDescription() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");
        Option opt2 = new Option("longOption1", "Long option 1", false, "Long option 1 description");

        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> optionsList = options.helpOptions();
        List<Option> expectedOptions = Arrays.asList(opt1, opt2);

        assertIterableEquals(expectedOptions, optionsList);
    }

    @Test
    public void testAddOptionWithShortNameLongNameAndArgument() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", true, "Short option 1 with argument");
        Option opt2 = new Option("longOption1", "Long option 1 with argument", true, "Long option 1 with argument description");

        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> optionsList = options.helpOptions();
        List<Option> expectedOptions = Arrays.asList(opt1, opt2);

        assertIterableEquals(expectedOptions, optionsList);
    }

    @Test
    public void testAddOptionWithShortNameAndValue() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", true, "Short option 1 with value");
        opt1.setValue("value1");

        options.addOption(opt1);

        List<Option> optionsList = options.helpOptions();
        List<Option> expectedOptions = Arrays.asList(opt1);

        assertIterableEquals(expectedOptions, optionsList);

        assertEquals("value1", opt1.getValue());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");
        Option opt2 = new Option("o", "option2", false, "Short option 2");

        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> expectedOptions = Arrays.asList(opt1, opt2);

        List<Option> optionsList = options.getOptions();

        assertIterableEquals(expectedOptions, optionsList);
    }

    @Test
    public void testRequiredOptions() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");
        Option opt2 = new Option("o", "option2", false, "Short option 2");

        options.addOption(opt1);
        options.addOption(opt2);

        List<Object> requiredOptions = options.getRequiredOptions();

        assertEquals(0, requiredOptions.size());

        opt1.setRequired(true);
        options.addOption(opt1);

        requiredOptions = options.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertSame(opt1, requiredOptions.get(0));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");

        options.addOption(opt1);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("a"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");

        options.addOption(opt1);

        assertFalse(options.hasLongOption("longOption1"));

        opt1.setLongOpt("longOption1");

        assertTrue(options.hasLongOption("longOption1"));
        assertFalse(options.hasLongOption("longOption2"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");

        options.addOption(opt1);

        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("a"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option opt1 = new Option("o", "option1", false, "Short option 1");

        options.addOption(opt1);

        Option retrievedOption = options.getOption("o");

        assertEquals(opt1, retrievedOption);

        Option retrievedOption2 = options.getOption("a");

        assertNull(retrievedOption2);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option opt1 = new Option("opt", "option1", false, "Short option 1");
        Option opt2 = new Option("optB", "optionB1", false, "Short option B1");
        Option opt3 = new Option("optC", "optionC1", false, "Short option C1");

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        List<String> matchingOptions = options.getMatchingOptions("opt");

        List<String> expectedOptions = Arrays.asList("opt", "optB", "optC");

        assertIterableEquals(expectedOptions, matchingOptions);
    }
}