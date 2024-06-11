package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        Option option3 = new Option("o", "opt3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(3, group.getOptions().size());
        assertEquals(option1, group.getOptions().stream().filter(o -> "opt1".equals(o.getKey())).findFirst().get());
        assertEquals(option2, group.getOptions().stream().filter(o -> "opt2".equals(o.getKey())).findFirst().get());
        assertEquals(option3, group.getOptions().stream().filter(o -> "opt3".equals(o.getKey())).findFirst().get());
        assertEquals(Arrays.asList("opt1", "opt2", "opt3"), group.getNames());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        Option option3 = new Option("o", "opt3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(Collections.unmodifiableList(Arrays.asList("opt1", "opt2", "opt3")), group.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        Option option3 = new Option("o", "opt3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(Collections.unmodifiableList(Arrays.asList(option1, option2, option3)), group.getOptions());
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        Option option3 = new Option("o", "opt3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals("opt1", group.getSelected());

        group.setSelected(option2);
        assertEquals("opt2", group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("opt1", group.getSelected());

        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        OptionGroup requiredGroup = new OptionGroup();
        OptionGroup optionalGroup = new OptionGroup();

        assertFalse(group.isRequired());
        group.setRequired(true);
        assertTrue(group.isRequired());

        assertFalse(requiredGroup.isRequired());
        requiredGroup.setRequired(true);
        assertTrue(requiredGroup.isRequired());

        assertFalse(optionalGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        Option option3 = new Option("o", "opt3", false, "Option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        StringBuilder expected = new StringBuilder("[");
        expected.append("-o opt1 Option 1,\n");
        expected.append("-o opt2 Option 2,\n");
        expected.append("-o opt3 Option 3]");

        assertEquals(expected.toString(), group.toString());
    }
}