package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        group1.setRequired(true);
        group1.setDescription("Group 1");

        Options options = new Options();
        options.addOptionGroup(group1);

        assertEquals("Group 1", options.getOptionGroups().iterator().next().getDescription());
        assertTrue(options.getOptionGroups().contains(group1));
        assertTrue(group1.isRequired());

        Option option1 = new Option("o", "opt1", true, "Option 1");
        group1.addOption(option1);

        assertEquals(option1, options.getOption("o"));
        assertEquals(option1, options.getOption("opt1"));

        OptionGroup group2 = new OptionGroup();
        group2.setDescription("Group 2");

        options.addOptionGroup(group2);

        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option option1 = new Option("o", "opt1", false, "Option 1");
        options.addOption(option1);

        assertEquals(option1, options.getOption("o"));
        assertEquals(option1, options.getOption("opt1"));

        Option option2 = new Option("o", "opt2", true, "Option 2");
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertEquals(option1, options.getOptions().get(0));
        assertEquals(option2, options.getOptions().get(1));

        Option option3 = new Option("l", "long-opt3", false, "Long Option 3");
        options.addOption(option3);

        assertEquals(3, options.getOptions().size());
        assertEquals("long-opt3", options.getOption("l").getLongOpt());

        Option option4 = new Option("s", "short-opt4", false, "Short Option 4");
        options.addOption(option4);

        assertEquals(4, options.getOptions().size());
        assertEquals("short-opt4", options.getOption("s").getKey());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        options.addOption(option1);

        List<Option> requiredOptions = options.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertEquals(option1, requiredOptions.get(0));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        Option optionRetrieved = options.getOption("o");

        assertSame(option1, optionRetrieved);
        assertSame(option1, options.getOption("opt1"));
        assertSame(option2, options.getOption("opt2"));
        assertNull(options.getOption("non-existent-option"));

        Option longOption = new Option("long", "long-option", false, "Long Option");
        options.addOption(longOption);

        Option longOptionRetrieved = options.getOption("long");

        assertSame(longOption, longOptionRetrieved);
        assertSame(longOption, options.getOption("long-option"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("opt1"));
        assertTrue(options.hasOption("opt2"));
        assertTrue(options.hasOption("non-existent-option")); // false in real implementation
        assertFalse(options.hasLongOption("non-existent-long-option")); // false in real implementation
        assertFalse(options.hasShortOption("non-existent-short-option")); // false in real implementation
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("opt", "option1", false, "Option 1");
        Option option2 = new Option("opt-long1", "long-option1", false, "Long Option 1");
        Option option3 = new Option("opt-long2", "long-option2", false, "Long Option 2");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("opt");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("opt-long1"));
        assertTrue(matchingOptions.contains("opt-long2"));

        List<String> emptyMatchingOptions = options.getMatchingOptions("non-existent-option");

        assertTrue(emptyMatchingOptions.isEmpty());
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        String toString = options.toString();

        assertTrue(toString.contains("short ["));
        assertTrue(toString.contains("long ["));
    }
}