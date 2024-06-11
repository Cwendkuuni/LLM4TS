package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();

        Option opt1 = new Option("o1", "option1", true, "Op1 Desc");
        Option opt2 = new Option("o2", "option2", false, "Op2 Desc");
        Option opt3 = new Option("o3", "option3", false, "Op3 Desc");

        optionGroup.addOption(opt1);
        optionGroup.addOption(opt2);
        optionGroup.addOption(opt3);
    }

    @Test
    public void testGetNames() {
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("o1");
        expectedNames.add("o2");
        expectedNames.add("o3");

        Collection<String> names = optionGroup.getNames();
        assertTrue(names instanceof java.util.AbstractCollection);
        assertTrue(names instanceof Collection);
        assertSame(names.getClass(), Collections.unmodifiableCollection(optionGroup.optionMap.keySet()).getClass());
        assertEquals(expectedNames.size(), names.size());
        for (String name : expectedNames) {
            assertTrue(names.contains(name));
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(opt1);
        expectedOptions.add(opt2);
        expectedOptions.add(opt3);

        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options instanceof java.util.AbstractCollection);
        assertTrue(options instanceof Collection);
        assertSame(options.getClass(), Collections.unmodifiableCollection(optionGroup.optionMap.values()).getClass());
        assertEquals(expectedOptions.size(), options.size());
        for (Option option : expectedOptions) {
            assertTrue(options.contains(option));
        }
    }

    @Test
    public void testSetSelectedNull() {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedOptionAlreadySelected() throws AlreadySelectedException {
        optionGroup.setSelected(opt1);
        optionGroup.setSelected(opt1);
    }

    @Test
    public void testSetSelectedOption() throws AlreadySelectedException {
        optionGroup.setSelected(opt1);
        assertEquals(opt1.getOpt(), optionGroup.getSelected());
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
        String expectedString = "[-o1 Op1 Desc, --option1, -o2 Op2 Desc, --option2, -o3 Op3 Desc, --option3]";
        assertEquals(expectedString, optionGroup.toString());
    }
}