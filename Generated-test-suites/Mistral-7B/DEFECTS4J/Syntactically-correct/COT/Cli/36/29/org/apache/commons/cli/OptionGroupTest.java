package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for OptionGroup class
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("testOption", "Test Option", true, "test");
        group.addOption(option);

        Option addedOption = group.getOptions().iterator().next();
        Assert.assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        List<String> names = new ArrayList<>(group.getNames());
        List<String> expectedNames = Arrays.asList(option1.getKey(), option2.getKey());

        Assert.assertEquals(expectedNames, names);
    }

    @Test
    public void testSetSelected() throws Exception {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        try {
            group.setSelected(option1);
            String selected = group.getSelected();
            Assert.assertEquals(option1.getKey(), selected);
        } finally {
            group.setSelected(null);
        }

        try {
            group.setSelected(option2);
            String selected = group.getSelected();
            Assert.assertEquals(option2.getKey(), selected);
        } finally {
            group.setSelected(null);
        }

        try {
            group.setSelected(option1);
            Assert.throwsException(AlreadySelectedException.class, () -> {
                group.setSelected(option1);
            });
        } finally {
            group.setSelected(null);
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("option", "Option");
        group.addOption(option);

        Assert.assertFalse(group.isRequired());

        group.setRequired(true);
        Assert.assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        Option option3 = new Option("option3", "Option 3", true);
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        String expected = "[--option1 Test Option 1, --option2 Test Option 2, -test Test]";
        String result = group.toString();

        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOption() {
        OptionGroup group = new OptionGroup();
        group.addOption(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSelectedNullOption() throws Exception {
        OptionGroup group = new OptionGroup();
        Option option = new Option("option", "Option");
        group.addOption(option);

        group.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwice() throws Exception {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testEmptyOptionGroup() {
        OptionGroup group = new OptionGroup();
        Assert.assertFalse(group.isRequired());
        Assert.assertTrue(group.getOptions().isEmpty());
        Assert.assertTrue(group.getNames().isEmpty());
    }

    @Test
    public void testOptionGroupWithOnlyRequiredOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("option", "Option", true);
        group.addOption(option);

        Assert.assertTrue(group.isRequired());
        Assert.assertFalse(group.getOptions().isEmpty());
        Assert.assertFalse(group.getNames().isEmpty());
    }

    @Test
    public void testOptionGroupWithMixedOptions() {
        OptionGroup group = new OptionGroup();
        Option requiredOption = new Option("requiredOption", "Required Option", true);
        Option nonRequiredOption = new Option("nonRequiredOption", "Non Required Option");
        group.addOption(requiredOption);
        group.addOption(nonRequiredOption);

        Assert.assertFalse(group.isRequired());
        Assert.assertFalse(group.getOptions().isEmpty());
        Assert.assertFalse(group.getNames().isEmpty());
    }
}