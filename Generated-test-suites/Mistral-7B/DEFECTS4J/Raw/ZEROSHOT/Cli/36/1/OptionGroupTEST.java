package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        // Test creating an OptionGroup and adding Options
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description 1");
        Option option2 = new Option("o2", "option2", false, "Description 2");
        Option option3 = new Option("o3", "option3", false, "Description 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test getting the names and options of the OptionGroup
        List<String> names = Arrays.asList("o1", "o2", "o3");
        List<Option> options = Arrays.asList(option1, option2, option3);

        Assert.assertEquals(names, optionGroup.getNames());
        Assert.assertEquals(options, optionGroup.getOptions());

        // Test setting and getting the selected Option
        optionGroup.setSelected(option1);
        Assert.assertEquals(option1, optionGroup.getSelected());
        optionGroup.setSelected(option2);
        Assert.assertEquals(option2, optionGroup.getSelected());
        try {
            optionGroup.setSelected(option3);
            Assert.fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
        }
        optionGroup.setSelected(null);

        // Test setting and getting whether the OptionGroup is required
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    // Test other methods and edge cases as needed

    @Test(expected = AlreadySelectedException.class)
    public void testAlreadySelectedException() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description 1");
        Option option2 = new Option("o2", "option2", false, "Description 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    // Add more test methods as needed

    // Test constructor from a Map
    @Test
    public void testOptionGroupFromMap() {
        Map<String, Option> optionMap = new HashMap<>();
        Option option1 = new Option("o1", "option1", false, "Description 1");
        Option option2 = new Option("o2", "option2", false, "Description 2");

        optionMap.put("o1", option1);
        optionMap.put("o2", option2);

        OptionGroup optionGroup = new OptionGroup(optionMap);

        List<String> names = Arrays.asList("o1", "o2");
        List<Option> options = Arrays.asList(option1, option2);

        Assert.assertEquals(names, optionGroup.getNames());
        Assert.assertEquals(options, optionGroup.getOptions());
    }

    // Test other methods and edge cases as needed

    // Test toString method
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description 1");
        Option option2 = new Option("o2", "option2", false, "Description 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 Description 1, -o2 Description 2]";

        Assert.assertEquals(expected, optionGroup.toString());
    }
}