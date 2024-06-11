
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", false, "File option");
        optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option, optionGroup.getOptions().iterator().next());
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("f", optionGroup.getSelected());
            
            optionGroup.setSelected(option2);
            assertEquals("d", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_ThrowsAlreadySelectedException() throws AlreadySelectedException {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelected_NullOption() {
        Option option = new Option("f", "file", false, "File option");
        optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        
        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[ -f File option, -d Directory option ]";
        assertEquals(expected, optionGroup.toString());
    }
}
