package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "optionA", false, "Option A");
        group.addOption(option);
        assertEquals(group.getOptions().size(), 1);
        assertTrue(group.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);
        assertEquals(group.getNames().size(), 2);
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);
        assertEquals(group.getOptions().size(), 2);
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        assertEquals(group.getSelected(), "a");
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "optionA", false, "Option A");
        group.addOption(option1);
        group.setSelected(option1);
        group.setSelected(null);
        assertNull(group.getSelected());
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
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);
        assertEquals(group.toString(), "[-a Option A, -b Option B]");
    }
}