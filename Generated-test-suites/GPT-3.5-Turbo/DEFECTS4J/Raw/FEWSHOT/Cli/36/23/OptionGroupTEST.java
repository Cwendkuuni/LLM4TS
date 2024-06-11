package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(option1, group.getOptions().get(0));
        assertEquals(option2, group.getOptions().get(1));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("f"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(option1, group.getOptions().get(0));
        assertEquals(option2, group.getOptions().get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("f", group.getSelected());
    }

    @Test(expected=AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2); // Should throw AlreadySelectedException
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("f", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[f, b]", group.toString());
    }
}