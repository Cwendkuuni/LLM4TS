import org.junit.*;
import static org.junit.Assert.*;

import org.apache.commons.cli.*;

public class OptionGroupTest {

    //Test addOption method
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        
        //Test with a new option
        Option option1 = new Option("a", "add", false, "Add option");
        OptionGroup result1 = group.addOption(option1);
        assertEquals(result1.getOptions().size(), 1);
        
        //Test with an existing option
        Option option2 = new Option("b", "back", false, "Back option");
        OptionGroup result2 = group.addOption(option2);
        OptionGroup result3 = result2.addOption(option2);
        assertEquals(result3.getOptions().size(), 2);
    }

    //Test getNames method
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        
        //Test with empty option group
        assertTrue(group.getNames().isEmpty());
        
        //Test with one option
        Option option1 = new Option("a", "add", false, "Add option");
        group.addOption(option1);
        assertEquals(group.getNames().size(), 1);
        
        //Test with multiple options
        Option option2 = new Option("b", "back", false, "Back option");
        group.addOption(option2);
        Option option3 = new Option("c", "cancel", false, "Cancel option");
        group.addOption(option3);
        assertEquals(group.getNames().size(), 3);
    }

    //Test getOptions method
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        
        //Test with empty option group
        assertTrue(group.getOptions().isEmpty());
        
        //Test with one option
        Option option1 = new Option("a", "add", false, "Add option");
        group.addOption(option1);
        assertEquals(group.getOptions().size(), 1);
        
        //Test with multiple options
        Option option2 = new Option("b", "back", false, "Back option");
        group.addOption(option2);
        Option option3 = new Option("c", "cancel", false, "Cancel option");
        group.addOption(option3);
        assertEquals(group.getOptions().size(), 3);
    }

    //Test setSelected method
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        
        //Test with null option
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "back", false, "Back option");
        Option option3 = new Option("c", "cancel", false, "Cancel option");
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(null);
        assertNull(group.getSelected());
        
        //Test with selected option
        group.setSelected(option2);
        assertEquals(group.getSelected(), "b");
        
        //Test with already selected option
        try {
            group.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertNotNull(e);
        }
    }

    //Test getSelected method
    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        
        //Test with null selected option
        assertNull(group.getSelected());
        
        //Test with selected option
        Option option1 = new Option("a", "add", false, "Add option");
        group.addOption(option1);
        group.setSelected(option1);
        assertEquals(group.getSelected(), "a");
    }

    //Test setRequired and isRequired methods
    @Test
    public void testSetRequiredAndIsRequired() {
        OptionGroup group = new OptionGroup();
        
        //Test with false required value
        group.setRequired(false);
        assertFalse(group.isRequired());
        
        //Test with true required value
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    //Test toString method
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        
        //Test with empty option group
        assertEquals(group.toString(), "[]");
        
        //Test with one option
        Option option1 = new Option("a", "add", false, "Add option");
        group.addOption(option1);
        assertEquals(group.toString(), "[-a Add option]");
        
        //Test with multiple options
        Option option2 = new Option("b", "back", false, "Back option");
        group.addOption(option2);
        Option option3 = new Option("c", "cancel", false, "Cancel option");
        group.addOption(option3);
        assertEquals(group.toString(), "[-a Add option, -b Back option, -c Cancel option]");
    }
}