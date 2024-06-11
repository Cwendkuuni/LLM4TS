package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", true, "o2-desc");
        Option option3 = new Option("o3", "option3", false, "o3-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }

    @Test
    public void testAddOption() {
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("o1");
        expectedNames.add("o2");
        expectedNames.add("o3");

        assertEquals(expectedNames, new ArrayList<>(optionGroup.getNames()));
    }

    @Test
    public void testSetSelectedNull() {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        String selectedOption = option1.getOpt();
        optionGroup.setSelected(option1);
        assertEquals(selectedOption, optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() {
        String option2Name = option2.getOpt();
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequiredFalse() {
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expectedToString = "[-o1 option1 , --o1-desc -o2 option2 <option2-desc> -o3 option3 , --o3-desc]";
        assertEquals(expectedToString, optionGroup.toString());
    }
}