package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        Option opt1 = new Option("f", "file", true, "File option");
        Option opt2 = new Option("d", "directory", false, "Directory option");
        OptionGroup group = new OptionGroup();
        group.addOption(opt1);
        group.addOption(opt2);

        Options options = new Options();
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOptionShortDescriptionOnly() {
        Options options = new Options();
        options.addOption("f", "File option");

        assertTrue(options.hasOption("f"));
    }

    @Test
    public void testAddOptionShortWithArg() {
        Options options = new Options();
        options.addOption("f", true, "File option");

        assertTrue(options.hasOption("f"));
    }

    @Test
    public void testAddOptionShortAndLong() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testAddOption() {
        Option opt = new Option("f", "file", true, "File option");

        Options options = new Options();
        options.addOption(opt);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");
        options.addOption("d", "directory", false, "Directory option");

        List<Option> optionList = options.getOptions();
        assertEquals(2, optionList.size());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");
        options.addOption("d", "directory", false, "Directory option");

        List requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");

        Option opt = options.getOption("f");
        assertNotNull(opt);
        assertEquals("f", opt.getOpt());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");
        options.addOption("d", "directory", false, "Directory option");

        List<String> matchingOpts = options.getMatchingOptions("d");
        assertEquals(1, matchingOpts.size());
        assertEquals("directory", matchingOpts.get(0));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertFalse(options.hasOption("d"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");

        assertFalse(options.hasLongOption("f"));
        assertTrue(options.hasLongOption("file"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");

        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("file"));
    }

    @Test
    public void testGetOptionGroup() {
        Option opt1 = new Option("f", "file", true, "File option");
        Option opt2 = new Option("d", "directory", false, "Directory option");
        OptionGroup group = new OptionGroup();
        group.addOption(opt1);
        group.addOption(opt2);

        Options options = new Options();
        options.addOptionGroup(group);

        OptionGroup result = options.getOptionGroup(opt1);
        assertEquals(group, result);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");

        String expected = "[ Options: [ short {f=" + options.getOption("f") + "} ] [ long {file=Option [ opt=f, longOpt=file, hasArg=true, description=File option, required=false ]} ]]";
        assertEquals(expected, options.toString());
    }
}