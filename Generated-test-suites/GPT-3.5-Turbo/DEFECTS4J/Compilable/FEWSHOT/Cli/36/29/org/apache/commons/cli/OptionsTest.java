package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("b", "bbb", false, "description");

        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "description");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertFalse(option.isRequired());
    }

    @Test
    public void testAddOptionWithShortNameAndDescription() {
        String opt = "t";
        String description = "description";

        options.addOption(opt, description);

        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertEquals(description, option.getDescription());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testAddOptionWithShortNameHasArgAndDescription() {
        String opt = "t";
        boolean hasArg = true;
        String description = "description";

        options.addOption(opt, hasArg, description);

        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertEquals(hasArg, option.hasArgs());
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testAddOptionWithShortNameLongNameHasArgAndDescription() {
        String shortOpt = "t";
        String longOpt = "test";
        boolean hasArg = true;
        String description = "description";

        options.addOption(shortOpt, longOpt, hasArg, description);

        Option option = options.getOption(shortOpt);
        assertNotNull(option);
        assertEquals(shortOpt, option.getOpt());
        assertEquals(longOpt, option.getLongOpt());
        assertEquals(hasArg, option.hasArgs());
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("b", "bbb", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("b", "bbb", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertFalse(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));

        option1.setRequired(true);
        option2.setRequired(true);

        assertTrue(options.getRequiredOptions().contains(option1));
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("b", "bbb", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("a1", "aaa1", false, "description");
        Option option3 = new Option("xyz", "xyz", false, "description");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertTrue(options.getMatchingOptions("a").contains("a1"));
        assertEquals(0, options.getMatchingOptions("b").size());
        assertEquals(1, options.getMatchingOptions("x").size());
        assertTrue(options.getMatchingOptions("x").contains("xyz"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("b", "bbb", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("a1", "aaa1", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("aaa"));
        assertTrue(options.hasLongOption("aaa1"));
        assertFalse(options.hasLongOption("bbb"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("a1", "aaa1", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("a1"));
        assertFalse(options.hasShortOption("bbb"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("b", "bbb", false, "description");

        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "ccc", false, "description")));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "aaa", false, "description");
        Option option2 = new Option("b", "bbb", false, "description");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option: aaa description, b=Option: bbb description} ] [ long {}]";
        assertEquals(expected, options.toString());
    }
}