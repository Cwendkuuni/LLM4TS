package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "a-option", true, "a description");
        Option option2 = new Option("b", "b-option", false, "b description");

        OptionGroup addedOptionGroup = optionGroup.addOption(option1).addOption(option2);

        assertEquals(2, addedOptionGroup.getOptions().size());
        assertEquals(option1, addedOptionGroup.getOptions().iterator().next());
        assertEquals(option2, addedOptionGroup.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "a-option", true, "a description");
        Option option2 = new Option("b", "b-option", false, "b description");

        optionGroup.addOption(option1).addOption(option2);

        List<String> names = optionGroup.getNames();

        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "a-option", true, "a description");
        Option option2 = new Option("b", "b-option", false, "b description");

        optionGroup.addOption(option1).addOption(option2);

        List<Option> options = optionGroup.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "a-option", true, "a description");
        Option option2 = new Option("b", "b-option", false, "b description");

        optionGroup.addOption(option1).addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "a-option", true, "a description");
        Option option2 = new Option("b", "b-option", false, "b description");

        optionGroup.addOption(option1).addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "a-option", true, "a description");
        Option option2 = new Option("b", "b-option", false, "b description");

        optionGroup.addOption(option1).addOption(option2);

        String stringRepresentation = optionGroup.toString();

        assertEquals("[--a a description, --b b description]", stringRepresentation);
    }
}