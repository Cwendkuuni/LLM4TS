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
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testAddOptionWithShortName() {
        options.addOption("a", "Option");

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionWithShortNameAndDescription() {
        options.addOption("a", "Option", false, "Option");

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionWithShortNameAndHasArg() {
        options.addOption("a", true, "Option");

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionWithShortNameAndHasArgAndDescription() {
        options.addOption("a", true, "Option");

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionWithShortNameAndLongName() {
        options.addOption("a", "option", false, "Option");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testAddOptionWithShortNameAndLongNameAndHasArg() {
        options.addOption("a", "option", true, "Option");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertFalse(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        Option option3 = new Option("c", "option3", false, "Option 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("o").size());
        assertEquals(3, options.getMatchingOptions("o").size());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");

        options.addOption(option1);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");

        options.addOption(option1);

        assertTrue(options.hasLongOption("option1"));
        assertFalse(options.hasLongOption("a"));
        assertFalse(options.hasLongOption("b"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");

        options.addOption(option1);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("option1"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option 1, b=Option 2} ] [ long {option1=Option 1, option2=Option 2} ]";
        assertEquals(expected, options.toString());
    }
}