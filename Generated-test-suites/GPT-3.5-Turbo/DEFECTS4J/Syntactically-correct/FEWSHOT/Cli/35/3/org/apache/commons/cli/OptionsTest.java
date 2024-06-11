package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        assertTrue(options.getRequiredOptions().contains(option1));
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        
        assertEquals(option, options.getOption("f"));
        assertEquals(option, options.getOption("file"));
        assertEquals(null, options.getOption("d"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        options.addOption(option1);
        options.addOption(option2);
        
        List<String> matchingOptions = options.getMatchingOptions("f");
        assertTrue(matchingOptions.contains("file"));
        
        matchingOptions = options.getMatchingOptions("dir");
        assertFalse(matchingOptions.contains("file"));
        assertTrue(matchingOptions.contains("directory"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertFalse(options.hasOption("d"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasLongOption("f"));
        assertFalse(options.hasLongOption("d"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "file option");
        options.addOption(option);
        
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("file"));
        assertFalse(options.hasShortOption("d"));
    }
}