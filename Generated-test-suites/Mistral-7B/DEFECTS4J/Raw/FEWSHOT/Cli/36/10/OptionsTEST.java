package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Options options = new Options();
        Options result = options.addOptionGroup(group);
        assertTrue(result == options);
        assertSame(group, options.getOptionGroup(group));
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup();
        OptionGroup group2 = new OptionGroup();
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        List<OptionGroup> result = options.getOptionGroups();
        assertTrue(result.size() == 2);
        assertTrue(result.contains(group1));
        assertTrue(result.contains(group2));
    }

    @Test
    public void testAddOption_singleCharacter_noArg() {
        Options options = new Options();
        String optionName = "f";
        String description = "foo description";
        Options result = options.addOption(optionName, description);
        Option option = options.getOption(optionName);
        assertTrue(option != null);
        assertEquals(optionName, option.getKey());
        assertEquals(description, option.getDescription());
        assertEquals(false, option.hasArg());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testAddOption_singleCharacter_hasArg() {
        Options options = new Options();
        String optionName = "f";
        String description = "foo description";
        Options result = options.addOption(optionName, true, description);
        Option option = options.getOption(optionName);
        assertTrue(option != null);
        assertEquals(optionName, option.getKey());
        assertEquals(description, option.getDescription());
        assertEquals(true, option.hasArg());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testAddOption_singleCharacter_isRequired() {
        Options options = new Options();
        String optionName = "f";
        String description = "foo description";
        Options result = options.addOption(optionName, description, true);
        Option option = options.getOption(optionName);
        assertTrue(option != null);
        assertEquals(optionName, option.getKey());
        assertEquals(description, option.getDescription());
        assertEquals(false, option.hasArg());
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testAddOption_multiCharacter_noArg() {
        Options options = new Options();
        String longOptionName = "longOptionName";
        String shortOptionName = "l";
        String description = "foo description";
        Options result = options.addOption(shortOptionName, longOptionName, description);
        Option option = options.getOption(longOptionName);
        assertTrue(option != null);
        assertEquals(longOptionName, option.getKey());
        assertEquals(shortOptionName, option.getOpt());
        assertEquals(description, option.getDescription());
        assertEquals(false, option.hasArg());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testAddOption_multiCharacter_hasArg() {
        Options options = new Options();
        String longOptionName = "longOptionName";
        String shortOptionName = "l";
        String description = "foo description";
        Options result = options.addOption(shortOptionName, longOptionName, true, description);
        Option option = options.getOption(longOptionName);
        assertTrue(option != null);
        assertEquals(longOptionName, option.getKey());
        assertEquals(shortOptionName, option.getOpt());
        assertEquals(description, option.getDescription());
        assertEquals(true, option.hasArg());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testAddOption_multiCharacter_isRequired() {
        Options options = new Options();
        String longOptionName = "longOptionName";
        String shortOptionName = "l";
        String description = "foo description";
        Options result = options.addOption(shortOptionName, longOptionName, description, true);
        Option option = options.getOption(longOptionName);
        assertTrue(option != null);
        assertEquals(longOptionName, option.getKey());
        assertEquals(shortOptionName, option.getOpt());
        assertEquals(description, option.getDescription());
        assertEquals(false, option.hasArg());
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Options result = options.addOption("f", "fooOpt");
        List<Option> optionsList = options.getOptions();
        assertTrue(optionsList.size() == 1);
        assertTrue(optionsList.get(0) != null);
        assertEquals("f", optionsList.get(0).getKey());
        assertEquals("fooOpt", optionsList.get(0).getDescription());
        assertEquals(false, optionsList.get(0).hasArg());
        assertEquals(false, optionsList.get(0).isRequired());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        String optionName = "f";
        Options result = options.addOption(optionName, null, true);
        List<Object> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.size() == 1);
        assertTrue(requiredOptions.get(0) != null);
        assertTrue(requiredOptions.get(0) instanceof String);
        assertEquals(optionName, requiredOptions.get(0));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        String optionName = "f";
        Options result = options.addOption(optionName, null, false, "foo description");
        Option option = options.getOption(optionName);
        assertTrue(option != null);
        assertEquals(optionName, option.getKey());
        assertEquals("foo description", option.getDescription());
        assertEquals(false, option.hasArg());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        String optionName = "foo";
        Option option1 = options.addOption("fooOpt1", null, false, "foo description 1");
        Option option2 = options.addOption("foobar", null, false, "foo description 2");
        List<String> result = options.getMatchingOptions(optionName);
        assertTrue(result.size() == 2);
        assertTrue(result.contains("fooOpt1"));
        assertTrue(result.contains("foobar"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        String optionName = "f";
        Options result = options.addOption(optionName, null, false, "foo description");
        assertTrue(options.hasOption(optionName));
        assertTrue(options.hasLongOption(optionName));
        assertTrue(options.hasShortOption(optionName));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        String optionName = "longOptionName";
        Options result = options.addOption("l", optionName, false, "foo description");
        assertTrue(options.hasLongOption(optionName));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        String optionName = "f";
        Options result = options.addOption("f", null, false, "foo description");
        assertTrue(options.hasShortOption(optionName));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Options options = new Options();
        options.addOptionGroup(group);
        Option option = new Option("foo", "fooOpt", false, "foo description");
        group.addOption(option);
        OptionGroup result = options.getOptionGroup(option);
        assertTrue(result != null);
        assertTrue(result == group);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        String optionName1 = "option1";
        String optionName2 = "option2";
        String description1 = "foo description 1";
        String description2 = "foo description 2";
        Options result = options
                .addOption(optionName1, optionName1, false, description1)
                .addOption(optionName2, null, false, description2);
        String expected = "[ Options: [ short {" + optionName1 + "}=>" + optionName1 + "<{},{" + optionName2 + "}=>" + optionName2 + "<{}} ] [ long {"
                + optionName1 + "}=>" + optionName1 + "<{},null}=>" + optionName2 + "<{}} ] ";
        assertEquals(expected, result.toString());
    }
}