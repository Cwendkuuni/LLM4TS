package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    private OptionGroup optionGroup;

    @Before
    public void setUp()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "a", false, "Option A");
        Option option2 = new Option("b", "b", false, "Option B");
        Option option3 = new Option("c", "c", false, "Option C");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        this.optionGroup = optionGroup;
    }

    /**
     * Test method for {@link OptionGroup#getNames()}
     */
    @Test
    public void testGetNames()
    {
        List<String> names = new ArrayList<String>(optionGroup.getNames());

        Assert.assertEquals(3, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));
        Assert.assertTrue(names.contains("c"));
    }

    /**
     * Test method for {@link OptionGroup#getOptions()}
     */
    @Test
    public void testGetOptions()
    {
        List<Option> options = new ArrayList<Option>(optionGroup.getOptions());

        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
        Assert.assertEquals(option3, options.get(2));
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected()
    {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}
     */
    @Test
    public void testSetSelected()
    {
        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    /**
     * Test method for {@link OptionGroup#toString()}
     */
    @Test
    public void testToString()
    {
        String expected = "[--a Option A, --b Option B, --c Option C]";

        String actual = optionGroup.toString();

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link OptionGroup#setRequired(boolean)} and {@link OptionGroup#isRequired()}
     */
    @Test
    public void testSetRequired()
    {
        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#addOption(Option)} with null option
     */
    @Test(expected = NullPointerException.class)
    public void testAddOptionNullOption()
    {
        optionGroup.addOption(null);
    }

    /**
     * Test parsing with valid options
     */
    @Test
    public void testParseValidOptions() throws ParseException
    {
        String[] args = {"-a", "--b"};

        OptionParser parser = new OptionParser();
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        parser.parse(args, optionGroup);

        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    /**
     * Test parsing with invalid options
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidOptions() throws ParseException
    {
        String[] args = {"-d", "--e"};

        OptionParser parser = new OptionParser();
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        parser.parse(args, optionGroup);
    }
}