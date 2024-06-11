package org.apache.commons.cli;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class OptionGroupTest {

    OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "apple", false, "option 1");
        Option option2 = new Option("b", "banana", false, "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        Collection<Option> options = optionGroup.getOptions();

        Assert.assertEquals(2, names.size());
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelectedWithExistingOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "option 1");
        Option option2 = new Option("b", "banana", false, "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithDifferentOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "option 1");
        Option option2 = new Option("b", "banana", false, "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2); // should throw AlreadySelectedException
    }

    @Test
    public void testSetSelectedWithNullOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "option 1");
        Option option2 = new Option("b", "banana", false, "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "option 1");
        Option option2 = new Option("b", "banana", false, "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -a apple, --b banana ]";
        String actual = optionGroup.toString();

        Assert.assertEquals(expected, actual);

        option1.setDescription("new description");
        expected = "[ -a apple new description, --b banana ]";
        actual = optionGroup.toString();

        Assert.assertEquals(expected, actual);
    }
}