import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            
            assertEquals(option1.getOpt(), optionGroup.getSelected());
            
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelected_nullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        
        optionGroup.addOption(option);
        
        try {
            optionGroup.setSelected(null);
            
            assertNull(optionGroup.getSelected());
            
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            
            assertEquals(option1.getOpt(), optionGroup.getSelected());
            
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "["
                        + "-a option a, "
                        + "-b option b"
                        + "]";
        
        assertEquals(expected, optionGroup.toString());
    }
}