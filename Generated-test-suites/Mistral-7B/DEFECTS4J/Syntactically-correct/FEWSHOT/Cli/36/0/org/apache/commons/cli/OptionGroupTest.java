package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroupEmpty() {
        OptionGroup group = new OptionGroup();
        assertEquals(Collections.emptyList(), group.getNames());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "a");
        group.addOption(option);

        assertEquals(Arrays.asList("a"), group.getNames());
        assertEquals(Arrays.asList(option), group.getOptions());
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupAddDuplicateOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "a");
        group.addOption(option);

        assertThrows(IllegalStateException.class, () -> {
            group.addOption(option);
        });

        assertEquals(Arrays.asList("a"), group.getNames());
        assertEquals(Arrays.asList(option), group.getOptions());
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupSetSelected() {
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "a");
        Option optionB = new Option("b", "b");
        group.addOption(optionA);
        group.addOption(optionB);

        assertNull(group.getSelected());

        group.setSelected(optionA);
        assertEquals("a", group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(optionA);
        });

        group.setSelected(optionB);
        assertEquals("b", group.getSelected());
    }

    @Test
    public void testOptionGroupSetSelectedTwice() {
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "a");
        Option optionB = new Option("b", "b");
        group.addOption(optionA);
        group.addOption(optionB);

        assertNull(group.getSelected());

        group.setSelected(optionA);
        assertEquals("a", group.getSelected());

        group.setSelected(optionB);
        assertEquals("b", group.getSelected());

        group.setSelected(optionA);
        assertEquals("a", group.getSelected());
    }

    @Test
    public void testOptionGroupIsRequired() {
        OptionGroup group = new OptionGroup();
        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());

        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testOptionGroupNullOption() {
        OptionGroup group = new OptionGroup();
        group.addOption(null);
    }
}