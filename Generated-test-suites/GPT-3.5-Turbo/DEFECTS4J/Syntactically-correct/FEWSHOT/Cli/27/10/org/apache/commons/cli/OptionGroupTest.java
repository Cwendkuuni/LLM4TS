
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        group.setSelected(option2);
        assertEquals("b", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2); // should throw AlreadySelectedException
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "first option");
        
        group.addOption(option1);
        
        group.setSelected(option1);
        group.setSelected(null);
        
        assertEquals(null, group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[ -a first option, -b second option ]";
        
        assertEquals(expected, group.toString());
    }
}
