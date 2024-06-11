package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        Option option3 = new Option("h", "help", false, "Help option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        optionGroup.addOption(option1);
        assertEquals(1, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        optionGroup.addOption(option1);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertNull(optionGroup.getSelected());
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());
        Exception exception = assertThrows(AlreadySelectedException.class, () -> {
            optionGroup.setSelected(option1);
        });
        assertNotNull(exception);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertNull(optionGroup.getSelected());
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expectedString = "[-f File option, -d Directory option]";
        assertEquals(expectedString, optionGroup.toString());
    }
}