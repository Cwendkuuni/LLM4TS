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
        Option option = new Option("a", "desc");
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        try {
            optionGroup.setSelected(option2);
            fail("AlreadySelectedException was not thrown");
        } catch (AlreadySelectedException e) {
            // expected
        }
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[ -a desc1, -b desc2 ]";
        assertEquals(expected, optionGroup.toString());
    }
}