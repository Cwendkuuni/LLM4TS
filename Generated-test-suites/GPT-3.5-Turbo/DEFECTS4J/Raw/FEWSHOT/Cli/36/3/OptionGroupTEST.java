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
        option1 = new Option("a", "option 1");
        option2 = new Option("b", "option 2");
        option3 = new Option("c", "option 3");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }
    
    @Test
    public void testAddOption() {
        Option option4 = new Option("d", "option 4");
        optionGroup.addOption(option4);
        
        assertTrue(optionGroup.getOptions().contains(option4));
    }
    
    @Test
    public void testGetNames() {
        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("c"));
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }
    
    @Test
    public void testSetSelected() {
        optionGroup.setSelected(option1);
        
        assertEquals("a", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelectedWithNullOption() {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        String expected = "[a option 1, b option 2, c option 3]";
        assertEquals(expected, optionGroup.toString());
    }
}