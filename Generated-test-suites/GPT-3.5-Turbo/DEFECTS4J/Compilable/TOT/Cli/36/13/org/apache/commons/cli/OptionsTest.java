package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        // Test adding an option without long name
        Option option1 = new Option("a", false, "Option without long name");
        options.addOption(option1);
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
        assertNull(options.getOption("b"));
        
        // Test adding an option with long name
        Option option2 = new Option("c", "option", false, "Option with long name");
        options.addOption(option2);
        assertTrue(options.hasOption("c"));
        assertTrue(options.hasOption("option"));
        assertEquals(option2, options.getOption("option"));
    }

    @Test
    public void testAddOptionGroup() {
        // Test adding an OptionGroup
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", false, "Option in group");
        Option option2 = new Option("b", "option", false, "Option in group with long name");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("option"));
        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOptionWithNameAndDesc() {
        // Test adding an option with only short name and description
        options.addOption("a", "Option without argument");
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
        assertNull(options.getOption("b"));
        
        // Test adding an option with short name, argument and description
        options.addOption("c", true, "Option with argument");
        assertTrue(options.hasOption("c"));
        assertTrue(options.hasOption("option"));
        assertEquals("Option with argument", options.getOption("option").getDescription());
    }

    @Test
    public void testAddOptionWithAllParams() {
        // Test adding an option with short name, long name, argument and description
        options.addOption("a", "option", true, "Option with short name, long name, and argument");
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
        assertEquals("Option with short name, long name, and argument", options.getOption("option").getDescription());
    }

    @Test
    public void testGetOptions() {
        // Test getting options from empty Options object
        assertTrue(options.getOptions().isEmpty());
        
        // Test getting options from Options object with added options
        Option option1 = new Option("a", false, "Option without argument");
        Option option2 = new Option("b", true, "Option with argument");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        // Test getting required options from empty Options object
        assertTrue(options.getRequiredOptions().isEmpty());

        // Test getting required options from Options object with added options
        Option option1 = new Option("a", false, "Option without argument");
        option1.setRequired(true);
        Option option2 = new Option("b", true, "Option with argument");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1.getKey()));
        assertTrue(options.getRequiredOptions().contains(option2.getKey()));
    }

    @Test
    public void testGetOption() {
        // Test getting Option with short name
        Option option1 = new Option("a", false, "Option without argument");
        options.addOption(option1);
        assertEquals(option1, options.getOption("a"));
        
        // Test getting Option with long name
        Option option2 = new Option("b", "option", false, "Option with long name");
        options.addOption(option2);
        assertEquals(option2, options.getOption("option"));
        
        // Test getting unknown Option
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting options matching a partial name
        Option option1 = new Option("a", false, "Option without argument");
        Option option2 = new Option("ab", false, "Option without argument");
        Option option3 = new Option("b", "option", false, "Option without argument");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        assertEquals(2, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertTrue(options.getMatchingOptions("a").contains("ab"));
    }

    @Test
    public void testHasOption() {
        // Test hasOption with short name
        Option option1 = new Option("a", false, "Option without argument");
        options.addOption(option1);
        assertTrue(options.hasOption("a"));
        
        // Test hasOption with long name
        Option option2 = new Option("b", "option", false, "Option with long name");
        options.addOption(option2);
        assertTrue(options.hasOption("option"));
        
        // Test hasOption with unknown option
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        // Test hasLongOption with long option
        Option option = new Option("a", "option", false, "Option with long name");
        options.addOption(option);
        assertTrue(options.hasLongOption("option"));
        
        // Test hasLongOption with short option
        assertFalse(options.hasLongOption("a"));
        
        // Test hasLongOption with unknown option
        assertFalse(options.hasLongOption("c"));
    }

    @Test
    public void testHasShortOption() {
        // Test hasShortOption with short option
        Option option = new Option("a", "option", false, "Option with long name");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
        
        // Test hasShortOption with long option
        assertFalse(options.hasShortOption("option"));
        
        // Test hasShortOption with unknown option
        assertFalse(options.hasShortOption("b"));
    }
}
