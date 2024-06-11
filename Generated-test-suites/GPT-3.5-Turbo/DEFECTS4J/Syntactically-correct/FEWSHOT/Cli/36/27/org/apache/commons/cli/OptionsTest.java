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
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        Option option3 = new Option("c", "carrot", false, "carrot option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("banana"));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("apple"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option2, options.getOption("banana"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        option1.setRequired(true);
        option2.setRequired(true);

        assertTrue(options.getRequiredOptions().contains(option1));
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("apple"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option2, options.getOption("banana"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");
        Option option3 = new Option("c", "carrot", true, "carrot option");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(0, options.getMatchingOptions("d").size());
        assertEquals(2, options.getMatchingOptions("b").size());
        assertTrue(options.getMatchingOptions("b").contains("banana"));
        assertTrue(options.getMatchingOptions("b").contains("banana"));
        assertTrue(options.getMatchingOptions("car").contains("carrot"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("banana"));
        assertFalse(options.hasOption("c"));
        assertFalse(options.hasOption("carrot"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.hasLongOption("banana"));
        assertFalse(options.hasLongOption("carrot"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        Option option3 = new Option("c", "carrot", false, "carrot option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", true, "banana option");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=apple, b=banana} ] [ long {apple=apple, banana=banana} ]";
        assertEquals(expected, options.toString());
    }
}