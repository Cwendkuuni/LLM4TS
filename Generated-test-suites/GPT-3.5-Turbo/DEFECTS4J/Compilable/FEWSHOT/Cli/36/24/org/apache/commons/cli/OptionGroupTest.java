package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "option 1");
        optionGroup.addOption(option);
        
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        optionGroup.setSelected(option1);
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
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
        Option option1 = new Option("a", "option 1");
        option1.setDescription("Option 1 description");
        Option option2 = new Option("b", "option 2");
        option2.setDescription("Option 2 description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[-a Option 1 description, -b Option 2 description]";
        assertEquals(expected, optionGroup.toString());
    }
}