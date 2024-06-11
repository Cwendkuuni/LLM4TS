package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        Option option3 = new Option("d", "delete", false, "Delete file");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("c"));
        assertTrue(options.hasOption("d"));
    }
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("c"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        option1.setRequired(true);
        option2.setRequired(true);
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("a"));
        assertTrue(options.getRequiredOptions().contains("c"));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("c"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("abc", "alphabet", true, "Letters in alphabet");
        option1.setRequired(true);
        option2.setRequired(true);
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals("a", options.getMatchingOptions("a").get(0));
        assertEquals(1, options.getMatchingOptions("abc").size());
        assertEquals("abc", options.getMatchingOptions("abc").get(0));
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("c"));
        assertFalse(options.hasOption("d"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("append"));
        assertTrue(options.hasLongOption("create"));
        assertFalse(options.hasLongOption("delete"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("c"));
        assertFalse(options.hasShortOption("d"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "append", false, "Append to file");
        Option option2 = new Option("c", "create", true, "Create new file");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals("[ Options: [ short {a=Option(\"a\",\"append\",false,\"Append to file\")} ] [ long {append=Option(\"a\",\"append\",false,\"Append to file\"), create=Option(\"c\",\"create\",true,\"Create new file\")} ]", options.toString());
    }

}