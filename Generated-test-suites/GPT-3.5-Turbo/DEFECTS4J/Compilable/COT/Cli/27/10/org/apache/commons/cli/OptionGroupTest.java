package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        group.addOption(option);
        
        assertEquals(1, group.getOptions().size());
        assertTrue(group.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("r"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("r", group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedMultipleTimes() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        group.addOption(option1);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(null);
        assertNull(group.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[--add Add option, --remove Remove option]";
        assertEquals(expected, group.toString());
    }
}