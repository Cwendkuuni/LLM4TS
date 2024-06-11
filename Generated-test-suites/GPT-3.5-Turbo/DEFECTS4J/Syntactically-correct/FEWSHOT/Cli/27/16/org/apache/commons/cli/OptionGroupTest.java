
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("b", group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testSetSelected_null() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(null);
        assertNull(group.getSelected());
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        assertFalse(group.isRequired());
        
        group.setRequired(true);
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[ -a Option 1, -b Option 2 ]";
        assertEquals(expected, group.toString());
    }
}
