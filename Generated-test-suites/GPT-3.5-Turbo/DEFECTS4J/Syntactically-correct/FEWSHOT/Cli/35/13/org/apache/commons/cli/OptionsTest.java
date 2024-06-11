package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", null, false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", null, false, "Option 2");
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertNotNull(options.getOption("a"));
        assertNotNull(options.getOption("b"));
        assertNotNull(options.getOption("option2"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("aa", null, false, "Option 2");
        Option option3 = new Option("b", "option2", true, "Option 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertTrue(options.getMatchingOptions("a").contains("aa"));

        assertEquals(1, options.getMatchingOptions("option2").size());
        assertTrue(options.getMatchingOptions("option2").contains("b"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("option2"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertFalse(options.hasLongOption("a"));
        assertTrue(options.hasLongOption("option2"));
        assertFalse(options.hasLongOption("c"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("option2"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", null, false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "option3", true, "Option 3")));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {a=Option 1, b=Option 2} ] [ long {option2=Option 2} ]", options.toString());
    }
}