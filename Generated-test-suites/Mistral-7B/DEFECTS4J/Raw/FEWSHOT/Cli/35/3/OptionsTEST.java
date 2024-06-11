package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", "option A");
        List<Option> helpOptions = options.helpOptions();
        assertEquals(1, helpOptions.size());
        Option option = helpOptions.get(0);
        assertEquals("a", option.getKey());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasArg());

        options.addOption("a", "option A", true);
        helpOptions = options.helpOptions();
        assertEquals(1, helpOptions.size());
        option = helpOptions.get(0);
        assertEquals("a", option.getKey());
        assertEquals("option A", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("l-a", "option A with long name");
        List<Option> helpOptions = options.helpOptions();
        assertEquals(2, helpOptions.size());

        Option shortOption = helpOptions.get(0);
        assertEquals("a", shortOption.getKey());
        assertEquals("option A", shortOption.getDescription());
        assertFalse(shortOption.hasArg());

        Option longOption = helpOptions.get(1);
        assertEquals("l-a", longOption.getKey());
        assertEquals("option A with long name", longOption.getDescription());
        assertFalse(longOption.hasArg());
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1");
        group.addOption(new Option("g", "group option", true, "Short description for group option"));
        options.addOptionGroup(group);
        List<Option> helpOptions = options.helpOptions();
        assertEquals(1, helpOptions.size());
        Option groupOption = helpOptions.get(0);
        assertEquals("g", groupOption.getKey());
        assertEquals("Short description for group option", groupOption.getDescription());
        assertTrue(groupOption.hasArg());

        assertEquals(1, group.getOptions().size());
        Option groupOptionInGroup = group.getOptions().get(0);
        assertEquals("g", groupOptionInGroup.getKey());
        assertEquals("Short description for group option", groupOptionInGroup.getDescription());
        assertTrue(groupOptionInGroup.hasArg());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);
        Option result = options.getOption("a");
        assertEquals(option, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNull() {
        Options options = new Options();
        options.getOption(null);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test(expected = NullPointerException.class)
    public void testHasOptionNull() {
        Options options = new Options();
        options.hasOption(null);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b-a", "option BA");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("a"));
        assertTrue(matchingOptions.contains("b-a"));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        options.addOption(option1);
        options.addOption(option2);
        options.addOptionGroup(new OptionGroup("group1"));
        options.addOptionGroup(new OptionGroup("group2"));
        options.addOption(new Option("c", "option C", true, "Option C is required"));
        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals("c", requiredOptions.get(0).getKey());
    }
}