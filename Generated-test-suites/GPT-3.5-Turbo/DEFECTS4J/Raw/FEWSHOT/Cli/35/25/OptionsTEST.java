
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        options.addOption(option1);
        
        assertTrue(options.getOptions().contains(option1));
        
        Option option2 = new Option("b", "banana", true, "banana option");
        options.addOption(option2);
        
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.getRequiredOptions().isEmpty());
        
        option1.setRequired(true);
        assertFalse(options.getRequiredOptions().isEmpty());
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        Option option3 = new Option("c", "cat", false, "cat option");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertEquals(2, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("apple"));
        
        assertEquals(1, options.getMatchingOptions("b").size());
        assertTrue(options.getMatchingOptions("b").contains("banana"));
        
        assertEquals(0, options.getMatchingOptions("d").size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("banana"));
        assertFalse(options.hasOption("c"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.hasLongOption("banana"));
        assertFalse(options.hasLongOption("cat"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        Option option3 = new Option("c", "cat", false, "cat option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        options.addOption(option3);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertEquals(null, options.getOptionGroup(option3));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals("[ Options: [ short {a:Option - apple option, b:Option - banana option} ] [ long {apple:Option - apple option, banana:Option - banana option} ]", options.toString());
    }
}
