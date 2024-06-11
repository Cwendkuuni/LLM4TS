package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        OptionGroup result = optionGroup.addOption(option1);

        assertTrue(result.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);

        assertTrue(optionGroup.getNames().contains("a"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);

        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedWithAlreadySelectedException() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Pass the test
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[ -a Add option, -r Remove option]";
        assertEquals(expectedString, optionGroup.toString());
    }
}