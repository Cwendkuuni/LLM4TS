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
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals("opt1", optionGroup.getSelected());
        assertEquals(Collections.singletonList("opt1"), optionGroup.getNames());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("opt1", optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
        assertEquals("opt1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("opt2", optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        OptionGroup optionGroup2 = new OptionGroup();
        assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option(null, "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        Option option3 = new Option("opt3", "Option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[--Option-1, Option 1]\n" +
                "  --Option-1\n" +
                "  Option 1\n" +
                "[--opt2, Option 2]\n" +
                "  --opt2\n" +
                "  Option 2\n" +
                "[--opt3, Option 3]\n" +
                "  --opt3\n" +
                "  Option 3\n";

        assertEquals(expected, optionGroup.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>(Arrays.asList(option1, option2));

        assertEquals(expectedOptions, optionGroup.getOptions());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> expectedNames = new ArrayList<>(Arrays.asList("opt1", "opt2"));

        assertEquals(expectedNames, optionGroup.getNames());
    }
}