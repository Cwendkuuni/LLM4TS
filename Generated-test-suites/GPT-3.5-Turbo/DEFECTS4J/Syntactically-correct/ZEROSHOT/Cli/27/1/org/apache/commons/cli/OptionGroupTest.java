import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        OptionGroup result = group.addOption(option1);
        assertSame(group, result);
        
        Collection names = group.getNames();
        assertEquals(1, names.size());
        assertTrue(names.contains("a"));
        
        Collection options = group.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option1));
        
        group.addOption(option2);
        names = group.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("b"));
        
        options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Collection names = group.getNames();
        
        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Collection options = group.getOptions();
        
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertNull(group.getSelected());
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("b", group.getSelected());
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertNull(group.getSelected());
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("b", group.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        
        assertFalse(group.isRequired());
        
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String result = group.toString();
        assertEquals("[ -a option1, -b option2 ]", result);
    }
}