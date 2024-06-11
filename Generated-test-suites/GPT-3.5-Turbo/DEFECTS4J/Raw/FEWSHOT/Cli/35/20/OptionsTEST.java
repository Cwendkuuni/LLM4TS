package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("f", "file", false, "file option");
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasShortOption("file"));
        assertFalse(options.hasLongOption("f"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "file", false, "file option"));
        options.addOptionGroup(group);
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasShortOption("file"));
        assertFalse(options.hasLongOption("f"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        assertEquals(option, options.getOption("f"));
        assertEquals(option, options.getOption("file"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("p", "print", false, "print option");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(1, options.getMatchingOptions("f").size());
        assertEquals(2, options.getMatchingOptions("").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertFalse(options.hasOption("p"));
        assertFalse(options.hasOption("print"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasLongOption("print"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("p"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("p", "print", false, "print option");
        options.addOption(option1);
        options.addOption(option2);
        String expected = "[ Options: [ short {f=org.apache.commons.cli.Option }, {p=org.apache.commons.cli.Option }] [ long {file=org.apache.commons.cli.Option }, {print=org.apache.commons.cli.Option }]";
        assertEquals(expected, options.toString());
    }
}