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
import java.util.Set;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        Set<Option> expectedOptions = new HashSet<>(Arrays.asList(option1, option2));
        Set<Option> actualOptions = new HashSet<>(group.getOptions());

        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        Option option3 = new Option("o", "opt3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        Collection<String> expectedNames = Arrays.asList("opt1", "opt2", "opt3");
        Collection<String> actualNames = group.getNames();

        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "opt", false, "Option");

        group.setSelected(option);
        group.setSelected(option);

        assertEquals(option.getKey(), group.getSelected());
    }

    @Test
    public void testSetSelectedDifferentOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");

        group.setSelected(option1);
        assertSame(option1, group.getSelected());

        group.setSelected(option2);
        assertSame(option2, group.getSelected());

        assertEquals(option2.getKey(), group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");

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

    @Test(expected = NullPointerException.class)
    public void testToStringNullOptions() {
        OptionGroup group = new OptionGroup();

        group.toString();
    }

    @Test
    public void testToStringEmptyOptions() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");

        group.addOption(option);

        String expectedToString = "[-option]";
        String actualToString = group.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToStringMultipleOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        Option option3 = new Option("o3", "opt3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        String expectedToString = "[--opt1, --opt2, --opt3]";
        String actualToString = group.toString();

        assertEquals(expectedToString, actualToString);
    }
}