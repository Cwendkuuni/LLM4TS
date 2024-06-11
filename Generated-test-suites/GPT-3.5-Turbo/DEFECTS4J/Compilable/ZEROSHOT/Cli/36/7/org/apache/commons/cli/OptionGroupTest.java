package org.apache.commons.cli;
import org.junit.*;
import java.util.Collection;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "test option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        Collection<Option> options = group.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        Option option = new Option("o", "option", false, "test option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        Collection<String> names = group.getNames();
        assertTrue(names.contains(option.getKey()));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "option", false, "test option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        Collection<Option> options = group.getOptions();
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
        
        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected exception
        }
        
        assertNull(group.getSelected());
        
        try {
            group.setSelected(null);
            assertNull(group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());
        
        try {
            group.setSelected(option1);
            assertEquals(option1.getKey(), group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
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
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--option1 test option 1, --option2 test option 2]";
        assertEquals(expected, group.toString());
    }

}