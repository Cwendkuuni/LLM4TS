package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        
        // Test adding an OptionGroup
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        
        // Test adding an option with only short name
        options.addOption("a", "Option A");
        assertTrue(options.hasOption("a"));
        
        // Test adding an option with short name and hasArg
        options.addOption("b", true, "Option B");
        assertTrue(options.hasOption("b"));
        
        // Test adding an option with short and long name and hasArg
        options.addOption("c", "option3", true, "Option C");
        assertTrue(options.hasOption("c"));
        assertTrue(options.hasLongOption("option3"));
        assertTrue(options.hasShortOption("c"));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        
        Option option1 = new Option("a", "option1", false, "Option 1");
        options.addOption(option1);
        
        // Test getOption for short name
        assertEquals(option1, options.getOption("a"));
        
        // Test getOption for long name
        assertEquals(option1, options.getOption("option1"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        // Test getMatchingOptions
        assertEquals(2, options.getMatchingOptions("o").size());
        assertEquals(1, options.getMatchingOptions("option").size());
        assertEquals(0, options.getMatchingOptions("z").size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        
        Option option1= new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);
        
        // Test hasOption for short name
        assertTrue(options.hasOption("a"));
        
        // Test hasOption for long name
        assertTrue(options.hasOption("option1"));
        
        // Test hasOption for non-existing option
        assertFalse(options.hasOption("c"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        String expectedString = "[ Options: [ short {a=Option 1, b=Option 2} ] [ long {option1=Option 1, option2=Option 2} ]";
        assertEquals(expectedString, options.toString());
    }

}