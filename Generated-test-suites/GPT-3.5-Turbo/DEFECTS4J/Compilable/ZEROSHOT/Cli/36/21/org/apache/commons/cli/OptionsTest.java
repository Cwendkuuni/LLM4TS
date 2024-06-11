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
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasLongOption("verbose"));
        assertFalse(options.hasShortOption("v"));
    }
    
    @Test
    public void testAddOptionWithArgument() {
        Option option = new Option("f", "file", true, "File option");
        
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasShortOption("f"));
    }
    
    @Test
    public void testAddOptionWithShortNameOnly() {
        options.addOption("o", "Output option");
        
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasLongOption("Output option"));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("o", "output", false, "Output option");
        Option option2 = new Option("f", "file", true, "File option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("o"));
        assertEquals(option2, options.getOption("f"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o", "output", false, "Output option");
        Option option2 = new Option("f", "file", true, "File option");
        Option option3 = new Option("a", "add", false, "Add option");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertEquals(2, options.getMatchingOptions("o").size());
        assertEquals(1, options.getMatchingOptions("f").size());
        assertEquals(0, options.getMatchingOptions("x").size());
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasLongOption("verbose"));
        assertFalse(options.hasOption("x"));
    }
    
    @Test
    public void testHasShortOption() {
        options.addOption("o", "Output option");
        
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("x"));
    }
    
    @Test
    public void testHasLongOption() {
        options.addOption("o", "Output option");
        
        assertTrue(options.hasLongOption("Output option"));
        assertFalse(options.hasLongOption("x"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        OptionGroup group2 = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        group1.addOption(option1);
        group2.addOption(option2);
        
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        
        assertEquals(group1, options.getOptionGroup(option1));
        assertEquals(group2, options.getOptionGroup(option2));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("v", "verbose", false, "Verbose option");
        
        options.addOption(option);
        
        assertEquals("[ Options: [ short {v=Option: v, verbose} ] [ long {verbose=Option: v, verbose} ]", options.toString());
    }
}