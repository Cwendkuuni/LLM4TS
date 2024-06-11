import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);
        
        assertNotNull(optionGroup.getOptions());
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option, optionGroup.getOptions().iterator().next());
        
        option = new Option("b", "bar", true, "option bar");
        optionGroup.addOption(option);
        
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);
        
        assertEquals(1, optionGroup.getNames().size());
        assertEquals("f", optionGroup.getNames().iterator().next());
        
        option = new Option("b", "bar", true, "option bar");
        optionGroup.addOption(option);
        
        assertEquals(2, optionGroup.getNames().size());
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);
        
        assertNotNull(optionGroup.getOptions());
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option, optionGroup.getOptions().iterator().next());
        
        option = new Option("b", "bar", true, "option bar");
        optionGroup.addOption(option);
        
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);
        
        option = new Option("b", "bar", true, "option bar");
        optionGroup.addOption(option);
        
        optionGroup.setSelected(option);
        
        assertEquals("b", optionGroup.getSelected());
        
        option = new Option("a", "baz", false, "option baz");
        
        try {
            optionGroup.setSelected(option);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertEquals(optionGroup, e.getOptionGroup());
            assertEquals(option, e.getOption());
        }
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);
        
        assertNull(optionGroup.getSelected());
        
        option = new Option("b", "bar", true, "option bar");
        optionGroup.addOption(option);
        
        assertNull(optionGroup.getSelected());
        
        optionGroup.setSelected(option);
        
        assertEquals("b", optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);
        
        assertEquals("[ -f option foo]", optionGroup.toString());
        
        option = new Option("b", "bar", true, "option bar");
        optionGroup.addOption(option);
        
        assertEquals("[ -f option foo, -b option bar]", optionGroup.toString());
    }
}