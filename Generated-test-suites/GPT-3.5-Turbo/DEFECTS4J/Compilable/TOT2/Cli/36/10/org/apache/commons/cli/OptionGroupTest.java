package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", false, "input file");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", false, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("f"));
        assertTrue(group.getNames().contains("d"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("f", "file", false, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
            assertEquals("f", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception not expected");
        }
        
        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", false, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[ -f input file, -d input directory]";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
}