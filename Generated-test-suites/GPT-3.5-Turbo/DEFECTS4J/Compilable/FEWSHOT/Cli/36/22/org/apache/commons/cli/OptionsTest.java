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
        group.addOption(new Option("a", "add", false, "add an entry"));
        group.addOption(new Option("v", "version", false, "display the version number"));
        
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("add"));
        assertTrue(options.hasOption("version"));
        
        OptionGroup retrievedGroup = options.getOptionGroup(options.getOption("a"));
        assertEquals(group, retrievedGroup);
    }
    
    @Test
    public void testAddOption() {
        options.addOption(new Option("h", "help", false, "display help"));
        
        assertTrue(options.hasOption("h"));
        assertTrue(options.hasOption("help"));
        assertFalse(options.hasOption("test"));
    }
    
    @Test
    public void testGetOptions() {
        options.addOption(new Option("a", "add", false, "add an entry"));
        options.addOption(new Option("v", "version", false, "display the version number"));
        
        assertEquals(2, options.getOptions().size());
    }
    
    @Test
    public void testGetRequiredOptions() {
        options.addOption(new Option("a", "add", false, "add an entry"));
        options.addOption(new Option("v", "version", false, "display the version number"));
        options.getOption("a").setRequired(true);
        
        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("a"));
    }
    
    @Test
    public void testGetOption() {
        options.addOption(new Option("a", "add", false, "add an entry"));
        
        Option option = options.getOption("a");
        assertEquals("a", option.getOpt());
        assertEquals("add", option.getLongOpt());
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testGetOptionWithHyphens() {
        options.addOption(new Option("D", "define", true, "define a system property"));
        
        Option option = options.getOption("-D");
        assertEquals("D", option.getOpt());
        assertEquals("define", option.getLongOpt());
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testGetMatchingOptions() {
        options.addOption(new Option("D", "define", true, "define a system property"));
        options.addOption(new Option("v", "version", false, "display the version number"));
        
        assertEquals(1, options.getMatchingOptions("v").size());
        assertTrue(options.getMatchingOptions("v").contains("version"));
    }
    
    @Test
    public void testHasOption() {
        options.addOption(new Option("D", "define", true, "define a system property"));
        options.addOption(new Option("v", "version", false, "display the version number"));
        
        assertTrue(options.hasOption("D"));
        assertTrue(options.hasOption("define"));
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("version"));
        assertFalse(options.hasOption("test"));
    }
    
    @Test
    public void testHasLongOption() {
        options.addOption(new Option("D", "define", true, "define a system property"));
        
        assertTrue(options.hasLongOption("define"));
        assertFalse(options.hasLongOption("version"));
    }
    
    @Test
    public void testHasShortOption() {
        options.addOption(new Option("D", "define", true, "define a system property"));
        
        assertTrue(options.hasShortOption("D"));
        assertFalse(options.hasShortOption("v"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "add", false, "add an entry"));
        group.addOption(new Option("v", "version", false, "display the version number"));
        
        options.addOptionGroup(group);
        
        Option option = options.getOption("a");
        OptionGroup retrievedGroup = options.getOptionGroup(option);
        assertEquals(group, retrievedGroup);
    }
    
    @Test
    public void testToString() {
        options.addOption(new Option("h", "help", false, "display help"));
        
        String expected = "[ Options: [ short {h=Option opt: h, longopt: help, hasArg: false, description: display help} ] [ long {help=Option opt: h, longopt: help, hasArg: false, description: display help} ]]";
        assertEquals(expected, options.toString());
    }
}