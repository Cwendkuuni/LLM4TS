package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
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
        Option option1 = new Option("a", "aaa", false, "option1");
        Option option2 = new Option("b", "bbb", false, "option2");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(group, options.getOptionGroup(option1));
    }
    
    @Test
    public void testAddOption_shortNameOnly() {
        Option option = new Option("a", false, "option1");
        
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testAddOption_shortNameAndHasArg() {
        Option option = new Option("a", true, "option1");
        
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testAddOption_shortAndLongNameAndHasArg() {
        Option option = new Option("a", "aaa", true, "option1");
        
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
        assertEquals(option, options.getOption("aaa"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "aaa", false, "option1");
        Option option2 = new Option("b", "bbb", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "aaa", false, "option1");
        Option option2 = new Option("b", "bbb", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        options.getOptionGroup(new Option("c", "ccc", false, "option3"));
        
        assertTrue(options.getRequiredOptions().contains(option1));
        assertTrue(options.getRequiredOptions().contains(option2));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "aaa", false, "option1");
        Option option2 = new Option("b", "bbb", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("aaa"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "abc", false, "option1");
        Option option2 = new Option("a", "def", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.getMatchingOptions("a").contains("abc"));
        assertTrue(options.getMatchingOptions("a").contains("def"));
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "abc", false, "option1");
        Option option2 = new Option("b", "def", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("abc"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("def"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "abc", false, "option1");
        Option option2 = new Option("b", "def", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("abc"));
        assertTrue(options.hasLongOption("def"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "abc", false, "option1");
        Option option2 = new Option("b", "def", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "abc", false, "option1");
        Option option2 = new Option("b", "def", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals("[ Options: [ short {a=option1, b=option2} ] [ long {abc=option1, def=option2} ]", options.toString());
    }
}