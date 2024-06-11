import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest
{
    @Test
    public void testAddOption() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionA", "Option A", false, "a");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("o1");
        expectedNames.add("o2");

        List<String> actualNames = new ArrayList<>();
        for (Object name : optionGroup.getNames().toArray())
        {
            actualNames.add((String) name);
        }

        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetOptions() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        List<Option> actualOptions = new ArrayList<>();
        for (Object option : optionGroup.getOptions().toArray())
        {
            actualOptions.add((Option) option);
        }

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        Option option3 = new Option("option3", "Option 3", false, "o3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expectedString = "[-o1 Option 1\n, -o2 Option 2\n, --option3 Option 3]";
        String actualString = optionGroup.toString();

        Assert.assertEquals(expectedString, actualString);
    }
}