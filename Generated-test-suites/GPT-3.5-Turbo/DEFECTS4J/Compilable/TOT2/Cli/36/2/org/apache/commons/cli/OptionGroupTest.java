package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("-a", "Option A");
        Option option2 = new Option("-b", "Option B");
        
        group.addOption(option1);
        assertTrue(group.getNames().contains("-a"));
        
        group.addOption(option2);
        assertTrue(group.getNames().contains("-b"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("-a", "Option A");
        
        group.addOption(option1);
        
        try {
            group.setSelected(option1);
            assertEquals("-a", group.getSelected());
            
            Option option2 = new Option("-b", "Option B");
            group.setSelected(option2);
            fail("Exception not thrown for reselecting option");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("-a", "Option A");
        Option option2 = new Option("--b", "Option B");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-a Option A, --b Option B]";
        assertEquals(expected, group.toString());
    }
}