package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
        
        assertEquals("b", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }
    
    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option1", false, "Description");
        Option option2 = new Option("b", "option2", false, "Description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
        
        assertEquals("b", optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("b", "option2", false, "Description 2");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals("[--option1 Description 1, --option2 Description 2]", optionGroup.toString());
    }

}