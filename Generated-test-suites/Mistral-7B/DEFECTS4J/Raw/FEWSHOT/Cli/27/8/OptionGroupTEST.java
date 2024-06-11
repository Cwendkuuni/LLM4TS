package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertSame(option1, optionGroup.getOptions().toArray()[0]);
        assertSame(option2, optionGroup.getOptions().toArray()[1]);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List names = (List) optionGroup.getNames();
        assertEquals(2, names.size());
        assertEquals(option1.getKey(), names.get(0));
        assertEquals(option2.getKey(), names.get(1));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List options = (List) optionGroup.getOptions();
        assertEquals(2, options.size());
        assertSame(option1, options.get(0));
        assertSame(option2, options.get(1));
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
    public void testSetSelectedSame() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedDifferent() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            fail();
        } catch (AlreadySelectedException e) {
            // Expected
        }
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

        String expected = "[-o1 Option 1, --o2 Option 2 ]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToStringEmpty() {
        OptionGroup optionGroup = new OptionGroup();

        String expected = "[ ]";
        assertEquals(expected, optionGroup.toString());
    }
}