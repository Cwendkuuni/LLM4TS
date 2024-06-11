package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("group", "group options");
        Option option = new Option("o", "opt", true, "optional option");

        options.addOptionGroup(optionGroup);
        optionGroup.addOption(option);

        assertTrue(options.hasOption("o"));
        assertSame(option, options.getOption("o"));
        assertTrue(options.hasOption("group"));
        assertSame(optionGroup, options.getOptionGroup("group"));
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("group1", "group options 1");
        Option option1 = new Option("o1", "opt1", true, "option 1");
        OptionGroup optionGroup2 = new OptionGroup("group2", "group options 2");
        Option option2 = new Option("o2", "opt2", true, "option 2");

        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);
        optionGroup1.addOption(option1);
        optionGroup2.addOption(option2);

        List<OptionGroup> optionGroups = options.getOptionGroups();

        assertTrue(optionGroups.contains(optionGroup1));
        assertTrue(optionGroups.contains(optionGroup2));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", true, "option");

        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertSame(option, options.getOption("o"));
    }

    @Test
    public void testAddOptionWithDescription() {
        Option option = new Option("o", "opt", true, "option with description");

        options.addOption(option);

        assertEquals(Arrays.asList("option with description"), options.getOptionDescription("o"));
    }

    @Test
    public void testAddOptionWithHasArg() {
        Option option = new Option("o", "opt", false, "option without argument");

        options.addOption(option);

        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionWithHasArgAndValue() {
        Option option = new Option("o", "opt", true, "option with argument");

        options.addOption(option);

        assertTrue(option.hasArg());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionWithValue() {
        Option option = new Option("o", "opt", true, "option with argument", "default");

        options.addOption(option);

        assertTrue(option.hasArg());
        assertEquals("default", option.getValue());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Option option = new Option("o", null, true, "long option");

        options.addOption(option);

        assertTrue(options.hasLongOption("o"));
        assertSame(option, options.getOption("o"));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", null, true, "option");
        options.addOption(option);

        List<Option> optionsList = options.getOptions();

        assertTrue(optionsList.contains(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("o", null, true, "required option");
        options.addOption(option);

        List<Option> requiredOptions = options.getRequiredOptions();

        assertTrue(requiredOptions.contains(option));
    }

    @Test
    public void testGetOptionWithShortName() {
        Option option = new Option("o", null, true, "option");
        options.addOption(option);

        Option retrievedOption = options.getOption("o");

        assertSame(option, retrievedOption);
    }

    @Test
    public void testGetOptionWithLongName() {
        Option option = new Option(null, "longOption", true, "long option");
        options.addOption(option);

        Option retrievedOption = options.getOption("longOption");

        assertSame(option, retrievedOption);
    }

    @Test
    public void testGetOptionWithMissingName() {
        Option option = new Option(null, "missingOption", true, "missing option");
        options.addOption(option);

        assertNull(options.getOption("missingOption"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("short", null, true, "short option 1");
        Option option2 = new Option("shortLong", null, true, "short option 2");
        Option option3 = new Option("long", "longOption", true, "long option");
        Option option4 = new Option("longLong", "longLongOption", true, "long long option");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        List<String> matchingOptions = options.getMatchingOptions("short");

        assertTrue(matchingOptions.size() > 1);
        assertTrue(matchingOptions.contains("short"));
        assertTrue(matchingOptions.contains("shortLong"));

        List<String> emptyMatchingOptions = options.getMatchingOptions("empty");

        assertTrue(emptyMatchingOptions.isEmpty());
    }

    @Test
    public void testHasOptionWithShortName() {
        Option option = new Option("o", null, true, "option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("missing"));
    }

    @Test
    public void testHasOptionWithLongName() {
        Option option = new Option(null, "longOption", true, "long option");
        options.addOption(option);

        assertTrue(options.hasLongOption("longOption"));
        assertFalse(options.hasLongOption("missing"));
    }

    @Test
    public void testHasOptionWithMissingName() {
        Option option = new Option("o", null, true, "option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("missing"));
    }

    @Test
    public void testGetOptionGroupWithOption() {
        OptionGroup optionGroup = new OptionGroup("group", "group options");
        Option option = new Option("o", null, true, "option");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);

        OptionGroup retrievedOptionGroup = options.getOptionGroup(option);

        assertSame(optionGroup, retrievedOptionGroup);
    }

    @Test
    public void testGetOptionGroupWithMissingOption() {
        OptionGroup optionGroup = new OptionGroup("group", "group options");
        options.addOptionGroup(optionGroup);

        assertNull(options.getOptionGroup(new Option("missing", null, true, "missing option")));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", null, true, "option");
        options.addOption(option);

        String toString = options.toString();

        assertTrue(toString.contains("[ Options: [ short {o=} ] [ long {} ] ]"));
    }
}