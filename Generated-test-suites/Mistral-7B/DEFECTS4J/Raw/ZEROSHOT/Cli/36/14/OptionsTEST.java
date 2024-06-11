package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddShortOption() {
        options.addOption('a', "arg", false, "Test short option with no argument");
        Option option = options.getOption('a');
        assertNotNull(option);
        assertEquals("Test short option with no argument", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testAddShortOptionWithArgument() {
        options.addOption('a', "arg", true, "Test short option with argument");
        Option option = options.getOption('a');
        assertNotNull(option);
        assertEquals("Test short option with argument", option.getDescription());
        assertTrue(option.hasArg());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testAddLongOption() {
        options.addOption("longArg", "Test long option with no argument", false, "Test long option with no argument");
        Option option = options.getOption("longArg");
        assertNotNull(option);
        assertEquals("Test long option with no argument", option.getDescription());
        assertFalse(option.hasArg());
        assertEquals("longArg", option.getLongOpt());
        assertNull(option.getOpt());
    }

    @Test
    public void testAddLongOptionWithArgument() {
        options.addOption("longArg", "Test long option with argument", true, "Test long option with argument");
        Option option = options.getOption("longArg");
        assertNotNull(option);
        assertEquals("Test long option with argument", option.getDescription());
        assertTrue(option.hasArg());
        assertEquals("longArg", option.getLongOpt());
        assertNull(option.getOpt());
    }

    @Test
    public void testAddShortOptionWithLongOption() {
        options.addOption('a', "arg", false, "Test short option with no argument");
        options.addOption("arg", "Test long option with no argument", false, "Test long option with no argument");
        Option option = options.getOption("arg");
        assertNotNull(option);
        assertEquals("Test long option with no argument", option.getDescription());
        assertFalse(option.hasArg());
        assertEquals("arg", option.getLongOpt());
        assertTrue(option.isAlt());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group", "Test OptionGroup");
        group.setRequired(true);
        group.addOption(new Option("arg1", "Test arg1", true, "Test arg1 option"));
        group.addOption(new Option("arg2", "Test arg2", true, "Test arg2 option"));
        options.addOptionGroup(group);
        List<Option> optionsList = options.helpOptions();
        OptionGroup groupOption = null;
        for (Option option : optionsList) {
            if (option.isAlt() && option.getLongOpt().equals("group")) {
                groupOption = (OptionGroup) option;
                break;
            }
        }
        assertNotNull(groupOption);
        assertEquals(2, groupOption.getOptions().size());
        Option option1 = groupOption.getOptions().get(0);
        Option option2 = groupOption.getOptions().get(1);
        assertEquals("Test arg1 option", option1.getDescription());
        assertEquals("Test arg1", option1.getKey());
        assertTrue(option1.isRequired());
        assertEquals("Test arg2 option", option2.getDescription());
        assertEquals("Test arg2", option2.getKey());
        assertTrue(option2.isRequired());
    }

    @Test
    public void testGetRequiredOptions() {
        OptionGroup group = new OptionGroup("group", "Test OptionGroup");
        group.setRequired(true);
        group.addOption(new Option("arg1", "Test arg1", true, "Test arg1 option"));
        group.addOption(new Option("arg2", "Test arg2", true, "Test arg2 option"));
        options.addOptionGroup(group);
        List<Option> optionsList = options.getRequiredOptions();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        assertEquals("Test arg1 option", option1.getDescription());
        assertEquals("Test arg1", option1.getKey());
        assertTrue(option1.isRequired());
        assertEquals("Test arg2 option", option2.getDescription());
        assertEquals("Test arg2", option2.getKey());
        assertTrue(option2.isRequired());
    }

    @Test
    public void testGetString() {
        options.addOption('a', "arg", false, "Test short option with no argument");
        options.addOption("arg", "Test long option with no argument", false, "Test long option with no argument");
        OptionGroup group = new OptionGroup("group", "Test OptionGroup");
        group.addOption(new Option("arg1", "Test arg1", true, "Test arg1 option"));
        group.addOption(new Option("arg2", "Test arg2", true, "Test arg2 option"));
        options.addOptionGroup(group);
        String str = options.toString();
        assertTrue(str.contains("Test short option with no argument"));
        assertTrue(str.contains("Test long option with no argument"));
        assertTrue(str.contains("Test arg1 option"));
        assertTrue(str.contains("Test arg2 option"));
        assertTrue(str.contains("Test OptionGroup"));
    }
}