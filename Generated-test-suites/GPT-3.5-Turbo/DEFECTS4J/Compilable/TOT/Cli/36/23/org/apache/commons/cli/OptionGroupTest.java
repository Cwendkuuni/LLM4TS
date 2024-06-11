package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    // Test cases for addOption method
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("r"));
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    // Test cases for getNames method
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("r"));
        assertEquals(2, group.getNames().size());
    }
    
    // Test cases for getOptions method
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
        assertEquals(2, group.getOptions().size());
    }
    
    // Test cases for setSelected method
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("r", group.getSelected());
        
        // Test AlreadySelectedException when same option is selected again
        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException to be thrown");
        } catch (AlreadySelectedException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testSetSelected_ResetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        
        group.addOption(option1);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(null);
        assertNull(group.getSelected());
    }
    
    // Test cases for getSelected method
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("r", group.getSelected());
    }
    
    // Test cases for setRequired method
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
    
    // Test cases for isRequired method
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
    
    // Test cases for toString method
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[" + "-a add option" + ", " + "-r remove option" + "]";
        assertEquals(expected, group.toString());
    }
}