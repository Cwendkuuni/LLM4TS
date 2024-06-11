package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("test", "Test options");
        Options options = new Options();
        options.addOptionGroup(group);
        assertEquals(1, options.getOptionGroups().size());
        assertSame(group, options.getOptionGroups().iterator().next());
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        OptionGroup group = new OptionGroup("test", "Test options");
        Option option = new Option("t", "test", true, "A test option");
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);
        assertEquals(1, options.getRequiredOptions().size());
        assertEquals(option, options.getRequiredOptions().get(0));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("g1", "Group 1");
        OptionGroup group2 = new OptionGroup("g2", "Group 2");
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        group1.addOption(option1);
        group2.addOption(option2);
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        assertEquals(2, options.getOptionGroups().size());
        List<OptionGroup> groups = new ArrayList<>(options.getOptionGroups());
        assertEquals(group1, groups.get(0));
        assertEquals(group2, groups.get(1));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getOptions().size());
        List<Option> optionsList = new ArrayList<>(options.getOptions());
        assertEquals(option1, optionsList.get(0));
        assertEquals(option2, optionsList.get(1));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("x"));
        assertFalse(options.hasOption(""));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("l", "longOption", false, "Long option");
        Options options = new Options();
        options.addOption(option);
        assertTrue(options.hasLongOption("longOption"));
        assertFalse(options.hasLongOption("shortOption"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("s", "shortOption", false, "Short option");
        Options options = new Options();
        options.addOption(option);
        assertTrue(options.hasShortOption("s"));
        assertFalse(options.hasShortOption("l"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        assertEquals(option, options.getOption("o"));
        assertNull(options.getOption("x"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        Option option3 = new Option("o3", "option3", false, "Option 3");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> matchingOptions = options.getMatchingOptions("o");
        assertEquals(2, matchingOptions.size());
        assertEquals("o1", matchingOptions.get(0));
        assertEquals("o2", matchingOptions.get(1));
        List<String> emptyMatchingOptions = options.getMatchingOptions("");
        assertTrue(emptyMatchingOptions.isEmpty());
        List<String> noMatchingOptions = options.getMatchingOptions("x");
        assertTrue(noMatchingOptions.isEmpty());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        String optionsString = "Options: [ short {o1=Option 1, o2=Option 2} ] [ long {} ]";
        assertEquals(optionsString, options.toString());
    }
}