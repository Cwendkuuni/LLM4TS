package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "aa", false, "Option A");
        Option option2 = new Option("b", "bb", false, "Option B");
        Option option3 = new Option("c", "cc", false, "Option C");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("c"));
        
        assertTrue(options.hasLongOption("aa"));
        assertTrue(options.hasLongOption("bb"));
        assertTrue(options.hasLongOption("cc"));
        
        assertTrue(options.getRequiredOptions().contains(optionGroup));
        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
        assertEquals(optionGroup, options.getOptionGroup(option3));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", "bb", true, "Option B");
        Option option3 = new Option("c", false, "Option C");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("c"));
        
        assertFalse(options.hasLongOption("aa"));
        assertTrue(options.hasLongOption("bb"));
        assertFalse(options.hasLongOption("cc"));
        
        assertTrue(options.getRequiredOptions().contains("a"));
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option3, options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("aa", false, "Option AA");
        Option option3 = new Option("ab", false, "Option AB");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertEquals(3, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertTrue(options.getMatchingOptions("a").contains("aa"));
        assertTrue(options.getMatchingOptions("a").contains("ab"));
        
        assertEquals(1, options.getMatchingOptions("aa").size());
        assertTrue(options.getMatchingOptions("aa").contains("aa"));
        
        assertEquals(0, options.getMatchingOptions("abc").size());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", "bb", true, "Option B");

        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        
        assertFalse(options.hasOption("c"));
        assertFalse(options.hasOption("aa"));
        assertFalse(options.hasOption("bb"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", "bb", true, "Option B");

        options.addOption(option1);
        options.addOption(option2);
        
        assertFalse(options.hasLongOption("a"));
        assertTrue(options.hasLongOption("bb"));
        
        assertFalse(options.hasLongOption("c"));
        assertFalse(options.hasLongOption("aa"));
        assertFalse(options.hasLongOption("b"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", "bb", true, "Option B");

        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("bb"));
        
        assertFalse(options.hasShortOption("c"));
        assertFalse(options.hasShortOption("aa"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", "bb", true, "Option B");

        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        
        assertNull(options.getOption("c"));
        assertNull(options.getOption("aa"));
        assertNull(options.getOption("bb"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", "bb", true, "Option B");

        options.addOption(option1);
        options.addOption(option2);
        
        String expectedToString = "[ Options: [ short {a=Option A, b=Option B }";
        
        assertEquals(expectedToString, options.toString());
    }
}