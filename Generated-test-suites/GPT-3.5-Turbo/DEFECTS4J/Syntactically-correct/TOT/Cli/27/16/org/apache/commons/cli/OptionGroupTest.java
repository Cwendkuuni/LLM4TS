import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        
        // typical use case
        Option option1 = new Option("a", "option1", false, "option 1 description");
        Option option2 = new Option("b", "option2", false, "option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        
        // typical use case
        Option option1 = new Option("a", "option1", false, "option 1 description");
        Option option2 = new Option("b", "option2", false, "option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getNames().size());
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        
        // typical use case
        Option option1 = new Option("a", "option1", false, "option 1 description");
        Option option2 = new Option("b", "option2", false, "option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getOptions().size());
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option1", false, "option 1 description");
        Option option2 = new Option("b", "option2", false, "option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        
        // typical use case
        try {
            group.setSelected(option1);
            assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception occurred.");
        }
        
        // error scenario: already selected option
        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException was not thrown.");
        } catch (AlreadySelectedException e) {
            // expected exception
        }
        
        // typical use case: deselect option
        try {
            group.setSelected(null);
            assertNull(group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception occurred.");
        }
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option1", false, "option 1 description");
        Option option2 = new Option("b", "option2", false, "option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        
        // typical use case
        try {
            group.setSelected(option1);
            assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception occurred.");
        }
        
        // typical use case: no option selected
        assertNull(group.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        
        // typical use case: not required
        assertFalse(group.isRequired());
        
        // typical use case: required
        group.setRequired(true);
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        
        // typical use case
        Option option1 = new Option("a", "option1", false, "option 1 description");
        Option option2 = new Option("b", "option2", false, "option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-" + option1.getOpt() + " " + option1.getDescription() + ", -" + option2.getOpt() + " " + option2.getDescription() + "]";
        assertEquals(expected, group.toString());
    }
}