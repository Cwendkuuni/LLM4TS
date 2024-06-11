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
        Option option = new Option(null, "f", false, "A test option");
        Option addedOption = group.addOption(option);
        assertEquals("f", addedOption.getOpt());
        assertEquals("A test option", addedOption.getDescription());
        assertEquals(1, group.getOptions().size());
        assertEquals("f", ((Option) group.getOptions().toArray()[0]).getOpt());
    }

    @Test
    public void testAddDuplicateOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option(null, "f", false, "A test option");
        Option duplicateOption = new Option(null, "f", false, "Another test option");
        assertThrows(IllegalStateException.class, () -> group.addOption(option));
        assertThrows(IllegalStateException.class, () -> group.addOption(duplicateOption));
        assertEquals(0, group.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option(null, "f", false, "Option 1");
        Option option2 = new Option(null, "g", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        List expectedNames = Arrays.asList("f", "g");
        List actualNames = (List) group.getNames();
        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option(null, "f", false, "Option 1");
        Option option2 = new Option(null, "g", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        List expectedOptions = Arrays.asList(option1, option2);
        List actualOptions = (List) group.getOptions();
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testSetSelectedNullOption() {
        OptionGroup group = new OptionGroup();
        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option(null, "f", false, "A test option");
        group.setSelected(option);
        assertSame(option, group.getSelectedOption());
    }

    @Test
    public void testSetSelectedDifferentOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option(null, "f", false, "Option 1");
        Option option2 = new Option(null, "g", false, "Option 2");
        group.setSelected(option1);
        assertSame(option1, group.getSelectedOption());
        assertNull(group.getSelected("g"));

        group.setSelected(option2);
        assertSame(option2, group.getSelectedOption());
        assertNull(group.getSelected("f"));
    }

    @Test
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option(null, "f", false, "A test option");
        group.setSelected(option);
        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option));
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option(null, "f", false, "Option 1");
        Option option2 = new Option(null, "g", false, "Option 2");
        group.setSelected(option1);
        assertEquals("f", group.getSelected());
        group.setSelected(option2);
        assertEquals("g", group.getSelected());
        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testIsRequiredFalseByDefault() {
        OptionGroup group = new OptionGroup();
        assertFalse(group.isRequired());
    }

    @Test
    public void testSetAndGetRequiredTrue() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option(null, "f", false, "Option 1");
        Option option2 = new Option(null, "g", false, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expectedToString = "[-- Option 1 Option 1\n"
                                  + "  Option 1\n"
                                  + "-- Option 2 Option 2\n"
                                  + "  Option 2]";
        String actualToString = group.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToStringEmptyGroup() {
        OptionGroup group = new OptionGroup();
        String expectedToString = "[ ]";
        String actualToString = group.toString();
        assertEquals(expectedToString, actualToString);
    }
}