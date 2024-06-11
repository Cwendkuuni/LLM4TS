package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "output file", true, "f");
        Option option1 = new Option("h", "help", false, "h");

        group.addOption(option);
        group.addOption(option1);

        assertEquals(2, group.getOptions().size());
        assertEquals("f", group.getSelected());
        assertNull(group.getSelectedOption());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "output file", true, "f");
        Option option1 = new Option("h", "help", false, "h");

        group.addOption(option);
        group.addOption(option1);

        Collection<String> names = group.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("f"));
        assertTrue(names.contains("h"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "output file", true, "f");
        Option option1 = new Option("h", "help", false, "h");

        group.addOption(option);
        group.addOption(option1);

        Collection<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option));
        assertTrue(options.contains(option1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "output file", true, "f");
        Option option1 = new Option("h", "help", false, "h");

        group.addOption(option);
        group.addOption(option1);

        assertNull(group.getSelected());

        group.setSelected(option);
        assertEquals("f", group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "output file", true, "f");
        Option option1 = new Option("h", "help", false, "h");

        group.addOption(option);
        group.addOption(option1);

        assertNull(group.getSelected());

        group.setSelected(option);
        assertEquals("f", group.getSelected());
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
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "output file", true, "f");
        Option option1 = new Option("h", "help", false, "h");

        group.addOption(option);
        group.addOption(option1);

        group.setSelected(option);

        String expected = "[-f output file, --help help]";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testSelectedOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "output file", true, "f");
        Option option1 = new Option("h", "help", false, "h");

        group.addOption(option);
        group.addOption(option1);

        group.setSelected(option);

        Option selectedOption = group.getSelectedOption();
        assertNotNull(selectedOption);
        assertEquals(option, selectedOption);
    }
}