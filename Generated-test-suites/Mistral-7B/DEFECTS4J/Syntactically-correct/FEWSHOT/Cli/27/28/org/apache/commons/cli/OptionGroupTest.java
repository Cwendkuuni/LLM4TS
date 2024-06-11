package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getNames().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
        assertTrue(group.getNames().contains("o1"));
        assertTrue(group.getNames().contains("o2"));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals("o1", group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(option1);
        });
    }

    @Test
    public void testSetSelectedNewOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        group.addOption(option1);

        group.setSelected(option2);

        assertNull(group.getSelected());

        assertThrows(IllegalArgumentException.class, () -> {
            group.setSelected(option2);
        });
    }

    @Test
    public void testGetSelectedNull() {
        OptionGroup group = new OptionGroup();

        assertNull(group.getSelected());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");
        Option option3 = new Option("o3", "option three");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        String expected = "[-o1 option one, --o2 option two, --o3 option three]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringNoOptions() {
        OptionGroup group = new OptionGroup();

        String expected = "[ ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringSingleOption() {
        Option option1 = new Option("o1", "option one");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        String expected = "-o1 option one";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringLongOptions() {
        Option option1 = new Option("o1", "option one", true, "longOption1");
        Option option2 = new Option("o2", "option two", true, "longOption2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--longOption1 option one, --longOption2 option two]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithSelected() {
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        String expected = "[-- o1 option one, - o2]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithSelectedLongOption() {
        Option option1 = new Option("o1", "option one", true, "longOption1");
        Option option2 = new Option("o2", "option two", true, "longOption2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        String expected = "[--longOption1 option one, --longOption2]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithMultipleSelected() {
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);

        String expected = "[-- o1 option one, -- o2 option two]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithEmptySelected() {
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(null);

        String expected = "[ ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithEmptyOptions() {
        OptionGroup group = new OptionGroup();

        String expected = "[ ]";

        assertEquals(expected, group.toString());
    }
}