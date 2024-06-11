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
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
        assertTrue(options.getOptionGroup(option1) == group);
        assertTrue(options.getOptionGroup(option2) == group);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", true, "file option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getOption("f") == option);
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasLongOption("file"));
        assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testAddOption_NoLongOpt() {
        Option option = new Option("f", true, "file option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getOption("f") == option);
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasLongOption("file"));
        assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testAddOption_NoArg() {
        Option option = new Option("v", "verbose", false, "verbose option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getOption("v") == option);
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasLongOption("verbose"));
        assertTrue(options.hasShortOption("v"));
    }

    @Test
    public void testAddOption_MatchingOptions() {
        Option option1 = new Option("v", "version", false, "version option");
        Option option2 = new Option("vv", "verbose", false, "verbose option");
        Option option3 = new Option("verbose", false, "verbose option");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertTrue(options.getMatchingOptions("v").contains("v"));
        assertTrue(options.getMatchingOptions("v").contains("verbose"));
        assertTrue(options.getMatchingOptions("vv").contains("vv"));
        assertTrue(options.getMatchingOptions("vv").contains("verbose"));
        assertTrue(options.getMatchingOptions("ve").isEmpty());
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getRequiredOptions().isEmpty());

        option1.setRequired(true);
        options.addOption(option1);

        assertTrue(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("f", "file", true, "file option");

        options.addOption(option);

        assertEquals(option, options.getOption("f"));
        assertEquals(option, options.getOption("--file"));
        assertEquals(option, options.getOption("-f"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("f", "file", true, "file option");

        options.addOption(option);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("--file"));
        assertTrue(options.hasOption("-f"));
        assertFalse(options.hasOption("d"));
        assertFalse(options.hasOption("--directory"));
        assertFalse(options.hasOption("-d"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("f", "file", true, "file option");

        options.addOption(option);

        assertFalse(options.hasLongOption("f"));
        assertTrue(options.hasLongOption("file"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("f", "file", true, "file option");

        options.addOption(option);

        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("file"));
    }
}