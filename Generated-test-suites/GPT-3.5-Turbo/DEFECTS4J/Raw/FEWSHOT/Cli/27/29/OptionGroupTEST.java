package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

public class OptionGroupTest {
      
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "firstName", false, "First Name");
        Option option2 = new Option("l", "lastName", false, "Last Name");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Collection options = group.getOptions();
        
        assertNotNull(options);
        assertFalse(options.isEmpty());
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "firstName", false, "First Name");
        Option option2 = new Option("l", "lastName", false, "Last Name");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Collection names = group.getNames();
        
        assertNotNull(names);
        assertFalse(names.isEmpty());
        assertEquals(2, names.size());
        assertTrue(names.contains("f"));
        assertTrue(names.contains("l"));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "firstName", false, "First Name");
        Option option2 = new Option("l", "lastName", false, "Last Name");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("f", group.getSelected());
        
        group.setSelected(null);
        assertNull(group.getSelected());
        
        group.setSelected(option2);
        assertEquals("l", group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "firstName", false, "First Name");
        Option option2 = new Option("l", "lastName", false, "Last Name");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "firstName", false, "First Name");
        Option option2 = new Option("l", "lastName", false, "Last Name");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-f First Name, -l Last Name]";
        assertEquals(expected, group.toString());
    }
}