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
        group.addOption(new Option("a", "optionA", false, "Option A"));
        group.addOption(new Option("b", "optionB", true, "Option B"));

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption_shortName_noArgument() {
        options.addOption("o", "Option O");

        assertTrue(options.hasOption("o"));
        assertFalse(options.getOption("o").hasArg());
    }

    @Test
    public void testAddOption_shortName_withArgument() {
        options.addOption("p", true, "Option P");

        assertTrue(options.hasOption("p"));
        assertTrue(options.getOption("p").hasArg());
    }

    @Test
    public void testAddOption_shortName_longName_noArgument() {
        options.addOption("o", "optionO", false, "Option O");

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("optionO"));
        assertFalse(options.getOption("o").hasArg());
        assertFalse(options.getOption("optionO").hasArg());
    }

    @Test
    public void testAddOption_shortName_longName_withArgument() {
        options.addOption("p", "optionP", true, "Option P");

        assertTrue(options.hasOption("p"));
        assertTrue(options.hasLongOption("optionP"));
        assertTrue(options.getOption("p").hasArg());
        assertTrue(options.getOption("optionP").hasArg());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");
        options.addOption(option1);
        options.addOption(option2);

        options.getOption("o").setRequired(true);
        assertTrue(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));

        options.getOption("o").setRequired(false);
        options.getOption("optionO").setRequired(true);
        assertTrue(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("o"));
        assertEquals(option2, options.getOption("p"));
        assertEquals(option1, options.getOption("optionO"));
        assertEquals(option2, options.getOption("optionP"));
    }

    @Test
    public void testGetOption_matchingOptions() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("o").size());
        assertEquals(1, options.getMatchingOptions("optionP").size());
        assertEquals(0, options.getMatchingOptions("q").size());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("p"));
        assertTrue(options.hasOption("optionO"));
        assertTrue(options.hasOption("optionP"));
        assertFalse(options.hasOption("q"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("optionO"));
        assertTrue(options.hasLongOption("optionP"));
        assertFalse(options.hasLongOption("q"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("o"));
        assertTrue(options.hasShortOption("p"));
        assertFalse(options.hasShortOption("q"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "optionO", false, "Option O");
        group.addOption(option1);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "optionO", false, "Option O");
        Option option2 = new Option("p", "optionP", true, "Option P");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {o=Option O, p=Option P} ] [ long {optionO=Option O, optionP=Option P} ]";
        assertEquals(expected, options.toString());
    }
}