package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("--option1", true, "Option 1 description");
        Option option2 = new Option("--option2", false, "Option 2 description");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(option1, group.getOptions().iterator().next());
        assertEquals(option2, group.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("--option1", true, "Option 1 description");
        Option option2 = new Option("--option2", false, "Option 2 description");

        group.addOption(option1);
        group.addOption(option2);

        Set<String> names = new HashSet<>(group.getNames());
        assertTrue(names.containsAll(Arrays.asList("--option1", "--option2")));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("--option1", true, "Option 1 description");
        Option option2 = new Option("--option2", false, "Option 2 description");

        group.addOption(option1);
        group.addOption(option2);

        assertTrue(group.getOptions().containsAll(Arrays.asList(option1, option2)));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();

        assertNull(group.getSelected());

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSame() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("--option", false, "Option description");

        assertNull(group.getSelected());

        group.setSelected(option);

        assertSame(option, group.getSelected());
    }

    @Test
    public void testSetSelectedDifferent() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("--option1", false, "Option 1 description");
        Option option2 = new Option("--option2", false, "Option 2 description");

        group.setSelected(option1);

        assertSame(option1, group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(option2);
        });

        assertSame(option1, group.getSelected());
    }

    @Test
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("--option", false, "Option description");

        group.setSelected(option);

        assertSame(option, group.getSelected());

        group.setSelected(option);

        assertSame(option, group.getSelected());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());

        group.setRequired(false);

        assertFalse(group.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());

        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("--option1", true, "Option 1 description");
        Option option2 = new Option("--option2", false, "Option 2 description");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--option1, --option2]";

        assertEquals(expected, group.toString());
    }
}