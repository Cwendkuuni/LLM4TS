package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "file", false, "file option"));
        group.addOption(new Option("d", "directory", false, "directory option"));
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("d"));        
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("f", true, "file option");
        options.addOption("d", false, "directory option");
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("d"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "file option");
        
        assertEquals(1, options.getOptions().size());
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "file option");
        option.setRequired(true);
        
        options.addOption(option);
        
        assertTrue(options.getRequiredOptions().contains(option.getKey()));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("f"));
        assertEquals(option2, options.getOption("directory"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "file option");
        options.addOption("d", "dir", true, "directory option");
        
        assertEquals(2, options.getMatchingOptions("f").size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("f", true, "file option");
        
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("d"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("f", true, "file option");
        
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasLongOption("dir"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("f", true, "file option");
        
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("d"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "file", false, "file option"));
        group.addOption(new Option("d", "directory", false, "directory option"));
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(options.getOption("f")));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("f", true, "file option");
        
        assertEquals("[ Options: [ short {f=org.apache.commons.cli.Option@...} ] [ long {file=org.apache.commons.cli.Option@...} ]", options.toString());
    }
}