package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testSetSelected_WithNullOption() throws AlreadySelectedException {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_SelectedSameOptionTwice() throws AlreadySelectedException {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals("[--add Add option, --remove Remove option]", optionGroup.toString());
    }
}