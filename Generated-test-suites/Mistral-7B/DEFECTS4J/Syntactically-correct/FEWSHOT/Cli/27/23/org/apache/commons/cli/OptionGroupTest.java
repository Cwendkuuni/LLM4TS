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
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", true);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getOptions().size());
        assertEquals(3, optionGroup.getNames().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", true);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> names = new ArrayList<>(optionGroup.getNames());
        List<String> expectedNames = Arrays.asList("o1", "o2", "o3");

        assertEquals(expectedNames, names);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", true);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());
        List<Option> expectedOptions = Arrays.asList(option1, option2, option3);

        assertEquals(expectedOptions, options);
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", true);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2, optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", true);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", true);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[-o1 Option 1\n--o2 Option 2\n--o3 Option 3\n]";
        assertEquals(expected, optionGroup.toString());
    }
}