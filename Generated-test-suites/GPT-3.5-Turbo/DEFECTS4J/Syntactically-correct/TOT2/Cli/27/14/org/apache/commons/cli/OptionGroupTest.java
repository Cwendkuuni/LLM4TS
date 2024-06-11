import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getNames().contains("t"));
        assertTrue(optionGroup.getOptions().contains(option));
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        assertEquals("t", optionGroup.getSelected());
        
        assertThrows(AlreadySelectedException.class, () -> {
            optionGroup.setSelected(option2);
        });
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expectedString = "[ -t Test option 1, -u Test option 2 ]";
        
        assertEquals(expectedString, optionGroup.toString());
    }
}