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
    public void testAddOption() {
        Option option = new Option("f", "file", false, "Specify a file");
        assertEquals(Options.class, options.addOption(option).getClass());
    }
    
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "file", false, "Specify a file"));
        group.addOption(new Option("d", "directory", false, "Specify a directory"));
        
        assertEquals(Options.class, options.addOptionGroup(group).getClass());
    }
    
    @Test
    public void testAddOption_shortNameOnly() {
        assertEquals(Options.class, options.addOption("f", "Specify a file").getClass());
    }
    
    @Test
    public void testAddOption_shortNameAndHasArg() {
        assertEquals(Options.class, options.addOption("f", true, "Specify a file").getClass());
    }
    
    @Test
    public void testAddOption_shortAndLongName() {
        assertEquals(Options.class, options.addOption("f", "file", true, "Specify a file").getClass());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("f"));
        assertEquals(option2, options.getOption("d"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        Option option3 = new Option("d", "dir", false, "Specify a directory");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertEquals(2, options.getMatchingOptions("f").size());
        assertEquals(2, options.getMatchingOptions("d").size());
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertFalse(options.hasOption("s"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasLongOption("dir"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("d"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "file", false, "Specify a file"));
        group.addOption(new Option("d", "directory", false, "Specify a directory"));
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(new Option("f", "file", false, "Specify a file")));
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals("[ Options: [ short {f=Option{ shortOpt='f', longOpt='file', hasArg=false, description='Specify a file' }, d=Option{ shortOpt='d', longOpt='directory', hasArg=false, description='Specify a directory' }} ] [ long {file=Option{ shortOpt='f', longOpt='file', hasArg=false, description='Specify a file' }, directory=Option{ shortOpt='d', longOpt='directory', hasArg=false, description='Specify a directory' }} ]", options.toString());
    }
}