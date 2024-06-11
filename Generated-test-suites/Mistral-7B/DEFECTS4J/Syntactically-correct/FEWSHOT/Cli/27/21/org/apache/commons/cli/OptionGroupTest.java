package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getNames().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        Iterator<String> namesIterator = group.getNames().iterator();
        assertEquals("a", namesIterator.next());
        assertEquals("b", namesIterator.next());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        Iterator<Option> optionsIterator = group.getOptions().iterator();
        assertEquals(option1, optionsIterator.next());
        assertEquals(option2, optionsIterator.next());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();

        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedExisting() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);

        assertEquals("a", group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));
    }

    @Test
    public void testSetSelectedNew() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option2);
        assertEquals("b", group.getSelected());
    }

    @Test
    public void testSetRequiredTrue() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertEquals(true, group.isRequired());
    }

    @Test
    public void testSetRequiredFalse() {
        OptionGroup group = new OptionGroup();
        group.setRequired(false);
        assertEquals(false, group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);

        String expected = "[-a option A, --option-A option A, ]";
        assertEquals(expected, group.toString());
    }
}