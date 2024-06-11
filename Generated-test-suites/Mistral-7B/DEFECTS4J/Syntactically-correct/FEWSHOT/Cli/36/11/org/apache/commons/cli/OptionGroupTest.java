package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "a", true, "A option");
        Option option2 = new Option("b", "b", true, "B option");

        group.addOption(option);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals("A option", group.getOptions().get(0).getDescription());
        assertEquals("B option", group.getOptions().get(1).getDescription());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "a");
        Option option2 = new Option("b", "b");

        group.addOption(option);
        group.addOption(option2);

        Collection<String> names = group.getNames();
        assertEquals(Arrays.asList("a", "b"), names);
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "a");
        Option option2 = new Option("b", "b");

        group.addOption(option);
        group.addOption(option2);

        Collection<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertEquals(option, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "a");
        Option option2 = new Option("b", "b");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));
        group.setSelected(option2);
        assertEquals("b", group.getSelected());
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "a");
        Option option2 = new Option("b", "b");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        group.setSelected(option2);
        assertEquals("b", group.getSelected());

        assertNull(group.getSelected());
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
        Option option = new Option("a", "a");
        Option option2 = new Option("b", "b");

        group.addOption(option);
        group.addOption(option2);

        String expected = "[-a, --a, --A option ]";
        assertEquals(expected, group.toString());
    }
}