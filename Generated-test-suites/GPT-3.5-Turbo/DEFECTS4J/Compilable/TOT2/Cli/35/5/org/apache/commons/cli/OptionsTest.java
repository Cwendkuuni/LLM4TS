package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Description");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Description");
        
        options.addOption(option);
        
        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Description");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Description");
        
        options.addOption(option);
        
        assertTrue(options.hasLongOption("option1"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Description");
        
        options.addOption(option);
        
        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
    }

    // Add more test cases for complete coverage

}