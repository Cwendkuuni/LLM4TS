package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1");
        optionGroup.addOption(option);
        Map<String, Option> expectedOptionMap = new HashMap<>();
        expectedOptionMap.put("option1", option);
        Map<String, Option> actualOptionMap = (Map<String, Option>) optionGroup.getOptionMap();
        Assert.assertEquals(expectedOptionMap, actualOptionMap);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> expectedNames = Collections.singletonList("option1"); // order is not guaranteed
        Collection<String> actualNames = optionGroup.getNames();
        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> expectedOptions = Collections.singletonList(option1); // order is not guaranteed
        Collection<Option> actualOptions = optionGroup.getOptions();
        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelectedSameOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1");
        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();
        String expectedSelected = null;
        String actualSelected = optionGroup.getSelected();
        Assert.assertEquals(expectedSelected, actualSelected);
    }

    @Test
    public void testGetSelectedOptionName()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1");
        optionGroup.setSelected(option);
        String expectedSelected = "option1";
        String actualSelected = optionGroup.getSelected();
        Assert.assertEquals(expectedSelected, actualSelected);
    }

    @Test
    public void testSetRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        boolean expectedRequired = true;
        boolean actualRequired = optionGroup.isRequired();
        Assert.assertEquals(expectedRequired, actualRequired);
    }

    @Test
    public void testSetRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        boolean expectedRequired = false;
        boolean actualRequired = optionGroup.isRequired();
        Assert.assertEquals(expectedRequired, actualRequired);
    }

    @Test
    public void testToStringEmptyOptionGroup()
    {
        OptionGroup optionGroup = new OptionGroup();
        String expectedToString = "[ ]";
        String actualToString = optionGroup.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToStringSingleOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1");
        optionGroup.addOption(option);
        String expectedToString = "[-option1 Option 1]";
        String actualToString = optionGroup.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToStringMultipleOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expectedToString = "[-option1 Option 1, --option2 Option 2]";
        String actualToString = optionGroup.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }
}