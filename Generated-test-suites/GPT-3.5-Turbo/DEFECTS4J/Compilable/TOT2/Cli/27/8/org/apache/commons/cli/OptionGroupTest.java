package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "testing option");
        group.addOption(option);
        
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "testing option 1");
        Option option2 = new Option("u", "test2", false, "testing option 2");
        
        group.addOption(option1);
        group.addOption(option2);

        assertTrue(group.getNames().contains("t"));
        assertTrue(group.getNames().contains("u"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "testing option 1");
        Option option2 = new Option("u", "test2", false, "testing option 2");
        
        group.addOption(option1);
        group.addOption(option2);

        try {
            group.setSelected(option1);
            assertEquals("t", group.getSelected());
        } catch (AlreadySelectedException ex) {
            fail("Should not throw exception");
        }

        try {
            group.setSelected(option2);
            assertEquals("u", group.getSelected());
        } catch (AlreadySelectedException ex) {
            fail("Should not throw exception");
        }

        try {
            group.setSelected(option1);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException ex) {
            // Expected
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
        Option option1 = new Option("t", "test1", false, "testing option 1");
        Option option2 = new Option("u", "test2", false, "testing option 2");
        
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-t testing option 1, -u testing option 2]";
        assertEquals(expected, group.toString());
    }
}