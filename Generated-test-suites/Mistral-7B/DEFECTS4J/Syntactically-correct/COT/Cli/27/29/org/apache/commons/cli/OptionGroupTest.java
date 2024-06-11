package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup with JUnit 4.
 */
public class OptionGroupTest
{
    /**
     * Test adding an option to OptionGroup.
     */
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "optionA", false, "Description for option A");

        optionGroup.addOption(option);

        Collection options = optionGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.contains(option));
    }

    /**
     * Test getting names of options in OptionGroup.
     */
    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", false, "Description for option 1");
        Option option2 = new Option("option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();
        List expectedNames = new ArrayList();
        expectedNames.add(option1.getKey());
        expectedNames.add(option2.getKey());

        Assert.assertEquals(expectedNames, names);
    }

    /**
     * Test getting options in OptionGroup.
     */
    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", false, "Description for option 1");
        Option option2 = new Option("option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();
        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Assert.assertEquals(expectedOptions, options);
    }

    /**
     * Test setting the selected option.
     */
    @Test
    public void testSetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", false, "Description for option 1");
        Option option2 = new Option("option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            Assert.fail("AlreadySelectedException expected.");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("AlreadySelectedException", e.getClass().getName());
            Assert.assertEquals("An option from this group has already been selected.", e.getMessage());
        }

        optionGroup.setSelected(null);
    }

    /**
     * Test getting the selected option.
     */
    @Test
    public void testGetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", false, "Description for option 1");
        Option option2 = new Option("option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    /**
     * Test setting required OptionGroup.
     */
    @Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", false, "Description for option 1");
        Option option2 = new Option("option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[--option1 Description for option 1, --option2 Description for option 2]";
        Assert.assertEquals(expectedString, optionGroup.toString());
    }

    /**
     * Test selecting an option from required OptionGroup.
     */
    @Test(expected = IllegalStateException.class)
    public void testSelectOptionFromRequiredGroup()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", false, "Description for option");
        optionGroup.setRequired(true);
        optionGroup.addOption(option);

        optionGroup.setSelected(option);
    }

    /**
     * Test getting options with null optionGroup.
     */
    @Test(expected = NullPointerException.class)
    public void testGetOptionsFromNullGroup()
    {
        OptionGroup optionGroup = null;
        optionGroup.getOptions();
    }

    /**
     * Test getting names with null optionGroup.
     */
    @Test(expected = NullPointerException.class)
    public void testGetNamesFromNullGroup()
    {
        OptionGroup optionGroup = null;
        optionGroup.getNames();
    }
}