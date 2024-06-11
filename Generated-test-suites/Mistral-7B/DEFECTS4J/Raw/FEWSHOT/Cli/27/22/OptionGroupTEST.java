package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "file", true, "Specify file name");

        group.addOption(option);
        assertEquals(1, group.getOptions().size());
        assertEquals("file", group.getSelected());
    }

    @Test
    public void testAddSameOptionTwice() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "file", true, "Specify file name");

        group.addOption(option);
        assertThrows(AlreadySelectedException.class, () -> group.addOption(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        List<String> names = new ArrayList<>(group.getNames());
        List<String> expectedNames = Collections.singletonList("f");

        assertEquals(expectedNames, names);
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        List<Option> options = new ArrayList<>(group.getOptions());

        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("file", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("file", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(false);
        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        String expected = "[-f Specify file name, --ignore Ignore errors ]";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithSelectedOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        String expected = "[-f Specify file name, --ignore Ignore errors ]";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithSelectedLongOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option(null, "file", true, "Specify file name");
        Option option2 = new Option("i", "ignore", false, "Ignore errors");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        String expected = "[--file Specify file name, --ignore Ignore errors ]";
        assertEquals(expected, group.toString());
    }
}