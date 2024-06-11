
import org.apache.commons.cli.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        OptionGroup updatedGroup = optionGroup.addOption(option1);
        assertTrue(updatedGroup.getOptions().contains(option1));
        assertFalse(updatedGroup.getOptions().contains(option2));
        
        updatedGroup.addOption(option2);
        assertTrue(updatedGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1).addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertFalse(optionGroup.getNames().contains("c"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1).addOption(option2);
        
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1).addOption(option2);
        
        // Select option1
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        // Select option2
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
        
        // Select option1 again
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        // Select null
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1).addOption(option2);
        
        // Select option1
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        // Select option2 (throws AlreadySelectedException)
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        assertNull(optionGroup.getSelected());
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1).addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        option1.setDescription("Description for option1");
        Option option2 = new Option("b", "option2");
        option2.setDescription("Description for option2");
        
        optionGroup.addOption(option1).addOption(option2);
        
        String expectedString = "[ -a Description for option1, -b Description for option2 ]";
        assertEquals(expectedString, optionGroup.toString());
    }
}
