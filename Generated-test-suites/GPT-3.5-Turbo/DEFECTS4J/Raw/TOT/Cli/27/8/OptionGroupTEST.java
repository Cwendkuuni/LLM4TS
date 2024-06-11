import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    // Test addOption() method
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        
        optionGroup.addOption(option1);
        
        assertTrue(optionGroup.getOptions().contains(option1));
    }
    
    // Test getNames() method
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        Option option2 = new Option("b", "beta", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }
    
    // Test getOptions() method
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        Option option2 = new Option("b", "beta", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    // Test setSelected() method
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        Option option2 = new Option("b", "beta", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
            
            optionGroup.setSelected(option2);
            assertEquals("b", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Test setSelected() method when an option from the same group is already selected
    @Test
    public void testSetSelected_AlreadySelectedException() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        Option option2 = new Option("b", "beta", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        try {
            optionGroup.setSelected(option1);
            fail("Expected AlreadySelectedException was not thrown.");
        } catch (AlreadySelectedException e) {
            assertEquals("The option 'a' has already been selected in this group.", e.getMessage());
        }
    }
    
    // Test getSelected() method
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        Option option2 = new Option("b", "beta", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }
    
    // Test setRequired() method
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        Option option2 = new Option("b", "beta", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[-a Option 1, -b Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}