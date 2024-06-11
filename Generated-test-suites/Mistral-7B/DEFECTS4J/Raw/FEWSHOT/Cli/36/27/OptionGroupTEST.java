package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
        Option option1 = new Option("o1", "opt1", true, "o1 description");
        Option option2 = new Option("o2", "opt2", false, "o2 description");

        OptionGroup addedGroup = group.addOption(option1).addOption(option2);

        Map<String, Option> expectedMap = new HashMap<>();
        expectedMap.put("o1", option1);
        expectedMap.put("o2", option2);

        assertEquals(expectedMap, addedGroup.optionMap);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "o1 description");
        Option option2 = new Option("o2", "opt2", false, "o2 description");

        group.addOption(option1).addOption(option2);

        List<String> expectedNames = new ArrayList<>(Arrays.asList("o1", "o2"));

        List<String> names = new ArrayList<>(group.getNames());

        assertEquals(expectedNames, names);
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "o1 description");
        Option option2 = new Option("o2", "opt2", false, "o2 description");

        group.addOption(option1).addOption(option2);

        List<Option> expectedOptions = new ArrayList<>(Arrays.asList(option1, option2));

        List<Option> options = new ArrayList<>(group.getOptions());

        assertEquals(expectedOptions, options);
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "o1 description");
        Option option2 = new Option("o2", "opt2", false, "o2 description");

        group.addOption(option1).addOption(option2);

        group.setSelected(option1);

        String expectedSelected = "o1";

        String selected = group.getSelected();

        assertEquals(expectedSelected, selected);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "o1 description");
        Option option2 = new Option("o2", "opt2", false, "o2 description");

        group.addOption(option1).addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "o1 description");

        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "o1 description");

        group.setRequired(false);

        assertFalse(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", true, "o1 description");
        Option option2 = new Option("o2", "opt2", false, "o2 description");

        group.addOption(option1).addOption(option2);

        String expectedString = "[--opt1 opt1 description, --opt2 opt2 description]";

        String stringifiedGroup = group.toString();

        assertEquals(expectedString, stringifiedGroup);
    }
}