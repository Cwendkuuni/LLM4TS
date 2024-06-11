package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(option1, group.getOptions().iterator().next());
        assertEquals(option2, group.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        List<String> names = new ArrayList<>(group.getNames());
        Collections.sort(names);

        List<String> expectedNames = Arrays.asList("opt1", "opt2");
        assertEquals(expectedNames, names);
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        try {
            group.setSelected(option1);
            assertEquals("opt1", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException: " + e.getMessage());
        }

        try {
            group.setSelected(option2);
            assertEquals("opt2", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException: " + e.getMessage());
        }

        try {
            group.setSelected(option1);
            fail("AlreadySelectedException expected but not thrown");
        } catch (AlreadySelectedException e) {
            // Expected
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-opt1 Option 1, -opt2 Option 2]";
        assertEquals(expected, group.toString());
    }
}