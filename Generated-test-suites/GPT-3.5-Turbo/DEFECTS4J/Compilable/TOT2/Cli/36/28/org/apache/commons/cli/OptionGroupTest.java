package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        optionGroup.addOption(option1);
        
        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        optionGroup.addOption(option1);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }

        Option option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option2);
            assertEquals("b", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }

        Option option3 = new Option("c", "option3", false, "Option 3");
        optionGroup.addOption(option3);
        
        try {
            optionGroup.setSelected(option3);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
}