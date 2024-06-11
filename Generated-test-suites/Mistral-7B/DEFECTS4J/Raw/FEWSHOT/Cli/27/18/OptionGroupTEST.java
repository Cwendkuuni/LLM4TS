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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false);
        Option option2 = new Option("o2", "option 2", true);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals("o1", optionGroup.getOptions().toArray()[0].getKey());
        assertEquals("o2", optionGroup.getOptions().toArray()[1].getKey());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false);
        Option option2 = new Option("o2", "option 2", true);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());

        assertEquals(2, names.size());
        assertEquals("o1", names.get(0));
        assertEquals("o2", names.get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false);
        Option option2 = new Option("o2", "option 2", true);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));

        optionGroup.setSelected(option2);
        assertEquals("o2", optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false);
        Option option2 = new Option("o2", "option 2", true);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 option 1, --o2 option 2 ]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testSetSelectedWithAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false);
        Option option2 = new Option("o2", "option 2", true);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));

        optionGroup.setSelected(option2);
        assertEquals("o2", optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false);

        optionGroup.addOption(option1);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedWithNonExistentOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false);

        optionGroup.addOption(option1);

        assertNull(optionGroup.getSelected());

        assertThrows(IllegalArgumentException.class, () -> optionGroup.setSelected(new Option("nonexistent", "nonexistent", false)));
    }
}