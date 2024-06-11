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
        Option option = new Option("t", "test", false, "Test option");
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("t", "test", false, "Test option");
        Option option2 = new Option("e", "example", false, "Example option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("t"));
        assertTrue(optionGroup.getNames().contains("e"));
    }
    
    @Test
    public void testSetSelected_noOptionSelected() {
        optionGroup.setSelected(null);
        
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testSetSelected_sameOptionSelected() {
        Option option = new Option("t", "test", false, "Test option");
        
        optionGroup.setSelected(option);
        
        assertEquals("t", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_differentOptionSelected() {
        Option option1 = new Option("t", "test", false, "Test option 1");
        Option option2 = new Option("e", "example", false, "Example option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("t", "test", false, "Test option");
        Option option2 = new Option("e", "example", false, "Example option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[ -t Test option, -e Example option]";
        
        assertEquals(expected, optionGroup.toString());
    }
}