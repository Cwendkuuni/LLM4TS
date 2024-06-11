package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "a");
        Option option2 = new Option("b", "option2", false, "b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "a");
        Option option2 = new Option("b", "option2", false, "b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(Arrays.asList("a", "b"), optionGroup.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "a");
        Option option2 = new Option("b", "option2", false, "b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(Collections.unmodifiableList(Arrays.asList(option1, option2)), optionGroup.getOptions());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "a");
        Option option2 = new Option("b", "option2", false, "b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // expected
        }

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            assertEquals("b", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "a");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();

        assertFalse(optionGroup1.isRequired());
        optionGroup2.setRequired(true);
        assertTrue(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "a");
        Option option2 = new Option("b", "option2", false, "b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        StringBuilder expected = new StringBuilder();
        expected.append("[");
        expected.append("-a option1");
        expected.append("]");

        assertEquals(expected.toString(), optionGroup.toString());
    }
}