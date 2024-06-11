package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    /**
     * Test adding options to an OptionGroup and getting their names and options.
     */
    @Test
    public void testAddOptionsAndGetNamesAndOptions() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        Option option3 = new Option("option3", "Option 3", false, "o3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> expectedOptionNames = new ArrayList<>();
        expectedOptionNames.add("option1");
        expectedOptionNames.add("option2");
        expectedOptionNames.add("option3");

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        expectedOptions.add(option3);

        List<String> actualOptionNames = new ArrayList<>(optionGroup.getNames());
        List<Option> actualOptions = new ArrayList<>(optionGroup.getOptions());

        Assert.assertEquals("Incorrect number of option names.", expectedOptionNames.size(), actualOptionNames.size());
        Assert.assertEquals("Incorrect option names.", expectedOptionNames, actualOptionNames);

        Assert.assertEquals("Incorrect number of options.", expectedOptions.size(), actualOptions.size());
        Assert.assertEquals("Incorrect options.", expectedOptions, actualOptions);
    }

    /**
     * Test setting and getting the selected option.
     */
    @Test
    public void testSetAndGetSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String selectedOption = optionGroup.getSelected();
            Assert.assertEquals("Selected option is not the one that was set.", option1.getKey(), selectedOption);
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option2);
            String selectedOption = optionGroup.getSelected();
            Assert.assertNull("Selected option is not null when no option is selected.", selectedOption);
        } finally {
            optionGroup.setSelected(option1);
        }

        try {
            optionGroup.setSelected(new Option("unknownOption", "Unknown option", false, "unknown"));
            String selectedOption = optionGroup.getSelected();
            Assert.assertNull("Selected option is not null when an unknown option is set.", selectedOption);
        } finally {
            optionGroup.setSelected(option1);
        }
    }

    /**
     * Test setting the required flag.
     */
    @Test
    public void testSetAndGetRequiredFlag() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        Assert.assertTrue("OptionGroup is not required.", optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse("OptionGroup is required.", optionGroup.isRequired());
    }

    /**
     * Test getting the stringified version of an OptionGroup.
     */
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedOptionGroupString = "[--option1, --option2]";

        String actualOptionGroupString = optionGroup.toString();

        Assert.assertEquals("Incorrect stringified version of OptionGroup.", expectedOptionGroupString, actualOptionGroupString);
    }
}