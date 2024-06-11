package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Options options = new Options();
        options.addOptionGroup(group);
        
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        
        assertTrue(optionGroups.contains(group));
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        
        Collection<Option> optionCollection = options.getOptions();
        
        assertTrue(optionCollection.contains(option));
    }
    
    @Test
    public void testAddOptionWithHasArg() {
        Option option = new Option("o", "option", true, "Option with argument");
        Options options = new Options();
        options.addOption(option);
        
        Collection<Option> optionCollection = options.getOptions();
        
        assertTrue(optionCollection.contains(option));
    }
    
    @Test
    public void testAddOptionWithShortNameOnly() {
        Options options = new Options();
        options.addOption("o", "Option");
        
        Collection<Option> optionCollection = options.getOptions();
        
        assertFalse(optionCollection.isEmpty());
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        
        Option retrievedOption = options.getOption("o");
        
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        List<String> matchingOptions = options.getMatchingOptions("o");
        
        assertTrue(matchingOptions.contains("option1"));
        assertTrue(matchingOptions.contains("option2"));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasOption("o"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasLongOption("option"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        
        assertTrue(options.hasShortOption("o"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Option option = new Option("o", "option", false, "Option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);
        
        OptionGroup retrievedGroup = options.getOptionGroup(option);
        
        assertEquals(group, retrievedGroup);
    }
    
    @Test
    public void testToString() {
        Option option = new Option("o", "option", false, "Option");
        Options options = new Options();
        options.addOption(option);
        
        String expectedString = "[ Options: [ short {o=Option} ] [ long {option=Option} ]";
        String actualString = options.toString();
        
        assertEquals(expectedString, actualString);
    }
}