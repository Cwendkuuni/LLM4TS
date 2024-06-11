package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(opt1);
        group.addOption(opt2);

        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(opt1));
        assertTrue(group.getOptions().contains(opt2));
        assertEquals(Arrays.asList("opt1", "opt2"), group.getNames());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(opt1);
        group.addOption(opt2);

        assertEquals(Arrays.asList("opt1", "opt2"), group.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(opt1);
        group.addOption(opt2);

        List<Option> options = group.getOptions();
        assertSame(opt1, options.get(0));
        assertSame(opt2, options.get(1));
    }

    @Test
    public void testSetSelected_null() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);

        assertNull(group.getSelected());

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelected_same() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);

        group.setSelected(opt1);

        assertSame(opt1, group.getSelected());

        group.setSelected(opt1);

        assertSame(opt1, group.getSelected());
    }

    @Test
    public void testSetSelected_different() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);

        assertNull(group.getSelected());

        group.setSelected(opt1);

        assertSame(opt1, group.getSelected());

        group.setSelected(opt2);

        assertSame(opt2, group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);

        group.setSelected(opt1);

        assertSame(opt1, group.getSelected());

        group.setSelected(opt2);

        assertEquals("Option 'opt2' was already selected in this OptionGroup", group.getSelectedException().getMessage());
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o", "opt1", true, "Option 1");
        Option opt2 = new Option("o", "opt2", true, "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);

        group.setSelected(opt1);

        assertSame(opt1, group.getSelected());
    }

    @Test
    public void testSetRequired_false() {
        OptionGroup group = new OptionGroup();
        group.setRequired(false);

        assertFalse(group.isRequired());
    }

    @Test
    public void testSetRequired_true() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        assertTrue(group.isRequired());
    }
}