package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    
    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        assertEquals("f", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_duplicateOption() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        assertEquals("f", optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("d", "directory", true, "directory option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[ -f file option, --directory directory option]";
        assertEquals(expected, optionGroup.toString());
    }
}