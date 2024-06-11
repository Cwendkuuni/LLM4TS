import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        Option option = new Option("option1", "opt1", true, "Option 1");
        Option addedOption = optionGroup.addOption(option);
        Option addedOption2 = optionGroup.addOption(option);

        // check if the option is added to the map
        Map<String, Option> optionMap = optionGroup.optionMap;
        Assert.assertEquals(option, optionMap.get("option1"));
        Assert.assertEquals(addedOption, optionMap.get("option1"));
        Assert.assertEquals(addedOption2, optionMap.get("option1"));
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "opt1", true, "Option 1");
        Option option2 = new Option("option2", "opt2", true, "Option 2");
        Option option3 = new Option("option3", "opt3", true, "Option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> names = new ArrayList<>(optionGroup.getNames());
        Collections.sort(names);

        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("option1");
        expectedNames.add("option2");
        expectedNames.add("option3");

        Assert.assertEquals(expectedNames, names);
    }

    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "opt1", true, "Option 1");
        Option option2 = new Option("option2", "opt2", true, "Option 2");
        Option option3 = new Option("option3", "opt3", true, "Option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
        Assert.assertEquals(option3, options.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "opt1", true, "Option 1");
        Option option2 = new Option("option2", "opt2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "opt1", true, "Option 1");
        Option option2 = new Option("option2", "opt2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        Assert.assertEquals("option1", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        OptionGroup optionGroupFalse = new OptionGroup();
        optionGroupFalse.setRequired(false);
        Assert.assertFalse(optionGroupFalse.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "opt1", true, "Option 1");
        Option option2 = new Option("option2", "opt2", true, "Option 2");
        Option option3 = new Option("option3", "opt3", true, "Option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expectedToString = "[--option1 Option 1, --option2 Option 2, --option3 Option 3 ]";
        Assert.assertEquals(expectedToString, optionGroup.toString());
    }
}