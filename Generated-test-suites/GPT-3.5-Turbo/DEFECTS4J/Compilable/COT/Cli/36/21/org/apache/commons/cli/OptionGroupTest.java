package org.apache.commons.cli;
import org.junit.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Assert.assertEquals(2, group.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Assert.assertEquals(2, group.getNames().size());
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Assert.assertEquals(2, group.getOptions().size());
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        
        Assert.assertEquals("a", group.getSelected());
    }
    
    @Test(expected=AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        
        Assert.assertEquals("a", group.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        
        Assert.assertTrue(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[option1, option2]";
        String actual = group.toString();
        
        Assert.assertEquals(expected, actual);
    }
}