import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OptionGroupTest {
    
    // create an instance of OptionGroup for testing
    OptionGroup optionGroup = new OptionGroup();
    
    // create a test Option
    Option option = new Option("t", "test", false, "Test Option");
    
    // create options for testing
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
    Option option3 = new Option("c", "option3", false, "Option 3");
    
    @Test
    public void testAddOption() {
        OptionGroup result = optionGroup.addOption(option);
        assertNotNull(result);
        assertEquals(1, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        Collection names = optionGroup.getNames();
        assertNotNull(names);
        assertEquals(3, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertTrue(names.contains("c"));
    }
    
    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        Collection options = optionGroup.getOptions();
        assertNotNull(options);
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }
    
    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
        
        try {
            optionGroup.setSelected(option2);
            assertEquals("b", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
        
        try {
            optionGroup.setSelected(option3);
            assertEquals("c", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
        
        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedException() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());
        
        optionGroup.addOption(option1);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        String expected = "[-a Option 1, -b Option 2, -c Option 3]";
        assertEquals(expected, optionGroup.toString());
    }
}