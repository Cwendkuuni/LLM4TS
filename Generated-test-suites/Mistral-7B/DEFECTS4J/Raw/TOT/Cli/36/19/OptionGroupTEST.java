package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Test for OptionGroup class.
 */
public class OptionGroupTest
{
    @Test
    public void testOptionGroup() throws ParseException
    {
        // Create an OptionGroup
        OptionGroup optionGroup = new OptionGroup();

        // Add options to the OptionGroup
        Option option1 = new Option("o1", true, "Option 1");
        Option option2 = new Option("o2", true, "Option 2");
        Option option3 = new Option("o3", true, "Option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test getNames() method
        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(Arrays.asList("o1", "o2", "o3"), names);

        // Test getOptions() method
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(Arrays.asList(option1, option2, option3), options);

        // Test setSelected() method with null value
        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());

        // Test setSelected() method with already selected option
        optionGroup.setSelected(option1);
        Assert.assertEquals("o1", optionGroup.getSelected());

        // Test setSelected() method with different option
        optionGroup.setSelected(option2);
        Assert.assertEquals("o2", optionGroup.getSelected());

        // Test isRequired() method with default value
        Assert.assertFalse(optionGroup.isRequired());

        // Test isRequired() method with required value
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwice() throws ParseException
    {
        // Create an OptionGroup
        OptionGroup optionGroup = new OptionGroup();

        // Add options to the OptionGroup
        Option option1 = new Option("o1", true, "Option 1");
        Option option2 = new Option("o2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Test setSelected() method with same option twice
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }
}