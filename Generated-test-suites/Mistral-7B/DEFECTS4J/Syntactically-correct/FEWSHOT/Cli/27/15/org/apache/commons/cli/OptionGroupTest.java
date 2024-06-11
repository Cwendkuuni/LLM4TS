package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", false);
        Option option2 = new Option("o2", "option 2", true);
        Option option3 = new Option("o3", "option 3", false);
        Option option4 = new Option("o4", "option 4", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        optionGroup.addOption(option4);
    }

    @Test
    public void testGetNames() {
        Set names = new HashSet(optionGroup.getNames());
        assertEquals(Arrays.asList("o1", "o2", "o3", "o4"), names);
    }

    @Test
    public void testGetOptions() {
        assertEquals(Arrays.asList(option1, option2, option3, option4), optionGroup.getOptions());
    }

    @Test
    public void testSetSelectedNull() {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOptionTwice() {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOptions() {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedAndGetSelected() {
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequiredFalse() {
        optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        assertEquals("[--option-1 option 1, -o2 option 2, -o3 option 3, -o4 option 4]", optionGroup.toString());
    }

    @Test
    public void testToStringWithSelectedOption() {
        optionGroup.setSelected(option1);
        assertEquals("[--option-1 option 1, -o2 option 2, -o3 option 3, -o4 option 4]", optionGroup.toString());
    }

    @Test
    public void testToStringWithEmptyOptions() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertEquals("[]", optionGroup.toString());
    }

    @Test
    public void testToStringWithEmptySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
        optionGroup.setRequired(true);
        assertEquals("[]", optionGroup.toString());
    }

    @Test
    public void testToStringWithSelectedLongOption() {
        Option longOption = new Option("longOptionName", "long option", false, "longOptionDescription");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(longOption);
        optionGroup.setSelected(longOption);
        assertEquals("[--longOptionName long option]", optionGroup.toString());
    }
}