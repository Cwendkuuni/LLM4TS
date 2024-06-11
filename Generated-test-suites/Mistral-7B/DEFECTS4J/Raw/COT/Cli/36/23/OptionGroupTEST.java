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

public class OptionGroupTest
{
    private OptionGroup optionGroup;

    @Before
    public void setUp()
    {
        optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option one", false, "o1-desc");
        Option option2 = new Option("o2", "option two", false, "o2-desc");
        Option option3 = new Option("o3", "option three", false, "o3-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }

    @Test
    public void testGetNames()
    {
        Collection<String> names = optionGroup.getNames();
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("o1");
        expectedNames.add("o2");
        expectedNames.add("o3");

        Assert.assertEquals(expectedNames, names);
    }

    @Test
    public void testGetOptions()
    {
        Collection<Option> options = optionGroup.getOptions();
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        expectedOptions.add(option3);

        Assert.assertEquals(expectedOptions, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option nullOption = null;

        optionGroup.addOption(nullOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullKey()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, "option description", false, "option-key");

        optionGroup.addOption(option);
    }

    @Test
    public void testSetSelectedNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o1", "option one", false, "o1-desc");

        optionGroup.setSelected(option);
        Assert.assertEquals("o1", optionGroup.getSelected());

        optionGroup.setSelected(null);
        Assert.assertEquals(null, optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedSameOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one", false, "o1-desc");

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one", false, "o1-desc");
        Option option2 = new Option("o2", "option two", false, "o2-desc");

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }
}