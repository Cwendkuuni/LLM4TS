
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("f", "file", false, "File option");
        option2 = new Option("d", "directory", false, "Directory option");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        assertEquals("f", optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        
        assertNull(optionGroup.getSelected());
        
        try {
            optionGroup.setSelected(option2);
        } catch (AlreadySelectedException e) {
            assertEquals("[-f: File option]", e.getMessage());
        }
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals("[-f File option, -d Directory option]", optionGroup.toString());
    }
}
