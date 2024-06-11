import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        
        assertEquals("b", group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        
        assertEquals("a", group.getSelected());
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
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "["
                + "-a Option 1, "
                + "-b Option 2"
                + "]";
        
        assertEquals(expected, group.toString());
    }
}