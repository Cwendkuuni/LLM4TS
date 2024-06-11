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
        Option option = new Option("a", "apple", false, "Option A");
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertEquals(1, optionGroup.getOptions().size());
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "apple", false, "Option A");
        Option option2 = new Option("b", "banana", false, "Option B");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
            
            optionGroup.setSelected(option2);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
        }
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
        Option option1 = new Option("a", "apple", false, "Option A");
        Option option2 = new Option("b", "banana", false, "Option B");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expectedOutput = "[ -a Option A, -b Option B]";
        assertEquals(expectedOutput, optionGroup.toString());
    }
}