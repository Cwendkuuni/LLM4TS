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
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", false, "option2 description");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertNull(options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option", true, "option description");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testAddOptionWithShortNameOnly() {
        options.addOption("a", "option description");

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("option"));
        assertEquals("a", options.getOption("a").getOpt());
    }

    @Test
    public void testAddOptionWithShortNameAndArg() {
        options.addOption("a", true, "option description");

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("option"));
        assertTrue(options.getOption("a").hasArg());
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        options.addOption("a", "option", true, "option description");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
        assertEquals("a", options.getOption("a").getOpt());
        assertEquals("option", options.getOption("option").getLongOpt());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", false, "option2 description");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1", true, "option1 description");
        Option option2 = new Option("b", "option2", true, "option2 description");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1.getOpt()));
        assertTrue(options.getRequiredOptions().contains(option2.getOpt()));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", false, "option2 description");
        Option option3 = new Option("c", "option3", false, "option3 description");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals(2, options.getMatchingOptions("o").size());
        assertEquals(0, options.getMatchingOptions("d").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "option", true, "option description");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "option", true, "option description");

        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("a"));
        assertFalse(options.hasLongOption("b"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "option", true, "option description");

        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("option"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", false, "option2 description");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertNull(options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", false, "option2 description");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {" + option1.getOpt() + "=" + option1.toString()
                + ", " + option2.getOpt() + "=" + option2.toString() + "} ] [ long {}]]";

        assertEquals(expected, options.toString());
    }
}