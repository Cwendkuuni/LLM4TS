package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    /**
     * Test adding options with short names.
     */
    @Test
    public void testAddShortOptions() {
        Options options = new Options();

        // add options with short names
        options.addOption('a', "option-a");
        options.addOption('b');
        options.addOption('c', "option-c");

        // check options added
        Map<Character, Option> shortOptions = options.getOptions();

        Option optionA = shortOptions.get('a');
        Option optionB = shortOptions.get('b');
        Option optionC = shortOptions.get('c');

        assertEquals("option-a", optionA.getLongOpt());
        assertEquals("option-a", optionA.getDescription());
        assertNull(optionA.getValue());
        assertFalse(optionA.isRequired());

        assertNull(optionB.getLongOpt());
        assertNull(optionB.getDescription());
        assertNull(optionB.getValue());
        assertFalse(optionB.isRequired());

        assertEquals("option-c", optionC.getLongOpt());
        assertEquals("option-c", optionC.getDescription());
        assertNull(optionC.getValue());
        assertFalse(optionC.isRequired());
    }

    /**
     * Test adding options with short and long names.
     */
    @Test
    public void testAddOptions() {
        Options options = new Options();

        // add options with short and long names
        options.addOption("option-a", 'a');
        options.addOption("option-b", 'b');
        options.addOption("option-c", 'c', "option-c");

        // check options added
        Map<String, Option> longOptions = options.getOptions();

        Option optionA = longOptions.get("option-a");
        Option optionB = longOptions.get("option-b");
        Option optionC = longOptions.get("option-c");

        assertEquals('a', optionA.getKey());
        assertEquals("option-a", optionA.getLongOpt());
        assertEquals("option-a", optionA.getDescription());
        assertNull(optionA.getValue());
        assertFalse(optionA.isRequired());

        assertEquals('b', optionB.getKey());
        assertEquals("option-b", optionB.getLongOpt());
        assertEquals("option-b", optionB.getDescription());
        assertNull(optionB.getValue());
        assertFalse(optionB.isRequired());

        assertEquals("option-c", optionC.getLongOpt());
        assertEquals("option-c", optionC.getKey());
        assertEquals("option-c", optionC.getDescription());
        assertNull(optionC.getValue());
        assertFalse(optionC.isRequired());
    }

    /**
     * Test adding options with and without arguments.
     */
    @Test
    public void testAddOptionsWithArguments() {
        Options options = new Options();

        // add options with arguments
        options.addOption("option-a", 'a', true, "an-argument");
        options.addOption("option-b", 'b', true);
        options.addOption("option-c", 'c');

        // check options added
        Map<String, Option> longOptions = options.getOptions();

        Option optionA = longOptions.get("option-a");
        Option optionB = longOptions.get("option-b");
        Option optionC = longOptions.get("option-c");

        assertEquals('a', optionA.getKey());
        assertEquals("option-a", optionA.getLongOpt());
        assertEquals("an-argument", optionA.getDescription());
        assertNull(optionA.getValue());
        assertTrue(optionA.hasArg());

        assertEquals('b', optionB.getKey());
        assertEquals("option-b", optionB.getLongOpt());
        assertNull(optionB.getDescription());
        assertNull(optionB.getValue());
        assertTrue(optionB.hasArg());

        assertEquals('c', optionC.getKey());
        assertEquals("option-c", optionC.getLongOpt());
        assertNull(optionC.getDescription());
        assertNull(optionC.getValue());
        assertFalse(optionC.hasArg());
    }

    /**
     * Test adding options to a group.
     */
    @Test
    public void testAddOptionsToGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("test", "Test options");

        // add options to the group
        Option optionA = new Option("option-a", "a", true, "an-argument");
        Option optionB = new Option("option-b", "b");
        Option optionC = new Option("option-c", "c");

        group.addOption(optionA);
        group.addOption(optionB);
        group.addOption(optionC);

        // add the group to the options
        options.addOptionGroup(group);

        // check options added to the group
        List<Option> groupOptions = group.getOptions();

        Option groupOptionA = groupOptions.get(0);
        Option groupOptionB = groupOptions.get(1);
        Option groupOptionC = groupOptions.get(2);

        assertEquals("option-a", groupOptionA.getLongOpt());
        assertEquals("a", groupOptionA.getKey());
        assertEquals("an-argument", groupOptionA.getDescription());
        assertNull(groupOptionA.getValue());
        assertTrue(groupOptionA.hasArg());

        assertEquals("option-b", groupOptionB.getLongOpt());
        assertEquals("b", groupOptionB.getKey());
        assertNull(groupOptionB.getDescription());
        assertNull(groupOptionB.getValue());
        assertFalse(groupOptionB.hasArg());

        assertEquals("option-c", groupOptionC.getLongOpt());
        assertEquals("c", groupOptionC.getKey());
        assertNull(groupOptionC.getDescription());
        assertNull(groupOptionC.getValue());
        assertFalse(groupOptionC.hasArg());

        // check options added to the global options
        Map<String, Option> longOptions = options.getOptions();

        Option globalOptionA = longOptions.get("option-a");
        Option globalOptionB = longOptions.get("option-b");
        Option globalOptionC = longOptions.get("option-c");

        assertEquals("option-a", globalOptionA.getLongOpt());
        assertEquals("a", globalOptionA.getKey());
        assertEquals("an-argument", globalOptionA.getDescription());
        assertNull(globalOptionA.getValue());
        assertTrue(globalOptionA.hasArg());

        assertEquals("option-b", globalOptionB.getLongOpt());
        assertEquals("b", globalOptionB.getKey());
        assertNull(globalOptionB.getDescription());
        assertNull(globalOptionB.getValue());
        assertTrue(globalOptionB.hasArg());

        assertEquals("option-c", globalOptionC.getLongOpt());
        assertEquals("c", globalOptionC.getKey());
        assertNull(globalOptionC.getDescription());
        assertNull(globalOptionC.getValue());
        assertFalse(globalOptionC.hasArg());
    }

    /**
     * Test getting options by name.
     */
    @Test
    public void testGetOptionByName() {
        Options options = new Options();

        // add options
        options.addOption('a', "option-a");
        options.addOption("option-b", 'b');

        // get options by name
        Option optionA = options.getOption("option-a");
        Option optionB = options.getOption("option-b");

        assertEquals("option-a", optionA.getLongOpt());
        assertEquals('a', optionA.getKey());
        assertNull(optionA.getValue());
        assertFalse(optionA.isRequired());

        assertEquals("option-b", optionB.getLongOpt());
        assertEquals('b', optionB.getKey());
        assertNull(optionB.getValue());
        assertFalse(optionB.isRequired());
    }

    /**
     * Test getting options by name with a partial match.
     */
    @Test
    public void testGetOptionsByNameWithPartialMatch() {
        Options options = new Options();

        // add options
        options.addOption("option-a");
        options.addOption("option-long-a");
        options.addOption("option-b");

        // get options by name with a partial match
        List<Option> optionsWithPrefix = options.getMatchingOptions("option");

        assertEquals(2, optionsWithPrefix.size());
        assertEquals("option-a", optionsWithPrefix.get(0).getLongOpt());
        assertEquals("option-long-a", optionsWithPrefix.get(1).getLongOpt());
    }

    /**
     * Test getting the help options.
     */
    @Test
    public void testGetHelpOptions() {
        Options options = new Options();

        // add options
        options.addOption('a', "option-a");
        options.addOption("option-b", 'b');

        // get help options
        List<Option> helpOptions = options.helpOptions();

        assertEquals(2, helpOptions.size());
        assertEquals("option-a", helpOptions.get(0).getLongOpt());
        assertEquals('a', helpOptions.get(0).getKey());
        assertNull(helpOptions.get(0).getValue());
        assertFalse(helpOptions.get(0).isRequired());

        assertEquals("option-b", helpOptions.get(1).getLongOpt());
        assertEquals('b', helpOptions.get(1).getKey());
        assertNull(helpOptions.get(1).getValue());
        assertFalse(helpOptions.get(1).isRequired());
    }

    /**
     * Test getting the required options.
     */
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();

        // add required options
        options.addOption("required-option-a");
        options.addOption("required-option-b");

        // get required options
        List<Option> requiredOptions = options.getRequiredOptions();

        assertEquals(2, requiredOptions.size());
        assertNotNull(requiredOptions.get(0));
        assertNotNull(requiredOptions.get(1));
    }

    /**
     * Test getting an option group.
     */
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("test", "Test options");

        // add options to the group
        Option optionA = new Option("option-a", "a", true, "an-argument");
        Option optionB = new Option("option-b", "b");

        group.addOption(optionA);
        group.addOption(optionB);

        // add the group to the options
        options.addOptionGroup(group);

        // get the option group
        OptionGroup optionGroup = options.getOptionGroup(optionA);

        assertEquals("test", optionGroup.getLongOpt());
        assertEquals("Test options", optionGroup.getDescription());
        assertEquals(2, optionGroup.getOptions().size());
        assertEquals("option-a", optionGroup.getOptions().get(0).getLongOpt());
        assertEquals("a", optionGroup.getOptions().get(0).getKey());
        assertEquals("an-argument", optionGroup.getOptions().get(0).getDescription());
        assertNull(optionGroup.getOptions().get(0).getValue());
        assertTrue(optionGroup.getOptions().get(0).hasArg());

        assertEquals("option-b", optionGroup.getOptions().get(1).getLongOpt());
        assertEquals("b", optionGroup.getOptions().get(1).getKey());
        assertNull(optionGroup.getOptions().get(1).getDescription());
        assertNull(optionGroup.getOptions().get(1).getValue());
        assertFalse(optionGroup.getOptions().get(1).hasArg());
    }
}