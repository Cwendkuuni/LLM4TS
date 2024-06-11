package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import java.util.Collection;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        options.addOption(option1);
        
        assertTrue(options.getOptions().contains(option1));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        options.addOption(option1);
        
        assertEquals(option1, options.getOption("a"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        List<String> matchingOptions = options.getMatchingOptions("opt");
        
        assertEquals(2, matchingOptions.size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        options.addOption(option1);
        
        assertTrue(options.hasOption("a"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        options.addOption(option1);
        
        assertTrue(options.hasLongOption("option1"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        options.addOption(option1);
        
        assertTrue(options.hasShortOption("a"));
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
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        String expected = "[ Options: [ short {a=Option [ short a, long option1, hasArg false, description Option 1 ]} ] [ long {option1=Option [ short a, long option1, hasArg false, description Option 1 ], option2=Option [ short b, long option2, hasArg false, description Option 2 ]} ]";
        assertEquals(expected, options.toString());
    }
}