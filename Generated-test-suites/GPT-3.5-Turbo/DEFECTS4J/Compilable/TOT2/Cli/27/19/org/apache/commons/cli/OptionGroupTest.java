package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        assertTrue(group.getNames().contains("t"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        try {
            group.setSelected(option1);
            assertEquals("t", group.getSelected());
            group.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
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
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[ -t Test option 1, -u Test option 2 ]", group.toString());
    }
}