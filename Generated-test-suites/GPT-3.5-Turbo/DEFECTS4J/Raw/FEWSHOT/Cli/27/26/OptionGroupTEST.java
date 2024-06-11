import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);
        
        assertEquals(1, group.getOptions().size());
        assertEquals(option, group.getOptions().iterator().next());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("o"));
        assertTrue(group.getNames().contains("p"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        assertNull(group.getSelected());
        
        group.setSelected(option1);
        assertEquals("o", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("p", group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testSetSelected_NullOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        group.addOption(option1);
        
        group.setSelected(option1);
        assertEquals("o", group.getSelected());
        
        group.setSelected(null);
        assertNull(group.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-o Option 1, -p Option 2]";
        assertEquals(expected, group.toString());
    }
}