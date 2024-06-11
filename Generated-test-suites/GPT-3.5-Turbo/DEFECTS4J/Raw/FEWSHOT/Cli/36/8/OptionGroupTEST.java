package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("r", "recursive", false, "recursive option");
        optionGroup.addOption(option3);
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testGetNames() {
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(new Option("f", "file", false, "file option")));
        assertTrue(optionGroup.getOptions().contains(new Option("d", "directory", false, "directory option")));
    }

    @Test
    public void testSetSelected() {
        assertNull(optionGroup.getSelected());
        Option option1 = new Option("f", "file", false, "file option");
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() {
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_nullOption() {
        Option option1 = new Option("f", "file", false, "file option");
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());
        Option option1 = new Option("f", "file", false, "file option");
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        assertEquals("[ -f file option, --d directory option ]", optionGroup.toString());
    }
}