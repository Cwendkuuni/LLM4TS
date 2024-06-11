package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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
    public void testCreateEmptyOptions() {
        Options options = new Options();
        assertEquals(Collections.emptyMap(), options.getShortOpts());
        assertEquals(Collections.emptyMap(), options.getLongOpts());
        assertEquals(Collections.emptyList(), options.getRequiredOpts());
        assertEquals(Collections.emptyMap(), options.getOptionGroups());
    }

    @Test
    public void testAddOptionWithShortOption() {
        Options options = new Options();
        String optionName = "o";
        String description = "Output file";

        Option option = new Option(optionName, description, false, null);
        options.addOption(option);

        assertEquals(optionName, options.getShortOpts().get(optionName));
        assertEquals(option, options.getOptions().get(0));
    }

    @Test
    public void testAddOptionWithShortAndLongOption() {
        Options options = new Options();
        String shortOptionName = "o";
        String longOptionName = "output";
        String description = "Output file";

        Option option = new Option(optionName, longOptionName, false, description);
        options.addOption(option);

        assertEquals(optionName, options.getShortOpts().get(optionName));
        assertEquals(longOptionName, option.getLongOpt());
        assertEquals(option, options.getOptions().get(0));
    }

    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();
        String optionName = "i";
        String description = "Input file";

        Option option = new Option(optionName, null, true, description);
        options.addOption(option);

        assertEquals(optionName, options.getShortOpts().get(optionName));
        assertTrue(option.hasArg());
        assertEquals(option, options.getOptions().get(0));
    }

    @Test
    public void testAddOptionWithShortAndLongOptionAndArgument() {
        Options options = new Options();
        String shortOptionName = "i";
        String longOptionName = "input";
        String description = "Input file";

        Option option = new Option(shortOptionName, longOptionName, true, description);
        options.addOption(option);

        assertEquals(optionName, options.getShortOpts().get(optionName));
        assertEquals(longOptionName, option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(option, options.getOptions().get(0));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        String groupName = "group";

        OptionGroup group = new OptionGroup(groupName, "Group of options");
        options.addOptionGroup(group);

        assertEquals(groupName, options.getOptionGroups().get(groupName));
        assertSame(group, options.getOptions().get(0));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        String optionName1 = "o1";
        String optionName2 = "o2";

        options.addOption(new Option(optionName1, null, true, "Required option 1"));
        options.addOption(new Option(optionName2, null, true, "Required option 2"));

        List<String> requiredOptions = options.getRequiredOptions();
        assertEquals(2, requiredOptions.size());
        assertTrue(requiredOptions.contains(optionName1));
        assertTrue(requiredOptions.contains(optionName2));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        String optionName1 = "o1";
        String optionName2 = "o2";
        String partialMatch = "o";

        options.addOption(new Option(optionName1, null, false, "Option 1"));
        options.addOption(new Option(optionName2, null, false, "Option 2"));

        List<String> matchingOptions = options.getMatchingOptions(partialMatch);
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains(optionName1));
        assertTrue(matchingOptions.contains(optionName2));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        String optionName1 = "o1";
        String optionName2 = "o2";

        options.addOption(new Option(optionName1, null, false, "Option 1"));
        options.addOption(new Option(optionName2, null, false, "Option 2"));

        assertTrue(options.hasOption(optionName1));
        assertTrue(options.hasOption(optionName2));
        assertFalse(options.hasOption("non-existent-option"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        String optionName1 = "l1";
        String longOptionName1 = "long-option-1";
        String optionName2 = "o2";
        String longOptionName2 = "long-option-2";

        options.addOption(new Option(optionName1, longOptionName1, false, "Option 1"));
        options.addOption(new Option(optionName2, null, false, "Option 2"));

        assertTrue(options.hasLongOption(longOptionName1));
        assertFalse(options.hasLongOption("non-existent-long-option"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        String optionName1 = "o1";
        String shortOptionName1 = "s1";
        String optionName2 = "o2";
        String shortOptionName2 = "s2";

        options.addOption(new Option(optionName1, null, false, "Option 1", new String[] { shortOptionName1 }));
        options.addOption(new Option(optionName2, null, false, "Option 2", new String[] { shortOptionName2 }));

        assertTrue(options.hasShortOption(shortOptionName1));
        assertTrue(options.hasShortOption(shortOptionName2));
        assertFalse(options.hasShortOption("non-existent-short-option"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        String groupName = "group";
        String optionName = "o";

        OptionGroup group = new OptionGroup(groupName, "Group of options");
        Option option = new Option(optionName, null, false, "Option in group");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
        assertNull(options.getOptionGroup(new Option("non-existent-option")));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        String optionName1 = "o1";
        String longOptionName1 = "long-option-1";
        String optionName2 = "o2";
        String longOptionName2 = "long-option-2";

        options.addOption(new Option(optionName1, longOptionName1, false, "Option 1"));
        options.addOption(new Option(optionName2, null, false, "Option 2"));

        String expectedOutput = "[ Options: [ short {o1=Option(o1, long-option-1, false, Option(o1, long-option-1, false, Option description:Option description:Option), false)} ] [ long {} ]]";

        assertEquals(expectedOutput, options.toString());
    }
}