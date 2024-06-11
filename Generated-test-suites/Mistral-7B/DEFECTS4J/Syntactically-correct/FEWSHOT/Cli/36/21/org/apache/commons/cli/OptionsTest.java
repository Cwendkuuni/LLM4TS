package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("Group");
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("Group");
        group.addOption(new Option("g", "group", false, "group option"));
        options.addOptionGroup(group);
        assertTrue(options.hasOption("g"));
    }

    @Test
    public void testAddOptionGroupWithRequiredOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("Group");
        group.setRequired(true);
        options.addOptionGroup(group);
        List<Object> requiredOpts = options.getRequiredOptions();
        assertTrue(requiredOpts.contains(group));
    }

    @Test
    public void testAddShortOption() {
        Options options = new Options();
        options.addOption("a", "optionA");
        assertEquals(Collections.singletonMap("a", new Option("a", "optionA")), options.getOptionsMap());
        assertEquals("optionA", options.getOption("a").getDescription());
    }

    @Test
    public void testAddShortOptionalOptionWithValue() {
        Options options = new Options();
        options.addOption("v", "value", true, "option with value");
        Option option = options.getOption("v");
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddShortOptionalOptionWithoutValue() {
        Options options = new Options();
        options.addOption("v", "optionV");
        Option option = options.getOption("v");
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddLongOption() {
        Options options = new Options();
        options.addOption("longOptionA", "longOptionA");
        assertEquals(Collections.singletonMap("longOptionA", new Option("", "longOptionA")), options.getOptionsMap());
        assertEquals("longOptionA", options.getOption("longOptionA").getDescription());
    }

    @Test
    public void testAddLongOptionalOptionWithValue() {
        Options options = new Options();
        options.addOption("longValue", "longValue", true, "option with long value");
        Option option = options.getOption("longValue");
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddLongOptionalOptionWithoutValue() {
        Options options = new Options();
        options.addOption("longNoValue", "longNoValue");
        Option option = options.getOption("longNoValue");
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionWithShortAndLongNames() {
        Options options = new Options();
        Option option = new Option("s", "shortOption", false, "option with short and long names");
        option.setLongOpt("longOption");
        options.addOption(option);
        assertEquals(new Option[]{option}, options.helpOptions().toArray(new Option[0]));
        assertEquals("option with short and long names", option.getDescription());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        Option optionC = new Option("c", "optionC");
        Option optionD = new Option("d", "optionD");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);
        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(Arrays.asList("optionB", "optionC"), matchingOptions);
        List<String> emptyMatchingOptions = options.getMatchingOptions("zzz");
        assertEquals(Collections.emptyList(), emptyMatchingOptions);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        Option optionC = new Option("c", "optionC");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("c"));
        assertFalse(options.hasOption("z"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option optionA = new Option("a", "longOptionA");
        Option optionB = new Option("b", "longOptionB");
        Option optionC = new Option("c", "longOptionC");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        assertTrue(options.hasLongOption("longOptionA"));
        assertTrue(options.hasLongOption("longOptionB"));
        assertTrue(options.hasLongOption("longOptionC"));
        assertFalse(options.hasLongOption("longOptionX"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option optionA = new Option("a", "shortOptionA");
        Option optionB = new Option("b", "shortOptionB");
        Option optionC = new Option("c", "shortOptionC");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertTrue(options.hasShortOption("c"));
        assertFalse(options.hasShortOption("x"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        Option optionC = new Option("c", "optionC");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        assertEquals(optionA, options.getOption("a"));
        assertEquals(optionB, options.getOption("b"));
        assertEquals(optionC, options.getOption("c"));
        assertNull(options.getOption("x"));
    }

    @Test
    public void testGetOptionsMap() {
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        Option optionC = new Option("c", "optionC");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        Map<String, Option> optionsMap = options.getOptionsMap();
        assertEquals(optionA, optionsMap.get("a"));
        assertEquals(optionB, optionsMap.get("b"));
        assertEquals(optionC, optionsMap.get("c"));
        assertNull(optionsMap.get("x"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        Option optionC = new Option("c", "optionC");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        String expected = "[ Options: [ short {a=optionA} ] [ long {} ]]";
        assertEquals(expected, options.toString());
    }
}