package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testNewOptionsInstance() {
        Options options = new Options();
        assertEquals(Collections.emptyMap(), options.getShortOpts());
        assertEquals(Collections.emptyMap(), options.getLongOpts());
        assertEquals(Collections.emptyList(), options.getRequiredOpts());
        assertEquals(Collections.emptyMap(), options.getOptionGroups());
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("Group1", "Group description");
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(group));
        assertTrue(options.getOptionGroups().contains(group));
        assertEquals(1, options.getOptionGroups().size());
    }

    @Test
    public void testAddOptionGroupWithDuplicateName() {
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("Group1", "Group1 description");
        options.addOptionGroup(group1);

        assertFalse(options.addOptionGroup(group1));
        assertEquals(1, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option opt = new Option("opt", "Option description");
        options.addOption(opt);

        assertEquals(opt, options.getOption(opt.getKey()));
        assertTrue(options.getOptions().contains(opt));
        assertEquals(1, options.getOptions().size());
    }

    @Test
    public void testAddOptionWithDuplicateShortName() {
        Options options = new Options();

        Option opt1 = new Option("opt", "Option description");
        options.addOption(opt1);

        assertFalse(options.addOption("opt", "New option description"));
        assertEquals(1, options.getOptions().size());
    }

    @Test
    public void testAddOptionWithDuplicateLongName() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt1", "Option2 description");
        options.addOption(opt1);

        assertFalse(options.addOption(opt2));
        assertEquals(1, options.getOptions().size());
    }

    @Test
    public void testAddOptionWithDuplicateOption() {
        Options options = new Options();

        Option opt = new Option("opt", "Option description");
        options.addOption(opt);

        assertFalse(options.addOption(opt));
        assertEquals(1, options.getOptions().size());
    }

    @Test
    public void testAddRequiredOption() {
        Options options = new Options();

        Option opt = new Option("opt", "Option description");
        options.addOption(opt);

        opt.setRequired(true);
        assertTrue(options.requireOption(opt.getKey()));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddRequiredOptionAlreadyExists() {
        Options options = new Options();

        Option opt = new Option("opt", "Option description");
        options.addOption(opt);

        opt.setRequired(true);
        options.addOption(opt);
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();

        Option opt = new Option("opt", "Option description", true, "arg");
        options.addOption(opt);

        assertEquals("arg", options.getOption(opt.getKey()).getArgName());
        assertTrue(options.getOption(opt.getKey()).hasArg());
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();

        Option opt = new Option("opt", "Option description", false, "arg");
        opt.setLongOpt("long-opt");
        options.addOption(opt);

        assertEquals("long-opt", options.getOption(opt.getKey()).getLongOpt());
        assertFalse(options.getOption(opt.getKey()).hasArg());
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        Options options = new Options();

        Option opt = new Option("o", "Option description", false, "arg");
        opt.setLongOpt("long-opt");
        options.addOption(opt);

        assertEquals("o", options.getOption(opt.getKey()).getKey());
        assertEquals("long-opt", options.getOption(opt.getKey()).getLongOpt());
        assertFalse(options.getOption(opt.getKey()).hasArg());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt2", "Option2 description");
        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> optionsList = options.getOptions();
        assertEquals(2, optionsList.size());
        assertEquals(opt1, optionsList.get(0));
        assertEquals(opt2, optionsList.get(1));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt2", "Option2 description");
        opt1.setRequired(true);
        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> requiredOptionsList = options.getRequiredOptions();
        assertEquals(1, requiredOptionsList.size());
        assertEquals(opt1, requiredOptionsList.get(0));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt2", "Option2 description");
        Option opt3 = new Option("opt3", "Option3 description");
        Option opt4 = new Option("opt4", "Option4 description");
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        options.addOption(opt4);

        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains("opt1"));
        assertTrue(matchingOptions.contains("opt2"));
        assertTrue(matchingOptions.contains("opt3"));

        List<String> matchingOptionsWithLongName = options.getMatchingOptions("long-opt3");
        assertEquals(1, matchingOptionsWithLongName.size());
        assertTrue(matchingOptionsWithLongName.contains("opt3"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt2", "Option2 description");
        options.addOption(opt1);
        options.addOption(opt2);

        assertTrue(options.hasOption("opt1"));
        assertTrue(options.hasOption("opt2"));
        assertTrue(options.hasOption("opt3")); // false by default
        assertFalse(options.hasLongOption("opt1"));
        assertFalse(options.hasLongOption("opt2"));
        assertFalse(options.hasLongOption("opt3"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt2", "Option2 description");
        options.addOption(opt1);
        options.addOption(opt2);

        Option optRetrieved1 = options.getOption("opt1");
        Option optRetrieved2 = options.getOption("opt2");

        assertEquals(opt1, optRetrieved1);
        assertEquals(opt2, optRetrieved2);
    }

    @Test
    public void testGetOptionWithDuplicateShortName() {
        Options options = new Options();

        Option opt1 = new Option("opt", "Option1 description");
        Option opt2 = new Option("opt", "Option2 description");
        options.addOption(opt1);
        options.addOption(opt2);

        Option optRetrieved = options.getOption("opt");

        assertEquals(opt1, optRetrieved);
    }

    @Test
    public void testGetOptionWithDuplicateLongName() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("long-opt1", "Option1 description");
        options.addOption(opt1);
        options.addOption(opt2);

        Option optRetrieved = options.getOption("long-opt1");

        assertEquals(opt1, optRetrieved);
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("Group1", "Group1 description");
        OptionGroup group2 = new OptionGroup("Group2", "Group2 description");
        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt2", "Option2 description");
        group1.addOption(opt1);
        group1.addOption(opt2);
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        OptionGroup groupRetrieved1 = options.getOptionGroup(group1);
        OptionGroup groupRetrieved2 = options.getOptionGroup(group2);

        assertEquals(group1, groupRetrieved1);
        assertEquals(group2, groupRetrieved2);
    }

    @Test
    public void testGetOptionGroupWithNullGroup() {
        Options options = new Options();

        assertNull(options.getOptionGroup(null));
    }

    @Test
    public void testToString() {
        Options options = new Options();

        Option opt1 = new Option("opt1", "Option1 description");
        Option opt2 = new Option("opt2", "Option2 description");
        options.addOption(opt1);
        options.addOption(opt2);

        String optionsString = options.toString();

        assertTrue(optionsString.contains("Options: [ short"));
        assertTrue(optionsString.contains("[ long"));
    }
}