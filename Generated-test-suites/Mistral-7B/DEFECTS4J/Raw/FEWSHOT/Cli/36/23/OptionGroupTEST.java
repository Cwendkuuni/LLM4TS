package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();

        Option opt1 = new Option(null, "o1", false, "Option 1");
        Option opt2 = new Option(null, "o2", false, "Option 2");
        Option opt3 = new Option(null, "o3", false, "Option 3");

        optionGroup.addOption(opt1);
        optionGroup.addOption(opt2);
        optionGroup.addOption(opt3);
    }

    @Test
    public void testGetNames() {
        Set<String> names = new HashSet<>(optionGroup.getNames());
        List<String> expectedNames = new ArrayList<>(Collections.nCopies(3, "o1"));
        List<String> actualNames = new ArrayList<>(names);

        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = new ArrayList<>(optionGroup.getOptions());
        List<Option> expectedOptions = new ArrayList<>(Collections.nCopies(3, opt1));
        List<Option> actualOptions = new ArrayList<>(options);

        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testSetSelectedNull() {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOptionTwice() {
        optionGroup.setSelected(opt1);
        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(opt1));
    }

    @Test
    public void testSetSelectedNewOption() {
        optionGroup.setSelected(opt2);
        assertEquals(opt2.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequiredFalseByDefault() {
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetRequiredTrue() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedWithNullOption() {
        optionGroup.setSelected(null);
    }
}