package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "desc1");
        Option option2 = new Option("o2", "opt2", true, "desc2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getNames().size());
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
        assertEquals("opt1", group.getOptions().get(0).getOpt());
        assertEquals("opt2", group.getOptions().get(1).getOpt());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "desc1");
        Option option2 = new Option("o2", "opt2", true, "desc2");

        group.addOption(option1);
        group.addOption(option2);

        Set<String> names = new HashSet<>(group.getNames());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "desc1");
        Option option2 = new Option("o2", "opt2", true, "desc2");

        group.addOption(option1);
        group.addOption(option2);

        List<Option> options = new ArrayList<>(group.getOptions());
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();

        assertNull(group.getSelected());

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "desc1");
        Option option2 = new Option("o2", "opt2", true, "desc2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals(option1, group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwice() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "desc1");
        Option option2 = new Option("o2", "opt2", true, "desc2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
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
        Option option1 = new Option("o1", "opt1", false, "desc1");
        Option option2 = new Option("o2", "opt2", true, "desc2");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-o1 desc1, --opt1 desc1, -o2 desc2, --opt2 desc2]";

        assertEquals(expected, group.toString());
    }
}