package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "add", false, "Add option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    // Additional test cases for edge cases and exceptions can be added here

}