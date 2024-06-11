package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();

        assertEquals(2, options.size());
        assertEquals(option1, options.iterator().next());
        assertEquals(option2, options.iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();

        assertEquals(2, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);

        optionGroup.setSelected(option1);
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    @Test
    public void testSetSelectedDifferentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2");

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[ -o1 option 1\n, --o2 option 2\n]", optionGroup.toString());
    }

    @Test
    public void testToStringWithSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("[ -o1 option 1\n]", optionGroup.toString());
    }
}