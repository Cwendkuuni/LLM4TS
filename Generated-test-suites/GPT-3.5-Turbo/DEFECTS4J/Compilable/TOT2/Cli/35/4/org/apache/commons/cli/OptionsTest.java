package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an OptionGroup
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Description for option1");
        Option option2 = new Option("b", "option2", true, "Description for option2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        // Test adding an option
        Options options = new Options();
        Option option = new Option("c", "option", false, "Description for option");
        options.addOption(option);
        
        assertTrue(options.hasOption("c"));
    }

    @Test
    public void testGetOption() {
        // Test retrieving an option by name
        Options options = new Options();
        Option option = new Option("d", "option", true, "Description for option");
        options.addOption(option);
        
        assertEquals(option, options.getOption("d"));
    }

    @Test
    public void testHasOption() {
        // Test checking if an option exists
        Options options = new Options();
        Option option = new Option("e", "option", false, "Description for option");
        options.addOption(option);
        
        assertTrue(options.hasOption("e"));
    }
    
    // Add more test cases for other methods in the Options class

}