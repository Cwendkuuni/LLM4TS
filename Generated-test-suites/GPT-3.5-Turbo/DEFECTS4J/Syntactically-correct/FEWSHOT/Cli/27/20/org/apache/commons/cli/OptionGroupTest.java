package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "test option");
        OptionGroup result = optionGroup.addOption(option);
        assertTrue(result.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
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
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals("[--a test option 1, --b test option 2]", optionGroup.toString());
    }
}