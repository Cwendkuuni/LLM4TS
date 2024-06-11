package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    // Basic test case for addOption method
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option = new Option("o", "option", false, "An option");
        OptionGroup result = optionGroup.addOption(option);
        
        assertTrue(result.getOptions().contains(option));
    }

    // Basic test case for getNames method
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("o1"));
        assertTrue(optionGroup.getNames().contains("o2"));
    }

    // Basic test case for getOptions method
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    // Basic test case for setSelected method
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("o1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    // Basic test case for getSelected method
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("o1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    // Basic test case for setRequired method
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }

    // Basic test case for isRequired method
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }

    // Basic test case for toString method
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expectedString = "[-o1 Option 1, -o2 Option 2]";
        
        assertEquals(expectedString, optionGroup.toString());
    }

    // Additional test case for setSelected method with AlreadySelectedException
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedException() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

}
