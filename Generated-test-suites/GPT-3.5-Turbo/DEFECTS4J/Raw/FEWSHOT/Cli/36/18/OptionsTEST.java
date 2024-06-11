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
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        group1.addOption(option1);
        group1.addOption(option2);

        OptionGroup group2 = new OptionGroup();
        Option option3 = new Option("s", "size", true, "size option");
        group2.addOption(option3);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertEquals("file option", options.getOption("f").getDescription());
        assertEquals("directory option", options.getOption("d").getDescription());
        assertEquals("size option", options.getOption("s").getDescription());
        assertEquals(group1, options.getOptionGroup(option1));
        assertEquals(group1, options.getOptionGroup(option2));
        assertEquals(group2, options.getOptionGroup(option3));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("file option", options.getOption("f").getDescription());
        assertEquals("directory option", options.getOption("d").getDescription());
    }

    @Test
    public void testAddOption_stringDescription() {
        options.addOption("f", "file", true, "file option");

        assertEquals("file option", options.getOption("f").getDescription());
    }

    @Test
    public void testAddOption_booleanDescription() {
        options.addOption("f", true, "file option");

        assertEquals("file option", options.getOption("f").getDescription());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(0, options.getRequiredOptions().size());

        option1.setRequired(true);
        options.addOption(option1);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("f"));
        assertEquals(option2, options.getOption("d"));
        assertNull(options.getOption("i"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("file").size());
        assertTrue(options.getMatchingOptions("file").contains("file"));

        assertEquals(2, options.getMatchingOptions("f").size());
        assertTrue(options.getMatchingOptions("f").contains("f"));
        assertTrue(options.getMatchingOptions("f").contains("file"));

        assertTrue(options.getMatchingOptions("i").isEmpty());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("directory"));
        assertFalse(options.hasOption("i"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("file"));
        assertTrue(options.hasLongOption("directory"));
        assertFalse(options.hasLongOption("input"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("f"));
        assertTrue(options.hasShortOption("d"));
        assertFalse(options.hasShortOption("i"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", true, "file option");
        options.addOption(option1);

        String expected = "[ Options: [ short {f=Option[ shortOpt= 'f' longOpt= 'file' hasArg=true description='file option']} ] [ long {file=Option[ shortOpt= 'f' longOpt= 'file' hasArg=true description='file option']} ]";
        assertEquals(expected, options.toString());
    }
}