import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        
        optionGroup.addOption(option1);
        assertEquals(1, optionGroup.getOptions().size());
        
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertFalse(optionGroup.getNames().contains("c"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option A");
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        Option option2 = new Option("b", "option B");
        
        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Pass
        }
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    // Add more test cases for other methods as needed

}