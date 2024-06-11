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
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        assertEquals(0, group.getOptions().size());
        assertEquals(0, group.getNames().size());

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getNames().size());

        assertEquals(option1, group.getOptions().toArray()[0]);
        assertEquals(option2, group.getOptions().toArray()[1]);
        assertEquals("opt1", group.getNames().toArray()[0].toString());
        assertEquals("opt2", group.getNames().toArray()[1].toString());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        List<String> names = Arrays.asList("opt1", "opt2");
        assertEquals(names, Collections.unmodifiableList(group.getNames()));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        List<Option> options = Arrays.asList(option1, option2);
        assertEquals(options, Collections.unmodifiableList(group.getOptions()));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

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
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

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
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[-opt1 Option 1, --opt2 Option 2]", group.toString());
    }
}