package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    OptionGroup optionGroup;

    @Before
    public void setup()  {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(option.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(option.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a Add option, -d Delete option]";
        assertEquals(expected, optionGroup.toString());
    }
}