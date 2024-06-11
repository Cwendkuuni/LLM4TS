package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("o2"));
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Option");
        
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("option"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("o1"));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "Option");
        
        Options options = new Options();
        options.addOption(option);
        
        assertNotNull(options.getOption("o"));
        assertEquals(option, options.getOption("o"));
        assertNull(options.getOption("option"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(1, options.getMatchingOptions("o1").size());
        assertEquals(2, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("o1"));
        assertTrue(options.getMatchingOptions("o").contains("o2"));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option");
        
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", false, "Option");
        
        Options options = new Options();
        options.addOption(option);
        
        assertFalse(options.hasLongOption("o"));
        assertTrue(options.hasLongOption("option"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", false, "Option");
        
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("option"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("o3", "option3", false, "Option 3")));
    }
    
}