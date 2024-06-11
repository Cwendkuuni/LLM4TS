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
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");
        Option option2 = new Option("h", null, true, "Print help message.");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getNames().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");
        Option option2 = new Option("h", null, true, "Print help message.");

        group.addOption(option1);
        group.addOption(option2);

        List<String> names = Arrays.asList("f", "h");
        assertEquals(names, group.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");
        Option option2 = new Option("h", null, true, "Print help message.");

        group.addOption(option1);
        group.addOption(option2);

        List<Option> options = Arrays.asList(option1, option2);
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");
        Option option2 = new Option("h", null, true, "Print help message.");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals(option1.getKey(), group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));
    }

    @Test
    public void testSetSelectedWithNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");

        group.addOption(option1);
        group.setSelected(option1);

        assertNull(group.getSelected());
        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedWithSameOptionTwice() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");

        group.addOption(option1);
        group.setSelected(option1);

        assertEquals(option1.getKey(), group.getSelected());
        group.setSelected(option1);
        assertEquals(option1.getKey(), group.getSelected());
    }

    @Test
    public void testSetSelectedWithDifferentOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");
        Option option2 = new Option("h", null, true, "Print help message.");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(null, group.getSelected());
        group.setSelected(option2);
        assertEquals(option2.getKey(), group.getSelected());
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");
        Option option2 = new Option("h", null, true, "Print help message.");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());
        group.setSelected(option1);
        assertEquals(option1.getKey(), group.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        OptionGroup requiredGroup = new OptionGroup();
        requiredGroup.setRequired(true);

        assertFalse(group.isRequired());
        assertTrue(requiredGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "help", false, "Print help message, with detailed options:");
        Option option2 = new Option("h", null, true, "Print help message.");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-f, Print help message, with detailed options:, --help, Print help message.]";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithEmptyGroup() {
        OptionGroup group = new OptionGroup();
        assertEquals("[]", group.toString());
    }

    @Test
    public void testToStringWithEmptyList() {
        OptionGroup group = new OptionGroup();
        Map<String, Option> optionMap = new HashMap<String, Option>();
        group.setOptions(optionMap);
        assertEquals("[]", group.toString());
    }

    @Test
    public void testToStringWithSingleOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "help", false, "Print help message, with detailed options:");
        group.addOption(option);

        String expected = "-f, Print help message, with detailed options:";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithLongOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("longOption1", "longOption1Description", false, "Long option 1 description:");
        Option option2 = new Option("longOption2", "longOption2Description", false, "Long option 2 description:");
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--longOption1, Long option 1 description:\n--longOption2, Long option 2 description:]";
        assertEquals(expected, group.toString());
    }
}