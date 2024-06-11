package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "option1");
        option2 = new Option("b", "option2");
        option3 = new Option("c", "option3");
    }
    
    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals(3, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("c"));
    }
    
    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        optionGroup.setSelected(option1);
        
        assertEquals("a", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelectedWithNullOption() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        
        optionGroup.setSelected(null);
        
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        optionGroup.setSelected(option2);
        
        assertEquals("b", optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        optionGroup.setRequired(false);
        
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals("[-a option1, -b option2, -c option3]", optionGroup.toString());
    }
}