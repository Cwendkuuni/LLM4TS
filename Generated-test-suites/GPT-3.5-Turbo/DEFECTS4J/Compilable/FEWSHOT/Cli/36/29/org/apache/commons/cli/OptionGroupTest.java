package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<Option> options = optionGroup.getOptions();
        
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup= new OptionGroup();
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<String> names = optionGroup.getNames();
        
        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup= new OptionGroup();
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<Option> options = optionGroup.getOptions();
        
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        
        try {
            optionGroup.addOption(option1);
            optionGroup.setSelected(option1);
            assertEquals(option1.getKey(), optionGroup.getSelected());
            
            optionGroup.setSelected(option2);
            assertEquals(option2.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
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
        Option option1 = new Option("a", "desc1");
        Option option2 = new Option("b", "desc2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[a desc1, b desc2]";
        
        assertEquals(expected, optionGroup.toString());
    }
}