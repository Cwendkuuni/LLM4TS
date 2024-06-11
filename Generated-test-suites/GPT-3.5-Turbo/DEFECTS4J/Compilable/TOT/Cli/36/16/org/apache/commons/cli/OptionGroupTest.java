package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    
    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option);
        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(1, names.size());
        Assert.assertTrue(names.contains(option.getKey()));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("u", "update", false, "Update option");
        Option option2 = new Option("f", "force", false, "Force option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected_WhenOptionNull() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testSetSelected_WhenNoOptionSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_WhenOptionAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString_WhenOptionsHaveOpt() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a Add option, -r Remove option]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
    
    @Test
    public void testToString_WhenOptionsHaveLongOpt() {
        Option option1 = new Option(null, "add", false, "Add option");
        Option option2 = new Option(null, "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[--add Add option, --remove Remove option]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
    
    @Test
    public void testToString_WhenOptionsHaveDescription() {
        Option option1 = new Option("a", null, false, "Add option");
        Option option2 = new Option("r", null, false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a Add option, -r Remove option]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
    
    @Test
    public void testToString_WhenOptionsHaveOptAndDescription() {
        Option option1 = new Option("a", null, false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a Add option, --remove Remove option]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
    
    @Test
    public void testToString_WhenMultipleOptions() {
        Option option1 = new Option("a", null, false, "Add option");
        Option option2 = new Option(null, "remove", false, "Remove option");
        Option option3 = new Option("u", "update", false, "Update option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        String expected = "[-a Add option, --remove Remove option, -u Update option]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
}