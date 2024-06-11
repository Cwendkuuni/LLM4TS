package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        
        assertEquals(group.getOptions().size(), 0);
        
        group.addOption(option1);
        assertEquals(group.getOptions().size(), 1);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        group.addOption(option1);
        
        assertTrue(group.getNames().contains("a"));
        assertFalse(group.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        group.addOption(option1);
        
        assertTrue(group.getOptions().contains(option1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        
        try {
            group.setSelected(option1);
            assertEquals(group.getSelected(), "a");
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException.");
        }
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
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        group.addOption(option1).addOption(option2);
        
        String expected = "[a apple, b banana]";
        assertEquals(group.toString(), expected);
    }
}