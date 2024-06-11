package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("f", "file", true, "input file");
        optionGroup.addOption(option1);
        
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().iterator().next());
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("v", "verbose", false, "enable verbose mode");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("v"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("v", "verbose", false, "enable verbose mode");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("v", "verbose", false, "enable verbose mode");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("v", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithSameOption() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", true, "input file");
        optionGroup.addOption(option1);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }
    
    @Test
    public void testSetSelectedWithNullOption() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", true, "input file");
        optionGroup.addOption(option1);
        
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("v", "verbose", false, "enable verbose mode");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[ -f input file, -v enable verbose mode ]";
        
        assertEquals(expected, optionGroup.toString());
    }
}