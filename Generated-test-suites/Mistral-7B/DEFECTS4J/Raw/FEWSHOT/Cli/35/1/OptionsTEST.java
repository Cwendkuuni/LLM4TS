package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public void testAddSingleShortOption() {
        Options options = new Options();
        options.addOption('a', "option-a");

        Map<String, Option> shortOpts = options.getShortOpts();
        assertEquals(1, shortOpts.size());
        Option optionA = shortOpts.get("a");
        assertEquals("option-a", optionA.getLongOpt());
        assertFalse(optionA.hasArg());

        List<Object> requiredOpts = options.getRequiredOpts();
        assertEquals(0, requiredOpts.size());
    }

    @Test
    public void testAddSingleLongOption() {
        Options options = new Options();
        options.addOption("long-option-a", true, "description-long-option-a");

        Map<String, Option> longOpts = options.getLongOpts();
        assertEquals(1, longOpts.size());
        Option optionALong = longOpts.get("long-option-a");
        assertEquals("a", optionALong.getKey());
        assertTrue(optionALong.hasArg());
        assertEquals("description-long-option-a", optionALong.getDescription());

        Map<String, Option> shortOpts = options.getShortOpts();
        assertEquals(0, shortOpts.size());

        List<Object> requiredOpts = options.getRequiredOpts();
        assertEquals(0, requiredOpts.size());
    }

    @Test
    public void testAddMultipleOptions() {
        Options options = new Options();
        options.addOption('a', "option-a");
        options.addOption('b', "option-b");
        options.addOption("long-option-c", false, "description-long-option-c");

        Map<String, Option> shortOpts = options.getShortOpts();
        assertEquals(2, shortOpts.size());
        Option optionA = shortOpts.get("a");
        assertEquals("option-a", optionA.getLongOpt());
        assertFalse(optionA.hasArg());

        Option optionB = shortOpts.get("b");
        assertEquals("option-b", optionB.getLongOpt());
        assertFalse(optionB.hasArg());

        Map<String, Option> longOpts = options.getLongOpts();
        assertEquals(1, longOpts.size());
        Option optionCLong = longOpts.get("long-option-c");
        assertEquals("c", optionCLong.getKey());
        assertFalse(optionCLong.hasArg());
        assertEquals("description-long-option-c", optionCLong.getDescription());

        List<Object> requiredOpts = options.getRequiredOpts();
        assertEquals(0, requiredOpts.size());
    }

    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();
        options.addOption('a', "option-a", true, "description-option-a-with-argument");

        Map<String, Option> shortOpts = options.getShortOpts();
        assertEquals(1, shortOpts.size());
        Option optionA = shortOpts.get("a");
        assertEquals("option-a", optionA.getLongOpt());
        assertTrue(optionA.hasArg());

        Map<String, Option> longOpts = options.getLongOpts();
        assertEquals(0, longOpts.size());

        List<Object> requiredOpts = options.getRequiredOpts();
        assertEquals(0, requiredOpts.size());
    }

    @Test
    public void testAddOptionGroup() {
        Options parentOptions = new Options();
        OptionGroup optionGroup = new OptionGroup("option-group-name");
        parentOptions.addOptionGroup(optionGroup);

        Map<String, Option> shortOpts = parentOptions.getShortOpts();
        assertEquals(0, shortOpts.size());

        Map<String, Option> longOpts = parentOptions.getLongOpts();
        assertEquals(0, longOpts.size());

        List<Object> requiredOpts = parentOptions.getRequiredOpts();
        assertEquals(0, requiredOpts.size());

        List<Option> optionsInGroup = optionGroup.getOptions();
        assertEquals(1, optionsInGroup.size());
        Option optionInGroup = optionsInGroup.get(0);
        assertEquals("option-group-name", optionInGroup.getLongOpt());
        assertTrue(optionInGroup.isRequired());

        Map<String, Option> shortOptsInGroup = optionGroup.getShortOpts();
        assertEquals(0, shortOptsInGroup.size());

        Map<String, Option> longOptsInGroup = optionGroup.getLongOpts();
        assertEquals(0, longOptsInGroup.size());

        List<Option> requiredOptsInGroup = optionGroup.getRequiredOpts();
        assertEquals(1, requiredOptsInGroup.size());
    }

    @Test
    public void testGetOptionsWithAddedOptions() {
        Options options = new Options();
        options.addOption('a', "option-a");
        options.addOption('b', "option-b");
        options.addOption("long-option-c", false, "description-long-option-c");

        List<Option> optionsList = options.helpOptions();
        assertEquals(3, optionsList.size());

        Option optionA = optionsList.get(0);
        assertEquals("option-a", optionA.getKey());
        assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(1);
        assertEquals("option-b", optionB.getKey());
        assertFalse(optionB.hasArg());

        Option optionC = optionsList.get(2);
        assertEquals("long-option-c", optionC.getLongOpt());
        assertFalse(optionC.hasArg());
    }

    @Test
    public void testGetOptionsWithAddedOptionGroup() {
        Options parentOptions = new Options();
        OptionGroup optionGroup = new OptionGroup("option-group-name");
        parentOptions.addOptionGroup(optionGroup);
        Option optionInGroup = new Option("option-in-group", true, "description-option-in-group");
        optionGroup.addOption(optionInGroup);
        parentOptions.addOption('p', "option-p");

        List<Option> optionsList = parentOptions.helpOptions();
        assertEquals(3, optionsList.size());

        Option optionP = optionsList.get(0);
        assertEquals("option-p", optionP.getKey());
        assertFalse(optionP.hasArg());

        Option optionInGroup = optionsList.get(1);
        assertEquals("option-group-name", optionInGroup.getLongOpt());
        assertTrue(optionInGroup.isRequired());

        Option optionOptionInGroup = optionsList.get(2);
        assertEquals("option-in-group", optionOptionInGroup.getKey());
        assertTrue(optionOptionInGroup.hasArg());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption("option-a", true, "description-option-a");
        options.addOptionGroup(new OptionGroup("option-group-name"));
        options.addOption("option-b", true, "description-option-b");

        List<Object> requiredOpts = options.getRequiredOpts();
        assertEquals(0, requiredOpts.size());

        options.addOption("option-a", true, "description-option-a", true);
        requiredOpts = options.getRequiredOpts();
        assertEquals(1, requiredOpts.size());
        Option optionA = (Option) requiredOpts.get(0);
        assertEquals("option-a", optionA.getKey());
        assertTrue(optionA.isRequired());
    }

    @Test
    public void testGetOptionWithShortName() {
        Options options = new Options();
        options.addOption('a', "option-a");
        Option optionA = options.getOption("a");
        assertEquals("option-a", optionA.getLongOpt());
        assertFalse(optionA.hasArg());
    }

    @Test
    public void testGetOptionWithLongName() {
        Options options = new Options();
        options.addOption("option-a", true, "description-option-a");
        Option optionALong = options.getOption("option-a");
        assertEquals("option-a", optionALong.getKey());
        assertTrue(optionALong.hasArg());
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionWithUnknownOption() {
        Options options = new Options();
        options.addOption('a', "option-a");
        options.getOption("unknown-option");
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("option-1");
        options.addOption("option-2");
        options.addOption("option-3");

        List<String> matchingOptions = options.getMatchingOptions("option-");
        assertEquals(2, matchingOptions.size());
        assertEquals("option-1", matchingOptions.get(0));
        assertEquals("option-2", matchingOptions.get(1));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption('a', "option-a");
        options.addOption("option-b");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option-a"));
        assertTrue(options.hasOption("a,"));
        assertTrue(options.hasOption("a-"));
        assertTrue(options.hasOption("a "));
        assertTrue(options.hasOption("a "));

        assertFalse(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("option-a", true, null);
        options.addOption("option-b", false, null);

        assertTrue(options.hasLongOption("option-a"));
        assertTrue(options.hasLongOption("option-b"));
        assertFalse(options.hasLongOption("option-c"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption('a', "option-a");
        options.addOption('b', "option-b");

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("a-"));
        assertTrue(options.hasShortOption("a "));

        assertTrue(options.hasShortOption("b"));
        assertTrue(options.hasShortOption("b-"));
        assertTrue(options.hasShortOption("b "));

        assertFalse(options.hasShortOption("c"));
        assertFalse(options.hasShortOption("d"));
    }

    @Test
    public void testGetOptionGroup() {
        Options parentOptions = new Options();
        OptionGroup optionGroup = new OptionGroup("option-group-name");
        parentOptions.addOptionGroup(optionGroup);
        Option optionInGroup = new Option("option-in-group", true, "description-option-in-group");
        optionGroup.addOption(optionInGroup);
        Option optionParent = new Option("option-parent", true, "description-option-parent");
        parentOptions.addOption(optionParent);

        OptionGroup optionGroupFromParent = parentOptions.getOptionGroup(optionInGroup);
        assertSame(optionGroup, optionGroupFromParent);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption('a', "option-a");
        options.addOption("option-b");

        String toString = options.toString();
        assertTrue(toString.contains("[ Options: [ short"));
        assertTrue(toString.contains("[ long"));
    }
}