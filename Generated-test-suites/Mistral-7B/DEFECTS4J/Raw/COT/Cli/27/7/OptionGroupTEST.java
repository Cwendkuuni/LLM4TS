import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for OptionGroup class.
 */
public class OptionGroupTest
{
    private OptionGroup optionGroup;

    @Before
    public void setUp()
    {
        Option option1 = new Option("opt1", "Option 1", true, "o1");
        Option option2 = new Option("opt2", "Option 2", true, "o2");
        Option option3 = new Option("opt3", "Option 3", true, "o3");

        optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }

    @Test
    public void testAddOption()
    {
        Option option4 = new Option("opt4", "Option 4", true, "o4");

        optionGroup.addOption(option4);

        Map options = optionGroup.getOptions();
        Assert.assertEquals(4, options.size());
        Assert.assertTrue(options.containsKey(option4.getKey()));
    }

    @Test
    public void testGetNames()
    {
        Collection names = optionGroup.getNames();
        Assert.assertEquals(3, names.size());
        Assert.assertTrue(names.contains("opt1"));
        Assert.assertTrue(names.contains("opt2"));
        Assert.assertTrue(names.contains("opt3"));
    }

    @Test
    public void testSetSelectedNull()
    {
        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedSameOption()
    {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption()
    {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedAndGetSelected()
    {
        optionGroup.setSelected(option1);
        String selected = optionGroup.getSelected();
        Assert.assertEquals(option1.getOpt(), selected);
    }

    @Test
    public void testSetRequired()
    {
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired()
    {
        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        String expected = "[--opt1 Option 1\n--opt2 Option 2\n--opt3 Option 3\n]";
        String actual = optionGroup.toString();
        Assert.assertEquals(expected, actual);
    }
}