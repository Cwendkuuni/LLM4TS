package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group1", "Group 1");
        Options options = new Options();
        options = options.addOptionGroup(group);
        assertEquals(Collections.singletonList(group), options.getOptionGroups());
    }

    @Test
    public void testAddShortOption() {
        Options options = new Options();
        options = options.addOption("o", "option", false, "Option test");
        Option option = options.getOption("o");
        assertEquals("o", option.getKey());
        assertEquals(false, option.isRequired());
        assertEquals("Option test", option.getDescription());
    }

    @Test
    public void testAddShortOptionWithArgument() {
        Options options = new Options();
        options = options.addOption("i", "input", true, "Input file");
        Option option = options.getOption("i");
        assertEquals("i", option.getKey());
        assertEquals(true, option.isRequired());
        assertEquals("Input file", option.getDescription());
    }

    @Test
    public void testAddLongOption() {
        Options options = new Options();
        options = options.addOption("o", "option", false, "Option test");
        options = options.addOption("l-option", "long-option", false, "Long option test");
        Option option = options.getOption("l-option");
        assertEquals("l-option", option.getKey());
        assertEquals(false, option.isRequired());
        assertEquals("Long option test", option.getDescription());
    }

    @Test
    public void testAddLongOptionWithArgument() {
        Options options = new Options();
        options = options.addOption("o", "option", false, "Option test");
        options = options.addOption("l--input", "long-input", true, "Long input file");
        Option option = options.getOption("l--input");
        assertEquals("l--input", option.getKey());
        assertEquals(true, option.isRequired());
        assertEquals("Long input file", option.getDescription());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        options = options.addOption("o", "option", false, "Option test");
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o", option.getKey());
        assertEquals(false, option.isRequired());
        assertEquals("Option test", option.getDescription());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = options.addOption("o", "option1", false, "Option 1 test");
        Option option2 = options.addOption("o", "option2", true, "Option 2 test");
        List<Object> requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(option2, requiredOptions.get(0));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = options.addOption("o", "option", false, "Option test");
        assertEquals(option, options.getOption("o"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullOption() {
        Options options = new Options();
        options.getOption(null);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options = options.addOption("o", "option", false, "Option test");
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("n"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options = options.addOption("o", "option", false, "Option test");
        options = options.addOption("--long-option", "long-option", false, "Long option test");
        assertTrue(options.hasLongOption("--long-option"));
        assertFalse(options.hasLongOption("--longother-option"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options = options.addOption("o", "optionA", false, "Option A test");
        options = options.addOption("o", "optionB", false, "Option B test");
        List<String> optionsList = options.getMatchingOptions("o");
        assertEquals(2, optionsList.size());
        assertEquals("optionA", optionsList.get(0));
        assertEquals("optionB", optionsList.get(1));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options = options.addOption("o", "option", false, "Option test");
        assertEquals("[ Options: [ short {o=OPTION, option=Option test} ] [ long <null> ]]", options.toString());
    }
}