package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "test option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("o1"));
        assertTrue(optionGroup.getNames().contains("o2"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        // Selecting option1 for the first time should pass
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
        
        // Selecting option2 for the first time should pass
        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());
        
        // Selecting option1 again should pass
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        // Selecting option2 again should pass
        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());
        
        // Selecting null should reset the selected option
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Selecting option1 for the first time should pass
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        // Selecting option2 for the first time should throw AlreadySelectedException
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "test option 1");
        Option option2 = new Option("o2", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--option1 test option 1, --option2 test option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}