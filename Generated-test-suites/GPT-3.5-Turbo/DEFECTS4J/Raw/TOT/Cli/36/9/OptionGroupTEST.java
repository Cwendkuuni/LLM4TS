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
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        
        assertEquals(1, optionGroup.getOptions().size());
        
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option2);
        
        assertNotNull(optionGroup.getNames());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("r"));
        assertEquals(2, optionGroup.getNames().size());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option2);
        
        assertNotNull(optionGroup.getOptions());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        // Trying to select the same option again should not throw an exception
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        try {
            optionGroup.setSelected(option2);
            fail("AlreadySelectedException not thrown");
        } catch (AlreadySelectedException e) {
            assertEquals(optionGroup, e.getOptionGroup());
            assertEquals(option2, e.getOption());
        }
    }
    
    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option2);
        
        assertNull(optionGroup.getSelected());
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
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
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option2);
        
        assertEquals("[-a Add option, -r Remove option]", optionGroup.toString());
    }
}