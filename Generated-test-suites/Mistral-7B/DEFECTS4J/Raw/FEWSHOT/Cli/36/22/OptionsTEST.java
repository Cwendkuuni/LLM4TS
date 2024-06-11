package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertIterableEquals;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testGroup");
        Options options = new Options();
        options.addOptionGroup(optionGroup);

        assertSame(optionGroup, options.getOptionGroup(optionGroup));

        assertEquals(Collections.singletonList(optionGroup), options.getOptionGroups());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionGroupNull() {
        Options options = new Options();
        options.addOptionGroup(null);
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("group1");
        OptionGroup optionGroup2 = new OptionGroup("group2");
        Options options = new Options();
        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);

        assertIterableEquals(
            Arrays.asList(optionGroup1, optionGroup2),
            options.getOptionGroups()
        );
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("false"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasShortOption("t"));
        assertFalse(options.hasShortOption("f"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("test", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasLongOption("test"));
        assertFalse(options.hasLongOption("false"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertSame(option, options.getOption("t"));
        assertSame(option, options.getOption("test"));
        assertNull(options.getOption("false"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("opt1", "opt1", false, "Option 1");
        Option option2 = new Option("opt2", "opt2, opt3", true, "Option 2");
        Option option3 = new Option("opt1", "opt4, opt1", false, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertIterableEquals(Arrays.asList("opt1", "opt1"), options.getMatchingOptions("opt1"));
        assertIterableEquals(Arrays.asList("opt2"), options.getMatchingOptions("opt2"));
        assertIterableEquals(Collections.emptyList(), options.getMatchingOptions("opt3"));
        assertIterableEquals(Collections.emptyList(), options.getMatchingOptions("opt4"));
    }
}