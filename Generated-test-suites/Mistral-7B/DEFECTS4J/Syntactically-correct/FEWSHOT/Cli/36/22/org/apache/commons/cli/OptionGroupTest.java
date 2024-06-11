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
        Option option1 = new Option("o", "o", true, "Option 1");
        Option option2 = new Option("o", "o2", false, "Option 2");

        OptionGroup groupWithOption1 = group.addOption(option1);
        OptionGroup groupWithOption2 = groupWithOption1.addOption(option2);

        assertEquals(Option.class, groupWithOption2.getClass());
        assertEquals(2, groupWithOption2.getOptions().size());
        assertEquals(Arrays.asList(option1, option2), groupWithOption2.getOptions());
        assertEquals(2, groupWithOption2.getNames().size());
        assertEquals(Arrays.asList("o", "o2"), groupWithOption2.getNames());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "o1", false, "Option 1");
        Option option2 = new Option("o", "o2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "o1", false, "Option 1");
        Option option2 = new Option("o", "o2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals(option1.getKey(), group.getSelected());

        group.setSelected(option1);

        assertEquals(option1.getKey(), group.getSelected());
    }

    @Test
    public void testSetSelectedDifferentOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "o1", false, "Option 1");
        Option option2 = new Option("o", "o2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option2);

        assertEquals(option2.getKey(), group.getSelected());
    }

    @Test
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "o1", false, "Option 1");
        Option option2 = new Option("o", "o2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));
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
        Option option1 = new Option("o", "o1", false, "Option 1");
        Option option2 = new Option("o", "o2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--o1 Option 1, --o2 Option 2]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringEmpty() {
        OptionGroup group = new OptionGroup();

        String expected = "[ ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithLongOpt() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("l", "longOption1", false, "Long Option 1");
        Option option2 = new Option("l", "longOption2", false, "Long Option 2");
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--longOption1 Long Option 1, --longOption2 Long Option 2]";

        assertEquals(expected, group.toString());
    }
}