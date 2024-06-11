package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "foo", false, "foo option");

        OptionGroup result = group.addOption(option);

        assertEquals(group, result);
        assertTrue(group.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        Option option3 = new Option("t", "test", false, "test option");
        
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        assertEquals(3, group.getNames().size());
        assertTrue(group.getNames().contains("f"));
        assertTrue(group.getNames().contains("b"));
        assertTrue(group.getNames().contains("t"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        Option option3 = new Option("t", "test", false, "test option");
        
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        assertEquals(3, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
        assertTrue(group.getOptions().contains(option3));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertNull(group.getSelected());
        
        try {
            group.setSelected(option1);
            assertEquals("f", group.getSelected());
            
            group.setSelected(option2);
            assertEquals("b", group.getSelected());
            
            group.setSelected(null);
            assertNull(group.getSelected());
            
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertNull(group.getSelected());
        
        try {
            group.setSelected(option1);
            assertEquals("f", group.getSelected());
            
            group.setSelected(option2);
            assertEquals("b", group.getSelected());
            
            group.setSelected(null);
            assertNull(group.getSelected());
            
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        
        assertFalse(group.isRequired());
        
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        Option option3 = new Option("t", "test", false, "test option");
        
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        String expected = "[ -f foo option, -b bar option, -t test option]";
        assertEquals(expected, group.toString());
    }
}