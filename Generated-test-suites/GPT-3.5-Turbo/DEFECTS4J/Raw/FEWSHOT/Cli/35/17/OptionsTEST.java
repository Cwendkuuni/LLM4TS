package org.apache.commons.cli;

import static org.junit.Assert.*;

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
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option("a", "apple", false, "apple option"));

        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("b", "banana", true, "banana option"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertTrue(options.getOptionGroups().contains(group1));
        assertTrue(options.getOptionGroups().contains(group2));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("-a"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option2, options.getOption("-b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("abc", "abcdef", true, "abcdef option");
        Option option3 = new Option("ab", "abcd", false, "abcd option");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertTrue(options.getMatchingOptions("a").contains("apple"));
        assertTrue(options.getMatchingOptions("abc").contains("abcdef"));
        assertTrue(options.getMatchingOptions("ab").contains("abcd"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("-a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("-b"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.hasLongOption("banana"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option("a", "apple", false, "apple option"));

        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("b", "banana", true, "banana option"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertEquals(group1, options.getOptionGroup(new Option("a", "apple", false, "apple option")));
        assertEquals(group2, options.getOptionGroup(new Option("b", "banana", true, "banana option")));
        assertNull(options.getOptionGroup(new Option("c", "carrot", false, "carrot option")));
    }
}