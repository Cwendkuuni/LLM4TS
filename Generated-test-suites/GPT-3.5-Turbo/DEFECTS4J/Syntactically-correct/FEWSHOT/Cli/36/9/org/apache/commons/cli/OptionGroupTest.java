
package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "file", true, "Specify a file");
        group.addOption(option);
        assertEquals(1, group.getOptions().size());
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify a file");
        Option option2 = new Option("d", "dir", true, "Specify a directory");
        group.addOption(option1);
        group.addOption(option2);
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("f"));
        assertTrue(group.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify a file");
        Option option2 = new Option("d", "dir", true, "Specify a directory");
        group.addOption(option1);
        group.addOption(option2);
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify a file");
        Option option2 = new Option("d", "dir", true, "Specify a directory");
        group.addOption(option1);
        group.addOption(option2);
        assertNull(group.getSelected());
        try {
            group.setSelected(option1);
            assertEquals("f", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
        try {
            group.setSelected(option2);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertEquals(group, e.getOptionGroup());
            assertEquals(option2, e.getOption());
        }
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify a file");
        Option option2 = new Option("d", "dir", true, "Specify a directory");
        group.addOption(option1);
        group.addOption(option2);
        assertNull(group.getSelected());
        try {
            group.setSelected(option1);
            assertEquals("f", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
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
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        assertFalse(group.isRequired());
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify a file");
        Option option2 = new Option("d", "dir", true, "Specify a directory");
        group.addOption(option1);
        group.addOption(option2);
        String expected = "[-f Specify a file, -d Specify a directory]";
        assertEquals(expected, group.toString());
    }
}
