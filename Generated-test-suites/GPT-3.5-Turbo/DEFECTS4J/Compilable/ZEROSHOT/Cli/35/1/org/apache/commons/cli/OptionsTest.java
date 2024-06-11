package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Collection;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        Collection<Option> allOptions = options.getOptions();
        assertTrue(allOptions.contains(option1));
        assertTrue(allOptions.contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        Option retrievedOption1 = options.getOption("o");
        Option retrievedOption2 = options.getOption("p");
        
        assertEquals(option1, retrievedOption1);
        assertEquals(option2, retrievedOption2);
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("p"));
        assertFalse(options.hasOption("q"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("option1"));
        assertTrue(options.hasLongOption("option2"));
        assertFalse(options.hasLongOption("option3"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("o"));
        assertTrue(options.hasShortOption("p"));
        assertFalse(options.hasShortOption("q"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        OptionGroup retrievedGroup1 = options.getOptionGroup(option1);
        OptionGroup retrievedGroup2 = options.getOptionGroup(option2);
        
        assertEquals(group, retrievedGroup1);
        assertEquals(group, retrievedGroup2);
    }

}