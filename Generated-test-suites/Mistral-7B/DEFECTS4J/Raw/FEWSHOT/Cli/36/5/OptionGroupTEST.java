package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        Option option1 = new Option("o1", "option1", true, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");
        Option option3 = new Option("o3", "option3", false, "o3-desc");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        Option[] options = group.getOptions().toArray(new Option[0]);

        assertEquals(3, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
        assertEquals(option3, options[2]);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");
        Option option3 = new Option("o3", "option3", false, "o3-desc");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        List<String> names = new ArrayList<>(group.getNames());

        assertEquals(3, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
        assertTrue(names.contains("o3"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");
        Option option3 = new Option("o3", "option3", false, "o3-desc");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        List<Option> options = new ArrayList<>(group.getOptions());

        assertEquals(3, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
        assertEquals(option3, options.get(2));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();

        assertNull(group.getSelected());
        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "o1-desc");

        group.setSelected(option1);
        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));
    }

    @Test
    public void testSetSelectedNewOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        group.setSelected(null);
        group.setSelected(option1);
        assertEquals("o1", group.getSelected());
        group.setSelected(option2);
        assertEquals("o2", group.getSelected());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup group = new OptionGroup();
        group.setRequired(false);
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
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");
        Option option3 = new Option("o3", "option3", false, "o3-desc");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        String expected = "[-o1, o1-desc, --o2, o2-desc, --o3, o3-desc]";
        assertEquals(expected, group.toString());
    }
}