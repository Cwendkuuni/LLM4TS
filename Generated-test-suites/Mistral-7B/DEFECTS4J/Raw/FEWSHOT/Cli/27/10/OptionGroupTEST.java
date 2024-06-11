package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testAddSameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");

        optionGroup.addOption(option1);
        assertThrows(IllegalArgumentException.class, () -> optionGroup.addOption(option1));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("o2", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup1.setRequired(true);
        optionGroup2.setRequired(false);

        assertEquals(true, optionGroup1.isRequired());
        assertEquals(false, optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        Option option3 = new Option("o3", "option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals("[--o1 option 1, --o2 option 2, --o3 option 3]", optionGroup.toString());
    }
}