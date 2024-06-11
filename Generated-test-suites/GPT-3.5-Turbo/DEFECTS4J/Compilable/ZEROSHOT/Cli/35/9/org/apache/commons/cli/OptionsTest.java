package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "description");
        
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("x"));
        assertFalse(options.hasOption("unknown"));
    }
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("o2"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("option2"));
        assertFalse(options.hasOption("x"));
        assertFalse(options.hasOption("unknown"));
        
        OptionGroup retrievedGroup = options.getOptionGroup(option1);
        assertEquals(group, retrievedGroup);
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("o1"));
        assertFalse(options.getRequiredOptions().contains("o2"));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("o1"));
        assertEquals(option1, options.getOption("option1"));
        assertEquals(option2, options.getOption("o2"));
        assertEquals(option2, options.getOption("option2"));
        assertNull(options.getOption("x"));
        assertNull(options.getOption("unknown"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        Option option3 = new Option("o3", "another", false, "description3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertEquals(2, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("option1"));
        assertTrue(options.getMatchingOptions("o").contains("option2"));
        assertEquals(1, options.getMatchingOptions("an").size());
        assertTrue(options.getMatchingOptions("an").contains("another"));
        assertEquals(0, options.getMatchingOptions("x").size());
        assertEquals(0, options.getMatchingOptions("unknown").size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("o2"));
        assertTrue(options.hasOption("option2"));
        assertFalse(options.hasOption("x"));
        assertFalse(options.hasOption("unknown"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("option1"));
        assertTrue(options.hasLongOption("option2"));
        assertFalse(options.hasLongOption("x"));
        assertFalse(options.hasLongOption("unknown"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("o1"));
        assertTrue(options.hasShortOption("o2"));
        assertFalse(options.hasShortOption("x"));
        assertFalse(options.hasShortOption("unknown"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("x", "unknown", false, "description")));
    }
    
}