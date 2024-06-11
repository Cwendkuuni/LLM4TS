package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "opt1", false, "Help Option 1");
        Option option2 = new Option("o2", "opt2", true, "Help Option 2");
        Option option3 = new Option("o3", "opt3", false, "Help Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(3, group.getOptions().size());
        assertEquals(option1, group.getOptions().toArray()[0]);
        assertEquals(option2, group.getOptions().toArray()[1]);
        assertEquals(option3, group.getOptions().toArray()[2]);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "opt1", false, "Help Option 1");
        Option option2 = new Option("o2", "opt2", true, "Help Option 2");
        Option option3 = new Option("o3", "opt3", false, "Help Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        List<String> names = new ArrayList<>(group.getNames());

        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
        assertTrue(names.contains("o3"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "opt1", false, "Help Option 1");
        Option option2 = new Option("o2", "opt2", true, "Help Option 2");
        Option option3 = new Option("o3", "opt3", false, "Help Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        List<Option> options = new ArrayList<>(group.getOptions());

        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
        assertEquals(option3, options.get(2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "opt1", false, "Help Option 1");
        Option option2 = new Option("o2", "opt2", true, "Help Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals(option1, group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));

        group.setSelected(option2);
        assertEquals(option2, group.getSelected());
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "opt1", false, "Help Option 1");
        Option option2 = new Option("o2", "opt2", true, "Help Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals(option1, group.getSelected());

        group.setSelected(option2);
        assertEquals(option2, group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());

        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "opt1", false, "Help Option 1");
        Option option2 = new Option("o2", "opt2", true, "Help Option 2");
        Option option3 = new Option("o3", "opt3", false, "Help Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        String expected = "[-o1 Help Option 1, --opt2 Help Option 2, --opt3 Help Option 3]";
        assertEquals(expected, group.toString());
    }
}