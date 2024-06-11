package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;
  
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", false, "file option");
        assertEquals(optionGroup, optionGroup.addOption(option));
    }
  
    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        optionGroup.addOption(option1).addOption(option2);
      
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }
  
    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        optionGroup.addOption(option1).addOption(option2);
      
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
  
    @Test
    public void testSetSelected() {
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        optionGroup.addOption(option1).addOption(option2);
      
        try {
            optionGroup.setSelected(option1);
            assertEquals("f", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
  
    @Test
    public void testSetSelected_NullOption() {
        Option option1 = new Option("f", "file", false, "file option");
        optionGroup.addOption(option1);
      
        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
  
    @Test
    public void testSetSelected_AlreadySelectedException() {
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        optionGroup.addOption(option1).addOption(option2);
      
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException, but it was not thrown");
        } catch (AlreadySelectedException e) {
            assertEquals("The option '--directory' has already been selected", e.getMessage());
        }
    }
  
    @Test
    public void testGetSelected() {
        Option option1 = new Option("f", "file", false, "file option");
        optionGroup.addOption(option1);
      
        try {
            optionGroup.setSelected(option1);
            assertEquals("f", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
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
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "directory", false, "directory option");
        optionGroup.addOption(option1).addOption(option2);
      
        assertEquals("[-f file option, -d directory option]", optionGroup.toString());
    }
}