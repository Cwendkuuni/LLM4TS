import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        group.addOption(option1);
        
        assertTrue(group.getOptions().contains(option1));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        group.addOption(option1);
        
        assertTrue(group.getNames().contains("a"));
    }
    
    @Test
    public void testSetSelectedSuccess() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        
        try {
            group.setSelected(option1);
            assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }
    
    @Test
    public void testSetSelectedFailure() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");
        
        group.setSelected(option1);
        
        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
        }
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
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-a apple, -b banana]";
        
        assertEquals(expected, group.toString());
    }
}