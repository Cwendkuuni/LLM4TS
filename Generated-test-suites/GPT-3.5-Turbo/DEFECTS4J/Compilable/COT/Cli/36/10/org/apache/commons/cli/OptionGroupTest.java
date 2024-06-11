package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        OptionGroup result = optionGroup.addOption(option);
        Assert.assertEquals(optionGroup, result);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        Collection<String> names = optionGroup.getNames();
        Assert.assertTrue(names.contains("a"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertTrue(options.contains(option));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        Assert.assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        Assert.assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String result = optionGroup.toString();
        Assert.assertEquals("[-a option a, -b option b]", result);
    }
}