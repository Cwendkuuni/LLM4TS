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
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().toArray()[0]);
        assertEquals(option2, optionGroup.getOptions().toArray()[1]);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> names = Arrays.asList("o1", "o2", "o3");
        assertEquals(names, optionGroup.getNames());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedOptionAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    @Test
    public void testSetSelectedNewOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 Option 1,\n--o2 Option 2,\n]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToStringWithSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option2);

        String expected = "[--o2 Option 2,\n]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToStringWithRequiredOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setRequired(true);

        String expected = "[-o1 Option 1,\n--o2 Option 2,\n]";
        assertEquals(expected, optionGroup.toString());
    }
}