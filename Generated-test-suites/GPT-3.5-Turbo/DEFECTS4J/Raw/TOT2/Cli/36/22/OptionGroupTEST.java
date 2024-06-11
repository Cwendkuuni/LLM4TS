import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        
        group.addOption(option);
        
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getOptions().contains(option));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("s", "select", false, "Select option");
        
        group.addOption(option);
        group.setSelected(option);
        
        assertEquals("s", group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("s1", "select1", false, "Select option 1");
        Option option2 = new Option("s2", "select2", false, "Select option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t1", "test1", false, "Test option 1");
        Option option2 = new Option("t2", "test2", false, "Test option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[ -t1 Test option 1, -t2 Test option 2]";
        
        assertEquals(expected, group.toString());
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        
        assertTrue(group.isRequired());
    }
}