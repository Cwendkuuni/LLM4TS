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
        OptionGroup result = optionGroup.addOption(option);
        assertSame("Check if option is added to the group", optionGroup, result);
        assertEquals("Check if option is added to the group", 1, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals("Check if names of options are retrieved correctly", 2, optionGroup.getNames().size());
        assertTrue("Check if names of options are retrieved correctly", optionGroup.getNames().contains("a"));
        assertTrue("Check if names of options are retrieved correctly", optionGroup.getNames().contains("r"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals("Check if options are retrieved correctly", 2, optionGroup.getOptions().size());
        assertTrue("Check if options are retrieved correctly", optionGroup.getOptions().contains(option1));
        assertTrue("Check if options are retrieved correctly", optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            assertEquals("Check if selected option is set correctly", "a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }

        try {
            optionGroup.setSelected(option2);
            assertEquals("Check if selected option is updated correctly", "r", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        assertNull("Check if initially selected option is null", optionGroup.getSelected());

        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        try {
            optionGroup.setSelected(option);
            assertEquals("Check if selected option is retrieved correctly", "a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetRequired() {
        assertFalse("Check if initially option group is not required", optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue("Check if option group is set as required", optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse("Check if initially option group is not required", optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue("Check if option group is required", optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse("Check if option group is not required", optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a Add option, -r Remove option]";
        assertEquals("Check if toString method returns correct string representation of the group", expected, optionGroup.toString());
    }
}