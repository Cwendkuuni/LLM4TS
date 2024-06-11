package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the Options class.
 */
public class OptionsTest
{
    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} .
     */
    @Test
    public void testAddOption()
    {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);

        Option expectedOption = new Option("a", null, false, "Description for option A");
        Assert.assertEquals(expectedOption, options.getOption("a"));
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with arg.
     */
    @Test
    public void testAddOptionWithArg()
    {
        Options options = new Options();
        Option option = new Option("a", "optionA", true, "Description for option A");
        options.addOption(option);

        Option expectedOption = new Option("a", null, true, "Description for option A");
        Assert.assertEquals(expectedOption, options.getOption("a"));
    }

    /**
     * Test method for {@link Options#addOption(String, String)} with short opt.
     */
    @Test
    public void testAddOptionShort()
    {
        Options options = new Options();
        options.addOption("a", "Option A");

        Option expectedOption = new Option('a', null, false, "Option A");
        Assert.assertEquals(expectedOption, options.getOption("a"));
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean)} with short opt and arg.
     */
    @Test
    public void testAddOptionShortWithArg()
    {
        Options options = new Options();
        options.addOption('a', "Option A", true);

        Option expectedOption = new Option('a', null, true, "Option A");
        Assert.assertEquals(expectedOption, options.getOption("a"));
    }

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)} and {@link Options#getOptionGroups()}.
     */
    @Test
    public void testAddOptionGroup()
    {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("group1", "Group 1");
        Option option1 = new Option("g1", "option1", false, "Option 1 in group 1");
        optionGroup.addOption(option1);

        options.addOptionGroup(optionGroup);

        List<OptionGroup> expectedOptionGroups = new ArrayList<>();
        OptionGroup expectedOptionGroup = new OptionGroup("group1", "Group 1");
        expectedOptionGroup.addOption(option1);
        expectedOptionGroups.add(expectedOptionGroup);

        Assert.assertEquals(expectedOptionGroups, options.getOptionGroups());
    }

    /**
     * Test method for {@link Options#hasOption(String)} and {@link Options#hasLongOption(String)}.
     */
    @Test
    public void testHasOption()
    {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Description for option A");
        options.addOption(option);

        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasLongOption("optionA"));
        Assert.assertFalse(options.hasOption("b"));
        Assert.assertFalse(options.hasLongOption("optionB"));
    }

    /**
     * Test method for {@link Options#getMatchingOptions(String)} with a partial long option name.
     */
    @Test
    public void testGetMatchingOptionsWithPartialName()
    {
        Options options = new Options();
        Option option1 = new Option("longOpt1", "Option 1");
        Option option2 = new Option("longOpt2", "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        List<String> expectedOptions = new ArrayList<>();
        expectedOptions.add("longOpt1");
        expectedOptions.add("longOpt2");

        List<String> matchingOptions = options.getMatchingOptions("opt");

        Assert.assertEquals(expectedOptions, matchingOptions);
    }

    /**
     * Test method for {@link Options#toString()}.
     */
    @Test
    public void testToString()
    {
        Options options = new Options();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        String expectedString = "[ Options: [ short {option1=Option 1}, option2=Option 2} ] [ long {}option1=Option 1, option2=Option 2] ]";

        String actualString = options.toString();

        Assert.assertEquals(expectedString, actualString);
    }
}