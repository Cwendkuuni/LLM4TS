package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup_Empty() {
        Options options = new Options();

        Options result = options.addOptionGroup(new OptionGroup());

        assertTrue(result instanceof Options);
        assertNotNull(result.getOptionGroups());
        assertEquals(0, result.getOptionGroups().size());
    }

    @Test
    public void testAddOptionGroup_OneGroup() {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "opt", true, "Option with long name 'opt'");
        optionGroup.addOption(option);

        Options result = options.addOptionGroup(optionGroup);

        assertTrue(result instanceof Options);
        assertNotNull(result.getOptionGroups());
        assertEquals(1, result.getOptionGroups().size());
        OptionGroup group = result.getOptionGroups().iterator().next();
        assertEquals(option, group.getOptions().get(0));
    }

    @Test
    public void testAddOptionGroup_MultipleGroups() {
        Options options = new Options();

        OptionGroup optionGroup1 = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "Option with long name 'opt1'");
        optionGroup1.addOption(option1);

        OptionGroup optionGroup2 = new OptionGroup();
        Option option2 = new Option("o2", "opt2", false, "Option with long name 'opt2'");
        optionGroup2.addOption(option2);

        Options result = options.addOptionGroup(optionGroup1).addOptionGroup(optionGroup2);

        assertTrue(result instanceof Options);
        assertNotNull(result.getOptionGroups());
        assertEquals(2, result.getOptionGroups().size());
        List<OptionGroup> groups = result.getOptionGroups();
        OptionGroup group1 = groups.get(0);
        OptionGroup group2 = groups.get(1);
        assertEquals(option1, group1.getOptions().get(0));
        assertEquals(option2, group2.getOptions().get(0));
    }

    @Test
    public void testHasOption_ShortOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with short name 'o'");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testHasOption_LongOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with long name 'opt'");
        options.addOption(option);

        assertTrue(options.hasLongOption("opt"));
    }

    @Test
    public void testHasOption_MismatchedLongOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with long name 'opt'");
        options.addOption(option);

        assertFalse(options.hasLongOption("opt2"));
    }

    @Test
    public void testHasOption_MismatchedShortOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with long name 'opt'");
        options.addOption(option);

        assertFalse(options.hasOption("o1"));
    }

    @Test
    public void testGetOption_ShortOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with short name 'o'");
        options.addOption(option);

        Option result = options.getOption("o");

        assertEquals(option, result);
    }

    @Test
    public void testGetOption_LongOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with long name 'opt'");
        options.addOption(option);

        Option result = options.getOption("opt");

        assertEquals(option, result);
    }

    @Test
    public void testGetOption_MissingOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option with long name 'opt'");
        options.addOption(option);

        assertNull(options.getOption("opt2"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option with long name 'opt1'");
        Option option2 = new Option("o2", "opt2", false, "Option with long name 'opt2'");
        options.addOption(option1);
        options.addOption(option2);

        List<Option> optionsList = options.getOptions();

        assertEquals(2, optionsList.size());
        assertEquals(option1, optionsList.get(0));
        assertEquals(option2, optionsList.get(1));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option with long name 'opt'");
        options.addOption(option);

        List<Object> requiredOpts = options.getRequiredOptions();

        assertEquals(1, requiredOpts.size());
        assertEquals(option, requiredOpts.get(0));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option with long name 'opt1'");
        Option option2 = new Option("o2", "opt2", false, "Option with long name 'opt2'");
        Option option3 = new Option("o3", "opt3", false, "Option with long name 'opt3'");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("opt");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("opt1"));
        assertTrue(matchingOptions.contains("opt2"));
    }
}