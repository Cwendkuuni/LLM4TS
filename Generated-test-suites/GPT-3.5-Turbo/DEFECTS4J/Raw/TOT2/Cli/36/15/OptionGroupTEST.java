import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        
        group.addOption(option1);
        
        assertTrue(group.getOptions().contains(option1));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getNames().contains("o1"));
        assertTrue(group.getNames().contains("o2"));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        
        group.addOption(option1);
        
        try {
            group.setSelected(option1);
            assertEquals("o1", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals("[ -o1 Option 1, -o2 Option 2]", group.toString());
    }
}