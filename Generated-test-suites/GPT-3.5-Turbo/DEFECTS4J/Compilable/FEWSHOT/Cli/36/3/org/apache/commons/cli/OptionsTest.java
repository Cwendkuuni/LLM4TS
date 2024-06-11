package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        group1.addOption(option1);
        group1.addOption(option2);
        Options options = new Options();
        options.addOptionGroup(group1);
        assertEquals(group1, options.getOptionGroup(option1));
        assertEquals(group1, options.getOptionGroup(option2));
        assertTrue(options.getOptionGroups().contains(group1));

        OptionGroup group2 = new OptionGroup();
        Option option3 = new Option("o", "output", true, "Output option");
        Option option4 = new Option("p", "print", false, "Print option");
        group2.addOption(option3);
        group2.addOption(option4);
        options.addOptionGroup(group2);
        assertEquals(group2, options.getOptionGroup(option3));
        assertEquals(group2, options.getOptionGroup(option4));
        assertTrue(options.getOptionGroups().contains(group2));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(option1, options.getOption("f"));
        assertEquals(option1, options.getOption("file"));
        assertEquals(option2, options.getOption("d"));
        assertEquals(option2, options.getOption("directory"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        option2.setRequired(true);
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("folder", "folder", false, "Folder option");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(1, options.getMatchingOptions("fi").size());
        assertTrue(options.getMatchingOptions("fi").contains("file"));
        assertEquals(1, options.getMatchingOptions("fol").size());
        assertTrue(options.getMatchingOptions("fol").contains("folder"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("f", "file", true, "File option");
        Options options = new Options();
        options.addOption(option1);
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("f", "file", true, "File option");
        Options options = new Options();
        options.addOption(option1);
        assertTrue(options.hasLongOption("file"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("f", "file", true, "File option");
        Options options = new Options();
        options.addOption(option1);
        assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        String expected = "[ Options: [ short {f=Option[, f, file, true, File option]} ] [ long {file=Option[, f, file, true, File option], directory=Option[, d, directory, false, Directory option]} ]";
        assertEquals(expected, options.toString());
    }
}