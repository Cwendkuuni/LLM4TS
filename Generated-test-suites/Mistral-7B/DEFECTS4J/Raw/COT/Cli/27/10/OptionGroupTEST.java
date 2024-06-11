import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "a", false, "Option A");
        Option option2 = new Option("b", "b", false, "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Map optionMap = optionGroup.getOptionMap();
        Set expectedOptionNames = new HashSet();
        expectedOptionNames.add("a");
        expectedOptionNames.add("b");

        Assert.assertEquals(expectedOptionNames, optionMap.keySet());
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "a", false, "Option A");
        Option option2 = new Option("b", "b", false, "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List actualOptionNames = new ArrayList(optionGroup.getNames());
        List expectedOptionNames = new ArrayList();
        expectedOptionNames.add("a");
        expectedOptionNames.add("b");

        Assert.assertEquals(expectedOptionNames, actualOptionNames);
    }

    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "a", false, "Option A");
        Option option2 = new Option("b", "b", false, "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List actualOptions = new ArrayList(optionGroup.getOptions());
        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "a", false, "Option A");
        Option option2 = new Option("b", "b", false, "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "a", false, "Option A");
        Option option2 = new Option("b", "b", false, "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "a", false, "Option A");
        Option option2 = new Option("b", "b", false, "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--Option A Option A\n--Option B Option B\n]", optionGroup.toString());
    }
}