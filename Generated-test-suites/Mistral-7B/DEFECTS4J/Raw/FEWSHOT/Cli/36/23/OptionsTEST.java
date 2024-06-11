package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testCreateEmptyOptions() {
        Options options = new Options();
        assertNull(options.getOptions());
        assertNull(options.getRequiredOptions());
        assertNull(options.getOptionGroups());
    }

    @Test
    public void testAddShortOptionWithoutDescription() {
        Options options = new Options();
        options.addOption('a');
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals('a', option.getKey());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getDescription());
    }

    @Test
    public void testAddShortOptionWithDescription() {
        Options options = new Options();
        options.addOption('a', "anOption");
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals('a', option.getKey());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("anOption", option.getDescription());
    }

    @Test
    public void testAddShortOptionWithArgument() {
        Options options = new Options();
        options.addOption('a', true);
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals('a', option.getKey());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddLongOptionWithoutDescription() {
        Options options = new Options();
        options.addOption("anOption", false);
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertNull(option.getKey());
        assertEquals("anOption", option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getDescription());
    }

    @Test
    public void testAddLongOptionWithDescription() {
        Options options = new Options();
        options.addOption("anOption", "aDescription");
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertNull(option.getKey());
        assertEquals("anOption", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("aDescription", option.getDescription());
    }

    @Test
    public void testAddLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("anOption", true);
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertNull(option.getKey());
        assertEquals("anOption", option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddShortOptionWithLongOption() {
        Options options = new Options();
        options.addOption('a', "anOption");
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals('a', option.getKey());
        assertEquals("anOption", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("anOption", option.getDescription());
    }

    @Test
    public void testAddOptionalGroup() {
        OptionGroup group = new OptionGroup("myGroup");
        Options options = new Options();
        options.addOptionGroup(group);
        assertEquals(1, options.getOptionGroups().size());
        OptionGroup retrievedGroup = options.getOptionGroups().iterator().next();
        assertEquals("myGroup", retrievedGroup.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRequiredGroup() {
        OptionGroup group = new OptionGroup("myGroup");
        group.setRequired(true);
        Options options = new Options();
        options.addOptionGroup(group);
    }

    @Test   
    public void testAddOptionToGroup() {
        OptionGroup group = new OptionGroup("myGroup");
        Options options = new Options();
        options.addOptionGroup(group);
        Option option = new Option("option", true, "g", "o");
        group.addOption(option);
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option retrievedOption = optionsList.get(0);
        assertEquals("option", retrievedOption.getKey());
        assertEquals("o", retrievedOption.getLongOpt());
        assertTrue(retrievedOption.hasArg());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("option1");
        options.addOption("option2");
        options.addOption("option3");
        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("option1"));
        assertTrue(matchingOptions.contains("option2"));
    }

    @Test
    public void testGetMatchingOptionsEmptyList() {
        Options options = new Options();
        List<String> matchingOptions = options.getMatchingOptions("nonexistentOption");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testGetMatchingOptionsSameName() {
        Options options = new Options();
        options.addOption("option1");
        options.addOption("option1");
        List<String> matchingOptions = options.getMatchingOptions("option1");
        assertEquals(2, matchingOptions.size());
    }

    @Test
    public void testGetMatchingOptionsPrefix() {
        Options options = new Options();
        options.addOption("option1");
        options.addOption("option2");
        List<String> matchingOptions = options.getMatchingOptions("optio");
        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains("option1"));
    }

    @Test
    public void testHasOptionShort() {
        Options options = new Options();
        options.addOption('a');
        assertTrue(options.hasOption('a'));
        assertFalse(options.hasOption('b'));
    }

    @Test
    public void testHasOptionLong() {
        Options options = new Options();
        options.addOption("anOption");
        assertTrue(options.hasOption("anOption"));
        assertFalse(options.hasOption("anotherOption"));
    }

    @Test
    public void testHasOptionGroup() {
        OptionGroup group = new OptionGroup("myGroup");
        Options options = new Options();
        options.addOptionGroup(group);
        assertTrue(options.hasOptionGroup(group));
        OptionGroup otherGroup = new OptionGroup("anotherGroup");
        assertFalse(options.hasOptionGroup(otherGroup));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption('a');
        options.addOption("anOption");
        String result = options.toString();
        assertTrue(result.contains("Options: [ short"));
        assertTrue(result.contains("long anOption]"));
    }
}