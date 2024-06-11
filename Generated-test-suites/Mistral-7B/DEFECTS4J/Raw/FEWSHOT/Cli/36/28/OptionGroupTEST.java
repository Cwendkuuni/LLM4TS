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
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getNames().size());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

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
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option2);

        assertEquals(option2.getKey(), group.getSelected());

        group.setSelected(option1);

        assertEquals(option1.getKey(), group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(option2);
        });
    }

    @Test
    public void testSetSelectedTwiceSameOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "a", false, "a help message");

        group.addOption(option1);

        group.setSelected(option1);

        assertEquals(option1.getKey(), group.getSelected());

        group.setSelected(option1);

        assertEquals(option1.getKey(), group.getSelected());
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
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-a, --opt1 a help message, -b, --opt2 another help message]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithEmptyOptionGroup() {
        OptionGroup group = new OptionGroup();

        String expected = "[ ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithSelectedOption() {
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        String expected = "[--opt1 a help message]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithRequiredOptionGroup() {
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setRequired(true);

        String expected = "[--opt1 a help message, --opt2 another help message]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithSelectedAndRequiredOptionGroup() {
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setRequired(true);

        String expected = "[--opt1 a help message]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithMultipleSelectedOptions() {
        Option option1 = new Option("opt1", "a", false, "a help message");
        Option option2 = new Option("opt2", "b", false, "another help message");
        Option option3 = new Option("opt3", "c", false, "another help message");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        group.setSelected(option1);
        group.setSelected(option3);

        String expected = "[--opt1 a help message, --opt3 another help message]";

        assertEquals(expected, group.toString());
    }
}