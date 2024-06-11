package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");

        group.addOption(option);
        group.addOption(option2);

        Collection options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testAddDuplicateOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("optA", "Option A");

        group.addOption(option);
        try {
            group.addOption(option);
            fail("AlreadySelectedException expected");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");
        group.addOption(option1);
        group.addOption(option2);

        Collection names = group.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("optA"));
        assertTrue(names.contains("optB"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");
        group.addOption(option1);
        group.addOption(option2);

        Collection options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelectedNullOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("optA", "Option A");
        group.setSelected(option);
        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("optA", "Option A");
        group.setSelected(option);
        group.setSelected(option);
        assertEquals(option.getOpt(), group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");
        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testGetSelectedNull() {
        OptionGroup group = new OptionGroup();
        assertNull(group.getSelected());
    }

    @Test
    public void testGetSelectedOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("optA", "Option A");
        group.setSelected(option);
        assertEquals(option.getOpt(), group.getSelected());
    }

    @Test
    public void testSetRequiredFalse() {
        OptionGroup group = new OptionGroup();
        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    @Test
    public void testSetRequiredTrue() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");
        group.addOption(option1);
        group.addOption(option2);
        String expected = "[-optA Option A, --optB Option B]";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringEmpty() {
        OptionGroup group = new OptionGroup();
        String expected = "[ ]";
        assertEquals(expected, group.toString());
    }
}