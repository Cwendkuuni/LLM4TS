package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testOptionsWithEmptyMaps() {
        Options options = new Options();
        assertEquals(Collections.emptyMap(), options.getShortOpts());
        assertEquals(Collections.emptyMap(), options.getLongOpts());
        assertEquals(Collections.emptyList(), options.getRequiredOpts());
        assertEquals(Collections.emptyMap(), options.getOptionGroups());
    }

    @Test
    public void testOptionsWithSingleOption() {
        String optionKey = "option1";
        String shortOpt = "o";
        String longOpt = "option1";
        String description = "Option 1 description";

        Options options = new Options();
        Option option = new Option(shortOpt, longOpt, false, description);
        options.addOption(option);

        assertEquals(1, options.getOptions().size());
        Option retrievedOption = options.getOption(optionKey);
        assertSame(option, retrievedOption);
        assertTrue(options.hasOption(optionKey));
        assertTrue(options.hasOption(shortOpt));
        assertTrue(options.hasOption(longOpt));
        assertFalse(options.hasOption("nonexistent"));
        List<String> optionsWithLongOptStartingWith = options.getMatchingOptions("option");
        assertEquals(Collections.singletonList(longOpt), optionsWithLongOptStartingWith);
    }

    @Test
    public void testOptionsWithSingleOptionGroup() {
        String optionGroupKey = "group1";
        String groupDescription = "Group 1 description";

        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup(groupDescription, "group1");
        options.addOptionGroup(optionGroup);

        assertEquals(1, options.getOptionGroups().size());
        OptionGroup retrievedOptionGroup = options.getOptionGroup(optionGroupKey);
        assertSame(optionGroup, retrievedOptionGroup);
        assertTrue(options.hasOptionGroup(optionGroupKey));
    }

    @Test
    public void testOptionsWithMultipleOptions() {
        String optionKey1 = "option1";
        String shortOpt1 = "o1";
        String longOpt1 = "option1";
        String description1 = "Option 1 description";

        String optionKey2 = "option2";
        String shortOpt2 = "o2";
        String longOpt2 = "option2";
        String description2 = "Option 2 description";

        Options options = new Options();
        Option option1 = new Option(shortOpt1, longOpt1, false, description1);
        Option option2 = new Option(shortOpt2, longOpt2, false, description2);
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        Option retrievedOption1 = options.getOption(optionKey1);
        assertSame(option1, retrievedOption1);
        Option retrievedOption2 = options.getOption(optionKey2);
        assertSame(option2, retrievedOption2);
        assertTrue(options.hasOption(optionKey1));
        assertTrue(options.hasOption(optionKey2));
        assertTrue(options.hasOption(shortOpt1));
        assertTrue(options.hasOption(shortOpt2));
        assertTrue(options.hasOption(longOpt1));
        assertTrue(options.hasOption(longOpt2));
        List<String> optionsWithLongOptStartingWith = options.getMatchingOptions("option");
        assertEquals(Arrays.asList(longOpt1, longOpt2), optionsWithLongOptStartingWith);
    }

    @Test
    public void testOptionsWithMultipleOptionGroups() {
        String optionGroupKey1 = "group1";
        String groupDescription1 = "Group 1 description";

        String optionGroupKey2 = "group2";
        String groupDescription2 = "Group 2 description";

        Options options = new Options();
        OptionGroup optionGroup1 = new OptionGroup(groupDescription1, "group1");
        OptionGroup optionGroup2 = new OptionGroup(groupDescription2, "group2");
        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);

        assertEquals(2, options.getOptionGroups().size());
        OptionGroup retrievedOptionGroup1 = options.getOptionGroup(optionGroupKey1);
        assertSame(optionGroup1, retrievedOptionGroup1);
        OptionGroup retrievedOptionGroup2 = options.getOptionGroup(optionGroupKey2);
        assertSame(optionGroup2, retrievedOptionGroup2);
        assertTrue(options.hasOptionGroup(optionGroupKey1));
        assertTrue(options.hasOptionGroup(optionGroupKey2));
    }

    @Test
    public void testOptionsWithMixedOptionsAndOptionGroups() {
        String optionKey1 = "option1";
        String shortOpt1 = "o1";
        String longOpt1 = "option1";
        String description1 = "Option 1 description";

        String optionGroupKey1 = "group1";
        String groupDescription1 = "Group 1 description";

        Options options = new Options();
        Option option1 = new Option(shortOpt1, longOpt1, false, description1);
        options.addOption(option1);

        OptionGroup optionGroup1 = new OptionGroup(groupDescription1, "group1");
        options.addOptionGroup(optionGroup1);

        assertEquals(2, options.getOptions().size());
        assertEquals(1, options.getOptionGroups().size());
        Option retrievedOption1 = options.getOption(optionKey1);
        assertSame(option1, retrievedOption1);
        OptionGroup retrievedOptionGroup1 = options.getOptionGroup(optionGroupKey1);
        assertTrue(options.hasOption(optionKey1));
        assertTrue(options.hasOption(shortOpt1));
        assertTrue(options.hasOption(longOpt1));
        assertTrue(options.hasOptionGroup(optionGroupKey1));
    }

    @Test
    public void testOptionsWithRequiredOptions() {
        String optionKey1 = "option1";
        String shortOpt1 = "o1";
        String longOpt1 = "option1";
        String description1 = "Option 1 description";

        String optionKey2 = "option2";
        String shortOpt2 = "o2";
        String longOpt2 = "option2";
        String description2 = "Option 2 description";

        Options options = new Options();
        Option option1 = new Option(shortOpt1, longOpt1, true, description1);
        Option option2 = new Option(shortOpt2, longOpt2, false, description2);
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertEquals(0, options.getRequiredOpts().size());
        assertTrue(options.hasOption(optionKey1));
        assertTrue(options.hasOption(optionKey2));
        assertTrue(options.hasOption(shortOpt1));
        assertTrue(options.hasOption(shortOpt2));
        assertTrue(options.hasOption(longOpt1));
        assertTrue(options.hasOption(longOpt2));
        List<String> optionsWithLongOptStartingWith = options.getMatchingOptions("option");
        assertEquals(Arrays.asList(longOpt1, longOpt2), optionsWithLongOptStartingWith);

        List<Object> requiredOpts = options.getRequiredOpts();
        assertEquals(1, requiredOpts.size());
        assertEquals(optionKey1, requiredOpts.get(0));
    }

    @Test
    public void testOptionsWithRequiredOptionGroup() {
        String optionGroupKey1 = "group1";
        String groupDescription1 = "Group 1 description";

        Options options = new Options();
        OptionGroup optionGroup1 = new OptionGroup(groupDescription1, "group1");
        optionGroup1.setRequired(true);
        options.addOptionGroup(optionGroup1);

        assertEquals(0, options.getRequiredOpts().size());
        assertTrue(options.hasOptionGroup(optionGroupKey1));
        assertFalse(options.hasOption(optionGroupKey1));

        List<Object> requiredOpts = options.getRequiredOpts();
        assertEquals(1, requiredOpts.size());
        assertEquals(optionGroupKey1, requiredOpts.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionsWithDuplicateShortOption() {
        String optionKey1 = "option1";
        String shortOpt1 = "o1";
        String longOpt1 = "option1";
        String description1 = "Option 1 description";

        Options options = new Options();
        Option option1 = new Option(shortOpt1, longOpt1, false, description1);
        options.addOption(option1);
        options.addOption(new Option(shortOpt1, null, false, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionsWithDuplicateLongOption() {
        String optionKey1 = "option1";
        String shortOpt1 = "o1";
        String longOpt1 = "option1";
        String description1 = "Option 1 description";

        Options options = new Options();
        Option option1 = new Option(shortOpt1, longOpt1, false, description1);
        options.addOption(option1);
        options.addOption(new Option(null, longOpt1, false, null));
    }

    @Test
    public void testOptionsToString() {
        String optionKey1 = "option1";
        String shortOpt1 = "o1";
        String longOpt1 = "option1";
        String description1 = "Option 1 description";

        Options options = new Options();
        Option option1 = new Option(shortOpt1, longOpt1, false, description1);
        options.addOption(option1);

        String expectedOptionsString = "[ Options: [ short {o1=option1}] [ long {}] ]";
        String actualOptionsString = options.toString();
        assertEquals(expectedOptionsString, actualOptionsString);
    }
}