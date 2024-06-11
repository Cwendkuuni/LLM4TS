package org.apache.commons.cli;

import org.junit.Test;

import java.util.collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option opt = new Option("a", "add", false, "Add an option");
        OptionGroup group = new OptionGroup();
        group.addOption(opt);
        assertEquals(1, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getOptions().contains(opt));
    }

    @Test
    public void testSetSelected() {
        Option opt1 = new Option("a", "add", false, "Add an option");
        Option opt2 = new Option("r", "remove", false, "Remove an option");
        OptionGroup group = new OptionGroup();
        group.addOption(opt1);
        group.addOption(opt2);
        group.setSelected(opt1);
        assertEquals("a", group.getSelected());
        group.setSelected(opt2);
        assertEquals("r", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
        group.setRequired(false);
        assertTrue(!group.isRequired());
    }

    @Test
    public void testToString() {
        Option opt1 = new Option("a", "add", false, "Add an option");
        Option opt2 = new Option("r", "remove", false, "Remove an option");
        OptionGroup group = new OptionGroup();
        group.addOption(opt1);
        group.addOption(opt2);
        String expected = "[-a Add an option, -r Remove an option]";
        assertEquals(expected, group.toString());
    }
}