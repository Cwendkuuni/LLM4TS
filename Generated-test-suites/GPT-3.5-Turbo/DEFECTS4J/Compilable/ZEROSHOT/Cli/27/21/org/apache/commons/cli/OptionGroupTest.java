package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", true, "test option");
        group.addOption(option);
        
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", true, "test option 1");
        Option option2 = new Option("u", "test2", true, "test option 2");
        Option option3 = new Option("v", "test3", true, "test option 3");
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        assertTrue(group.getNames().contains("t"));
        assertTrue(group.getNames().contains("u"));
        assertTrue(group.getNames().contains("v"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", true, "test option 1");
        Option option2 = new Option("u", "test2", true, "test option 2");
        Option option3 = new Option("v", "test3", true, "test option 3");
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
        assertTrue(group.getOptions().contains(option3));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", true, "test option 1");
        Option option2 = new Option("u", "test2", true, "test option 2");
        Option option3 = new Option("v", "test3", true, "test option 3");
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        group.setSelected(option1);
        assertEquals("t", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("u", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDuplicate() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", true, "test option 1");
        Option option2 = new Option("u", "test2", true, "test option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", true, "test option 1");
        Option option2 = new Option("u", "test2", true, "test option 2");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("t", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("u", group.getSelected());
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
        Option option1 = new Option("t", "test1", true, "test option 1");
        Option option2 = new Option("u", "test2", true, "test option 2");
        Option option3 = new Option("v", "test3", true, "test option 3");
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        String expected = "[test1 - test option 1, test2 - test option 2, test3 - test option 3]";
        assertEquals(expected, group.toString());
    }
}