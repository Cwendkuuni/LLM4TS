package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.Iterator;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Test option");
        group.addOption(option);
        
        Collection options = group.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Test option 1");
        Option option2 = new Option("o2", "option2", false, "Test option 2");

        group.addOption(option1);
        group.addOption(option2);

        Collection names = group.getNames();
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Test option 1");
        Option option2 = new Option("o2", "option2", false, "Test option 2");

        group.addOption(option1);
        group.addOption(option2);

        try {
            group.setSelected(option1);
            assertEquals("o1", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw exception for first selection");
        }

        try {
            group.setSelected(option2);
            assertEquals("o2", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw exception for different selection");
        }

        try {
            group.setSelected(option1);
            fail("Should throw AlreadySelectedException for reselecting the same option");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        assertFalse(group.isRequired());
        
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Test option 1");
        Option option2 = new Option("o2", "option2", false, "Test option 2");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-o1 Test option 1, -o2 Test option 2]";
        assertEquals(expected, group.toString());
    }
}