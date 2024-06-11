package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", false, "description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", false, "description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getNames().size());
        Assert.assertTrue(optionGroup.getNames().contains("a"));
        Assert.assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", false, "description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option1));
        Assert.assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", false, "description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        // Exception handling scenario - selecting an already selected option
        try {
            optionGroup.setSelected(option1);
            Assert.fail("Expected AlreadySelectedException not thrown");
        } catch (AlreadySelectedException e) {
            // Expected exception thrown
        }

        // Exception handling scenario - selecting an option not in the group
        try {
            Option option3 = new Option("c", "option3", false, "description");
            optionGroup.setSelected(option3);
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", false, "description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("b", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", false, "description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a option1, -b option2]";
        Assert.assertEquals(expected, optionGroup.toString());
    }

    // Additional test cases for edge cases and exceptions can be added here

}