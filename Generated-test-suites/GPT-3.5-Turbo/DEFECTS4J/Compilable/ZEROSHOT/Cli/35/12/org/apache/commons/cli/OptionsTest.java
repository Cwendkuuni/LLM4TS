package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option("a", "test1"));
        group1.addOption(new Option("b", "test2"));
        group1.setRequired(true);

        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("c", "test3"));
        group2.addOption(new Option("d", "test4"));
        
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        
        assertEquals(2, options.getOptionGroups().size());
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", "test", false, "test option");
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("test"));
        assertFalse(options.hasShortOption("test"));
        
        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("test", option.getLongOpt());
        assertEquals("test option", option.getDescription());
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        assertEquals(2, options.getOptions().size());
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        assertEquals(0, options.getRequiredOptions().size());
        
        options.getOption("a").setRequired(true);
        
        assertEquals(1, options.getRequiredOptions().size());
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("option a", option.getDescription());
        
        option = options.getOption("b");
        assertNotNull(option);
        assertEquals("b", option.getOpt());
        assertEquals("option b", option.getDescription());
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("abc", false, "option abc");
        options.addOption("acd", false, "option acd");
        options.addOption("bcd", false, "option bcd");
        
        assertEquals(1, options.getMatchingOptions("ab").size());
        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(3, options.getMatchingOptions("").size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        assertTrue(options.hasLongOption("option a"));
        assertFalse(options.hasLongOption("option b"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option("a", "test1"));
        group1.addOption(new Option("b", "test2"));
        
        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("c", "test3"));
        group2.addOption(new Option("d", "test4"));
        
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        
        Option option1 = options.getOption("a");
        Option option2 = options.getOption("c");
        
        assertEquals(group1, options.getOptionGroup(option1));
        assertEquals(group2, options.getOptionGroup(option2));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        String expected = "[ Options: [ short {a=Option: a}, {b=Option: b} ] [ long {option a=Option: a}, {option b=Option: b} ]";
        
        assertEquals(expected, options.toString());
    }
}