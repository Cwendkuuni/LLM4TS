package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.AlreadySelectedException;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2", false);

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getNames().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2", false);

        group.addOption(option1);
        group.addOption(option2);

        List<String> names = Arrays.asList("o1", "o2");
        assertEquals(names, Collections.unmodifiableList(group.getNames()));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2", false);

        group.addOption(option1);
        group.addOption(option2);

        List<Option> options = Arrays.asList(option1, option2);
        assertEquals(options, Collections.unmodifiableList(group.getOptions()));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);

        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSameOptionTwice() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);

        group.setSelected(option1);
        group.setSelected(option1);

        assertEquals(option1.getOpt(), group.getSelected());
    }

    @Test
    public void testSetSelectedDifferentOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2", false);

        group.setSelected(option1);
        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option2));
    }

    @Test
    public void testSetSelectedNullAfterReset() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);

        group.setSelected(option1);
        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testGetSelectedNull() {
        OptionGroup group = new OptionGroup();

        assertNull(group.getSelected());
    }

    @Test
    public void testGetSelectedSetOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);

        group.setSelected(option1);

        assertEquals(option1.getOpt(), group.getSelected());
    }

    @Test
    public void testIsRequiredFalseByDefault() {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToStringEmptyGroup() {
        OptionGroup group = new OptionGroup();

        assertEquals("[]", group.toString());
    }

    @Test
    public void testToStringSingleOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);

        group.addOption(option1);

        assertEquals("[ -o1 option 1]", group.toString());
    }

    @Test
    public void testToStringMultipleOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1", true);
        Option option2 = new Option("o2", "option 2", false);

        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[ -o1 option 1, --o2 option 2]", group.toString());
    }

    @Test
    public void testToStringMultipleOptionsWithLongOpt() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true);
        Option option2 = new Option("o2", "option2", false);

        group.addOption(option1);
        group.addOption(option2);

        Map<String, Object> longOptMap = new HashMap<>();
        longOptMap.put("o2", option2);

        assertEquals("[ -o1 option1, --option2 option2]", group.toString(longOptMap));
    }
}