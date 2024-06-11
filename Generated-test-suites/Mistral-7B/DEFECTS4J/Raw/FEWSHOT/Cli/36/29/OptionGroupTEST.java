package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class OptionGroupTest {

    /**
     * Test adding an option to an OptionGroup and getting it back.
     */
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "file", false, "file path");
        Option addedOption = group.addOption(option);
        assertEquals(option, addedOption);
    }

    /**
     * Test getting options from an OptionGroup.
     */
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file1", false, "file1 path");
        Option option2 = new Option("f", "file2", false, "file2 path");
        Option option3 = new Option("g", "group", false, "group name");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(Arrays.asList(option1, option2, option3), group.getOptions().stream().collect(Collectors.toList()));
    }

    /**
     * Test getting names of options from an OptionGroup.
     */
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file1", false, "file1 path");
        Option option2 = new Option("f", "file2", false, "file2 path");
        Option option3 = new Option("g", "group", false, "group name");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(Arrays.asList("f", "file1", "f", "file2", "g", "group"), group.getNames());
    }

    /**
     * Test setting and getting the selected option in an OptionGroup.
     */
    @Test
    public void testSetAndGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file1", false, "file1 path");
        Option option2 = new Option("f", "file2", false, "file2 path");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals("file1", group.getSelected());

        group.setSelected(option2);
        assertEquals("file2", group.getSelected());

        assertThrows(IllegalArgumentException.class, () -> group.setSelected(null));
        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals("file1", group.getSelected());
    }

    /**
     * Test setting and getting the required property in an OptionGroup.
     */
    @Test
    public void testSetAndGetRequired() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());

        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    /**
     * Test selecting an already selected option in an OptionGroup.
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSelectAlreadySelectedOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file1", false, "file1 path");
        Option option2 = new Option("f", "file2", false, "file2 path");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    /**
     * Test getting string representation of an OptionGroup.
     */
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file1", false, "file1 path");
        Option option2 = new Option("f", "file2", false, "file2 path");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--file1 file1 path, --file2 file2 path]";
        assertEquals(expected, group.toString());
    }
}